package com.yagov.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Simple interface that abstracts calls to 
 * the US Congress Mongo database
 * 
 * @author Phillip Lopez - pgl5711@rit.edu
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
	
	public void updateBillText(Integer congress, String billName, String text) {
		
		MongoCollection<Document> billCollection = db.getCollection("BillText-"+congress);
		FindIterable<Document> bills = billCollection.find(new Document("bill_id", getBillID(congress, billName)));
		Document bill = bills.first();
		if(bill == null) {
			bill = new Document("bill_id", getBillID(congress, billName));
			bill.append("text", text);
			billCollection.insertOne(bill);
		}
		else {
			bill.append("text", text);
			billCollection.replaceOne(getID(bill), bill);
		}
		
	}
	
	public String getBillText(Integer congress, String billName) {
		MongoCollection<Document> billCollection = db.getCollection("BillText-"+congress);
		FindIterable<Document> bills = billCollection.find(new Document("bill_id", getBillID(congress, billName)));
		Document bill = bills.first();
		if(bill == null){
			return null;
		}
		
		return bill.getString("text");

	}
	
	public String getBillID(Integer congress, String billName) {
		return congress + "-" + billName;
	}
	
	public Document getID(Document original) {
		return new Document("_id", original.get("_id"));
	}
}
