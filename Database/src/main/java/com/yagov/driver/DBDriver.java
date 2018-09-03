package com.yagov.driver;

import com.yagov.db.DatabaseJob;
import com.yagov.util.Config;

/**
 * 
 * @author Phil Lopez
 *
 */
public class DBDriver {

	/**
	 * Main class that updates the DB with any 
	 * new data from the US Congress scraper files
	 * @param args
	 */
	public static void main(String[] args) {

	
		Config config = new Config(args[0]);
		
		DatabaseJob dbJob = new DatabaseJob(config);
		dbJob.updateDatabase();
		
	}
	
}
