package yagov.rest.bills;

import java.util.List;

import org.bson.Document;

import com.yagov.db.MongoFacade;

import yagov.rest.mongo.MongoSingleton;

/**
 * Hello world! from a little d0g named bust3r
 *
 */
public class Bill {
	
	private final MongoFacade mongo = MongoSingleton.getMongo();
	private final String bill_id;
	private final List<String> subjects;
	
    public Bill(Integer congress, String billName) {
    	
    	Document billDoc = mongo.getBillDoc(congress, billName);
    	
    	this.bill_id = billDoc.getString("bill_id");
    	this.subjects = (List<String>) billDoc.get("subjects");
    	System.out.println(subjects);
    	
    }
    
    public String getBill_id() {
    	return bill_id;
    }
    
    public List<String> getSubjects(){
    	return subjects;
    }
    
    
}
