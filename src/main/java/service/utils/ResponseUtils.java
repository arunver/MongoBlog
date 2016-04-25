package service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResponseUtils {
	 private static ResponseUtils instance = null;

	   private Properties props;

	   private ResponseUtils() throws IOException{
	         // Here you could read the file into props object
		   InputStream inputStream = getClass().getResourceAsStream("src/main/resources/error.properties");
	        props= new Properties();
	        props.load(inputStream);
	   }

	   public static synchronized ResponseUtils getInstance() throws IOException{
	       if (instance == null)
	           instance = new ResponseUtils();
	       return instance;
	   }

	   public String getValue(String propKey){
	       return this.props.getProperty(propKey);
	   }
	}
