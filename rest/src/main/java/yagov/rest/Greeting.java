package yagov.rest;

/**
 * Hello world! from a little d0g named bust3r
 *
 */
public class Greeting 
{
	private final long id;
	private final String content;
	
    public Greeting(long id, String content) {
    	
        this.id = id;
        this.content = content;
    }
    
    public long getId() {
    	
    	return id;
    }

    public String getContent() {
    	
        return content;
    }
}
