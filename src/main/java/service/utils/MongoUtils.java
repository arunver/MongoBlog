package service.utils;

import com.mongodb.DBCollection;

public class MongoUtils {
	
	
	public String getUserCollection()
	{
		return "posts";
	}
	
	public DBCollection getPostCollection()
	{
		return null;
	}

}
