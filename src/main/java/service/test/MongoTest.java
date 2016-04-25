package service.test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import web.MODEL.Post;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;

public class MongoTest {
	public static void main(String[] args) {
		
		/*MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.writeConcern(WriteConcern.JOURNAL_SAFE);
		
		MongoClient client = null;
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db= client.getDB("blog");
		DBCollection collection= db.getCollection("posts");
		
		BasicDBObject post= new BasicDBObject();
		post.append("permalink", "cxzdzjkztkqraoqlgcru");
		
		DBCursor cursor= collection.find(post);
		Post postObj= new Post();
		
		while(cursor.hasNext())
		{
			DBObject tobj = cursor.next();
			System.out.println((String)tobj.get("title"));
			postObj.setAuthor((String)tobj.get("author"));
			postObj.setTitle((String)tobj.get("title"));
			postObj.setBody((String)tobj.get("body"));
			
			BasicDBList tag= (BasicDBList) tobj.get("tags");
			ArrayList<String> tags= new ArrayList<String>();
			
			Iterator it= tag.iterator();
			
			while(it.hasNext())
			{
				tags.add((String) it.next());
			}
			
		//	postObj.setTags(tags);
			
			
			
			
			
		}
		
		
		System.out.println(postObj);*/
		String newUrl="http://localhost:8080/MyService-1/rs/postService/getPost";
		String permalink="bjdgkubdbesvrgkvvelt";
		newUrl= newUrl + "/{"+permalink+"}";
		
		System.out.println("newUrl is: "+newUrl);
	}

}
