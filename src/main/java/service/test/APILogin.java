package service.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class APILogin {

	public String login() throws IOException {

		String newUrl = "https://api.tier3.com/REST/Auth/Logon/";
		URL url = new URL(newUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");

		String jsonMsg = "{\'APIKey\':\'51f6f3ba63a04e34b2ee150ecc635028\',\'Password\':\'-#5A@H-[7sEp)]pq\'}";

		OutputStream os = conn.getOutputStream();
		os.write(jsonMsg.getBytes());
		Scanner scanner;
		String response;
		if (conn.getResponseCode() != 200) {
			scanner = new Scanner(conn.getErrorStream());
			response = "Error From Server \n\n";
		} else {
			scanner = new Scanner(conn.getInputStream());
			response = "Response From Server \n\n";
		}

		Map<String, List<String>> header = conn.getHeaderFields();

		List<String> setCookie = header.get("Set-Cookie");

		String authCookie = setCookie.get(0);
		System.out.println("authCookie -> " + authCookie);

		scanner.useDelimiter("\\Z");
		response = scanner.next();
		scanner.close();
		conn.disconnect();
		System.out.println(response);
		return authCookie;

	}

	public String getServers(String authCookie) throws IOException {
		String newUrl = "https://api.tier3.com/REST/Server/GetServers/";
		URL url = new URL(newUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Cookie", authCookie);

		OutputStream os = conn.getOutputStream();
		Scanner scanner;
		String response;
		if (conn.getResponseCode() != 200) {
			scanner = new Scanner(conn.getErrorStream());
			response = "Error From Server \n\n";
		} else {
			scanner = new Scanner(conn.getInputStream());
			// System.out.println("scanner output is: "+scanner);
			response = "Response From Server \n\n";
		}
		scanner.useDelimiter("\\Z");
		response = scanner.next();

		scanner.close();
		conn.disconnect();
		// System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Executing api service of control tier 3");
		APILogin log = new APILogin();
		int countActive = 0, countArchived = 0, countConstruct = 0;
		JSONObject json = null;
		String cookie = log.login();
		try {
			// json= new JSONObject(response);
			// json.get("Message");

			String resp = log.getServers(cookie);
			JSONObject servers = new JSONObject(resp);
			JSONArray serverData = servers.getJSONArray("Servers");
			
			JSONObject serversdataasa= servers.getJSONObject("Servers");
			System.out.println("count of  servers is : " + serverData.length());

			for (int i = 0; i < serverData.length(); i++) {
				JSONObject findServer = serverData.getJSONObject(i);
				// String status= findServer.getString("Status");
				if (findServer.getString("Status").equals("Active")) {
					countActive++;
				} else if (findServer.getString("Status").equals("Archived")) {
					countArchived++;
				} else {
					countConstruct++;
				}
			}

			System.out.println("Number of active servers are: " +countActive);
			System.out.println("Number of archived servers are: " +countArchived);
			System.out.println("Number of servers under construction are: " +countConstruct);
			// System.out.println("Response is: "+resp);
			// System.out.println("Message received from the json object is: "+json.get("Message"));System.out.println(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
