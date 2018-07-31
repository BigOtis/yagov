package com.yagov.driver;

import com.yagov.db.DatabaseJob;
import com.yagov.util.Config;

public class DBDriver {

	public static void main(String[] args) {

	
		Config config = new Config(args[0]);
		
		DatabaseJob dbJob = new DatabaseJob(config);
		dbJob.updateDatabase();
		
	}
	
}
