package foo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Hello world!
 *
 */
public class App 
{
	private MongoTemplate mongo;
	
	App()
	{
		
	}
	
	App(MongoTemplate mongo)
	{
		this.mongo = mongo;
	}
	
	public void showMongoTemplate()
	{
		System.out.println(mongo.getCollection("posts").find().count());
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        App app= new App();
        app.showMongoTemplate();
        
    }
}
