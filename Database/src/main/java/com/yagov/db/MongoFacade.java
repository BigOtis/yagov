package com.yagov.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.mongodb.MongoClient;
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
	
	private String dbName;
	
	public MongoFacade(String dbName){
		
		this.dbName = dbName;
		
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
	
	
}
