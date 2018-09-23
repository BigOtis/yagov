package yagov.rest.bills;

import java.util.List;

import org.bson.Document;

import com.yagov.db.MongoFacade;

import yagov.rest.mongo.MongoSingleton;

/**
 * Bill - returns the requested Bill Metadata
 *
 */
public class Bill {
	
	private final MongoFacade mongo = MongoSingleton.getMongo();
	private final String bill_id;
	private final List<String> subjects;
	
    @SuppressWarnings("unchecked")
	public Bill(Integer congress, String billName) {
    	
    	Document billDoc = mongo.getBillDoc(congress, billName);
    	this.bill_id = billDoc.getString("bill_id");
    	this.subjects = (List<String>) billDoc.get("subjects");    	
    }
    
    public String getBill_id() {
    	return bill_id;
    }
    
    public List<String> getSubjects(){
    	return subjects;
    }
    
    
}
