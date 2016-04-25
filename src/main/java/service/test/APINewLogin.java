package service.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APINewLogin {
	
	public String login() throws IOException
	{
		String key = "51f6f3ba63a04e34b2ee150ecc635028";
		String pw = "-#5A@H-[7sEp)]pq\'}";
		
		String newUrl="https://api.tier3.com/REST/Auth/Logon/";
		URL url = null;
		try {
			url = new URL(newUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		String payload = String.format("{\"APIKey\":\"{0}\", \"Password\":\"{1}}\"}", key, pw);
		
		return null;
	}
	public static void main(String[] args) {
		
	}

}
