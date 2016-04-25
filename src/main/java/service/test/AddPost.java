package service.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AddPost {
	
	public String addPost() throws IOException
	{
		String newUrl = "http://localhost:8080/MyService-1/rs/postService/addPost";
		URL url = new URL(newUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		Date date = new Date();
		SimpleDateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String formattedDate = tsdf.format(date);
		
		
		String input = "{\"apiKey\" :\"arun\",\"password\" :\"admin\" , \"post\" :{\"body\": \"This is a dummy blog post. \",\"permalink\": \"cxzdzjkztkqraoqlgcru\",\"author\": \"Arun\",\"title\": \"Nice Post\"}}";;

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		Scanner scanner;
		String response;
		if (conn.getResponseCode() != 200) {
			scanner = new Scanner(conn.getErrorStream());
			response = "Error From Server \n\n";
		} else {
			scanner = new Scanner(conn.getInputStream());
			response = "Response From Server \n\n";
		}

		scanner.useDelimiter("\\Z");
		response = scanner.next();
		scanner.close();
		conn.disconnect();
		//System.out.println(response);
		return response;
	}
	
	
	public static void main(String[] args) throws IOException {
		AddPost obj= new AddPost();
		System.out.println(obj.addPost());
	}

}
