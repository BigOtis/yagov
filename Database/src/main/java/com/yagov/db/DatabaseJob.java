package com.yagov.db;

import com.yagov.util.Config;

public class DatabaseJob {
	
	private Config config;
	private String congressDataPath;
	
	public DatabaseJob(Config config) {
		
		this.config = config;
		this.congressDataPath = config.getProperty("congressDataPath");
	}
	
	
	/** 
	 * 	Performs the initial creation/setup of the YaGov MongoDB
	 */
	public void createDatabase() {
		
		
		
	}

}
