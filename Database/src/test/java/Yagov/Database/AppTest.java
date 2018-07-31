package Yagov.Database;

import com.yagov.db.MongoFacade;
import com.yagov.util.Config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
		Config config = new Config("yagov.properties");
    	MongoFacade mongo = new MongoFacade(config.getProperty("dbName"));
    	System.out.println(mongo.getBillText(114, "hr10"));
        assertTrue( true );
    }
}
