package com.yagov.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Phil Lopez
 *
 */
public class Config {
	
	public Config(String configFileName) {
				
        try {
			System.getProperties().load(new FileInputStream(configFileName));
		} catch (IOException e) {
			String myCurrentDir = System.getProperty("user.dir") + File.separator + 
					System.getProperty("sun.java.command").substring(0, 
							System.getProperty("sun.java.command").lastIndexOf(".")) .replace(".", File.separator); 
			System.out.println(myCurrentDir);
			System.err.println("MISSING CONFIGURATION FILE. DB WILL NOT LOAD CORRECTLY.");
		}
	}
	
	public String getProperty(String key) {
		return System.getProperty(key);
	}
}
