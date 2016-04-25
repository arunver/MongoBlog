package service.test;

import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ParseJson {
	
	String reverseString(String obj) throws Exception
	{
		if(obj.length() == 0)
			throw new Exception();
		return null;
	}
	public static void main(String[] args) {
		 String s="[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
		 Object object = JSONValue.parse(s);
		 JSONArray obj= (JSONArray)object;
		 
		 System.out.println(obj.get(1));
		 
		 JSONObject obj2= (JSONObject)obj.get(1);
		 System.out.println(obj2.get("1"));
		 
		 JSONObject data = new JSONObject();
		 data.put("test", "This is test data");
		 
		 System.out.println(data);
		 ParseJson parseObject= new ParseJson();
		 try
		 {
			 String str="";
			 parseObject.reverseString(str);
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("String is empty");
		}
		 finally{
			 
		 }
		 
	}

}
