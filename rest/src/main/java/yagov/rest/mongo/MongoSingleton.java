package yagov.rest.mongo;

import com.yagov.db.MongoFacade;

public class MongoSingleton {
	
	private static final MongoFacade mongo = new MongoFacade("Congress");
	
	public static MongoFacade getMongo() {
		return mongo;
	}
	
}
