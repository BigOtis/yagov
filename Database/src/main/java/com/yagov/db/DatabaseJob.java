package com.yagov.db;

import java.io.File;
import java.io.FileFilter;

import com.yagov.util.Config;

public class DatabaseJob {
	
	private Config config;
	private String congressDataPath;
	private String dbName;
	private MongoFacade mongo;
	
	private Integer billsProcessed = 0;
	
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
		
		// Load in updated bill data
		File dir = new File(congressDataPath);
		for(File congDir : dir.listFiles()) {
			parseCongressDir(congDir);
		}
	}
	
	/**
	 * Parses a directory pulled from ther US Congress parsers:
	 *     https://github.com/unitedstates/congress
	 *     
	 * Stores the data in a MongoDB with the following collections:
	 * 	BillText-nnn (nnn = congress number, ie 114, 115)
	 *   - The full text bill
	 *  Bills-nnn     
	 *   - The metadata for a bill
	 *  People 
	 *   - Info about the individual members of congress
	 * @param dir
	 */
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
		
		for(File f : dir.listFiles()) {
			try {
				// Update the full text from this bill, if it has changed
				File billTextDir = new File(f.getAbsolutePath() + "\\text-versions");
				File billTextFile = billTextDir.listFiles()[0];
				billTextFile = new File(billTextFile.getAbsolutePath() + "\\document.txt");
				mongo.updateBillText(congress, f.getName(), billTextFile);
	
				// Update the metadata for this bill if it has changed
				File billJSON = new File(f.getAbsolutePath() + "\\data.json");
				mongo.updateBillJSON(congress, f.getName(), billJSON);
				
				if((billsProcessed % 1000) == 0) {
					System.out.println("Bills processed: " + billsProcessed);
					
				}
				billsProcessed++;
			}
			catch(Exception e) {
				// The bill JSON likely was not downloaded yet
				// - just skip this bill for now
				System.out.println("Error parsing bill: " + f.getAbsolutePath());
				e.printStackTrace();
			}
		}		
	}

}
