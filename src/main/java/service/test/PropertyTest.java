package service.test;

import java.io.IOException;

import service.utils.ResponseUtils;

public class PropertyTest {
	public static void main(String[] args) {
		
		try {
			ResponseUtils utils= ResponseUtils.getInstance();
			System.out.println(utils.getValue("604"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
