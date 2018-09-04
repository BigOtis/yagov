package com.yagov.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

/**
 * Simple interface that abstracts calls to 
 * the US Congress Mongo database
 * 
 * @author Phillip Lopez
 *
 */
public class MongoFacade {
	
	/**
	 * MongoClient API
	 */
	private MongoClient mongo;
	
	/**
	 * MongoDatabase API
	 */
	private MongoDatabase db;
	
	/**
	 * Establishes a connection to the local MongoDB
	 * defined in the system properties
	 * 
	 * @param dbName
	 */
	public MongoFacade(String dbName){
				
        try {
			System.getProperties().load(new FileInputStream("mongo.properties"));
		} catch (IOException e) {
			String myCurrentDir = System.getProperty("user.dir") + File.separator + 
					System.getProperty("sun.java.command").substring(0, 
							System.getProperty("sun.java.command").lastIndexOf(".")) .replace(".", File.separator); 
			System.out.println(myCurrentDir);
			System.err.println("MISSING MONGO.PROPERTIES FILE. DB WILL NOT LOAD CORRECTLY.");
		}
		mongo = new MongoClient(System.getProperty("mongo.address"), 
				Integer.valueOf(System.getProperty("mongo.port")));		
		db = mongo.getDatabase(dbName);
	}
	
	public Document getBillDoc(Integer congress, String billName) {
		
		MongoCollection<Document> billCollection = db.getCollection("Bills-"+congress);
		FindIterable<Document> bills = billCollection.find(
				new Document("bill_id", getBillID(congress, billName)));
		Document bill = bills.first();
		return bill;
	}
	
	/**
	 * Updates a Bill document from the given JSON File
	 *  If no BillText doc exists, a new one will be created
	 * @param congress
	 * @param billName
	 * @param text
	 */
	public void updateBillJSON(Integer congress, String billName, File billJSON) {
		
		MongoCollection<Document> billCollection = db.getCollection("Bills-"+congress);
		FindIterable<Document> bills = billCollection.find(
				new Document("bill_id", getBillID(congress, billName)))
					.projection(Projections.include("_id", "lastModified"));
				
		Date lastModified = new Date(billJSON.lastModified());
		Document bill = bills.first();
		if(bill == null) {
			bill = Document.parse(getStringFromFile(billJSON));
			bill.append("bill_id", getBillID(congress, billName));
			bill.append("lastModified", lastModified);
			billCollection.insertOne(bill);
		}
		else {
			if(billWasModified(bill, lastModified)){
				Document newBill = Document.parse(getStringFromFile(billJSON));
				newBill.append("bill_id", getBillID(congress, billName));
				newBill.append("lastModified", lastModified);
				billCollection.replaceOne(getID(bill), newBill);
			}
		}
	}
	
	/**
	 * Updates a BillText document with the provided text. 
	 *  If no BillText doc exists, a new one will be created
	 * @param congress
	 * @param billName
	 * @param text
	 */
	public void updateBillText(Integer congress, String billName, File billFile) {
		
		MongoCollection<Document> billCollection = db.getCollection("BillText-"+congress);
		FindIterable<Document> bills = billCollection.find(
				new Document("bill_id", getBillID(congress, billName)))
					.projection(Projections.include("_id", "lastModified"));
				
		Date lastModified = new Date(billFile.lastModified());
		Document bill = bills.first();
		
		if(bill == null) {
			bill = new Document("bill_id", getBillID(congress, billName));
			bill.append("text", getStringFromFile(billFile));
			bill.append("lastModified", lastModified);
			billCollection.insertOne(bill);
		}
		else if(billWasModified(bill, lastModified)){
			bill.append("text", getStringFromFile(billFile));
			bill.append("lastModified", lastModified);
			billCollection.replaceOne(getID(bill), bill);
		}
	}
	
	/**
	 * Returns the full String text of the passed in billName
	 * @param congress - # of congress, ie 115, 116
	 * @param billName - bill name, ie hr1, s1 
	 * 
	 * @return
	 */
	public String getBillText(Integer congress, String billName) {
		MongoCollection<Document> billCollection = db.getCollection("BillText-"+congress);
		FindIterable<Document> bills = billCollection.find(new Document("bill_id", getBillID(congress, billName)));
		Document bill = bills.first();
		if(bill == null){
			return null;
		}
		
		return bill.getString("text");

	}
	
	/**
	 * Checks the lastModified field on a given Bill document
	 * against the given last modified date of a file
	 * 
	 * @param bill
	 * @param fileLastModified
	 * @return true, if the bill date is older than the fileLastModified date
	 */
	public boolean billWasModified(Document bill, Date fileLastModified) {
		
		Date lastModifiedDB = bill.getDate("lastModified");
		return DateUtils.round(fileLastModified, Calendar.HOUR)
				.after(DateUtils.round(lastModifiedDB, Calendar.HOUR));
		
	}
	
	/**
	 * Creates a billID from a congress # and billName
	 * 
	 * @param congress
	 * @param billName
	 * @return
	 */
	public String getBillID(Integer congress, String billName) {
		return congress + "-" + billName;
	}
	
	
	/**
	 * Gets the MongoDB _id of a doc
	 * 
	 * @param original
	 * @return
	 */
	public Document getID(Document original) {
		return new Document("_id", original.get("_id"));
	}
	
	/**
	 * Loads in all of the text from a given
	 * file and returns it as a String
	 * @param file
	 * @return entire file as String
	 */
	public String getStringFromFile(File file) {
		
		String text = file.getName();
		try {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(file).useDelimiter("\\Z");
			text = scan.next();
			scan.close();				
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return text;
	}
}
