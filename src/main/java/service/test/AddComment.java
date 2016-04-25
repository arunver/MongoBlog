package service.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AddComment {
	
	public String addComment() throws IOException
	{
		String newUrl = "http://localhost:8080/MyService-1/rs/postService/addComment";
		URL url = new URL(newUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		String input = "{\"body\": \"This is a dummy post comment. \",\"permalink\": \"nice_post\",\"author\": \"Simer\",\"email\": \"sahni.simer@gmail.com\"}";

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		
		Scanner scanner1;
		String response;
		
		System.out.println("Response code is: "+conn.getResponseCode());
		
		if (conn.getResponseCode() != 200) {
			scanner1 = new Scanner(conn.getErrorStream());
			response = "Error From Server \n\n";
		} else {
			scanner1 = new Scanner(conn.getInputStream());
			
			response =  scanner1.next();
			
		}

		scanner1.useDelimiter("\\Z");
		response = scanner1.next();
		scanner1.close();
		conn.disconnect();
		System.out.println(response);
		return response;
	}
	
	public static void main(String[] args) throws IOException {
		AddComment obj= new AddComment();
		System.out.println(obj.addComment());
	}

}
