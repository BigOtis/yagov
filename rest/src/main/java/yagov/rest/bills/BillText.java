package yagov.rest.bills;

import com.yagov.db.MongoFacade;

import yagov.rest.mongo.MongoSingleton;

/**
 * BillText - returns the requested Bill Text
 *
 */
public class BillText {
	
	private final MongoFacade mongo = MongoSingleton.getMongo();
	private final String bill_id;
	private final String fullText;
	
    @SuppressWarnings("unchecked")
	public BillText(Integer congress, String billName) {
    	
    	this.fullText = mongo.getBillText(congress, billName);
    	this.bill_id = mongo.getBillID(congress, billName);
    }
    
    public String getBill_id() {
    	return bill_id;
    }
    
    public String getFullText(){
    	return fullText;
    }
    
    
}
