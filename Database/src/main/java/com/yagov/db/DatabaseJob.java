package com.yagov.db;

import java.io.File;
import java.io.FileFilter;

import com.yagov.util.Config;

public class DatabaseJob {
	
	private Config config;
	private String congressDataPath;
	private String dbName;
	private MongoFacade mongo;
	
	public DatabaseJob(Config config) {
		
		this.config = config;
		
		this.congressDataPath = config.getProperty("congressDataPath");
		
		this.dbName = config.getProperty("dbName");
		this.mongo = new MongoFacade(dbName);

	}
	
	
	/** 
	 * 	Populates the database with any newly changed files
	 */
	public void updateDatabase() {
		
		File dir = new File(congressDataPath);
		for(File congDir : dir.listFiles()) {
			parseCongressDir(congDir);
		}
	}
	
	public void parseCongressDir(File dir) {
		
		Integer congress = Integer.parseInt(dir.getName());
		if(congress > 113) {
			System.out.println("Parsing new bill data for the " + congress + "th congress...");
			
			File congDir = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return "bills".equals(pathname.getName());
				}
			})[0];
			
			File[] billDirs = congDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return "s".equals(pathname.getName())
							|| "hr".equals(pathname.getName());
				}
			}); 
			
			for(File billDir : billDirs) {
				parseBillDir(billDir, congress);
			}
		}
	}
	
	public void parseBillDir(File dir, Integer congress) {
		
		
		
	}

}
