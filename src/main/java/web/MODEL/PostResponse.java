package web.MODEL;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class PostResponse {
	int responseCode;
	String message;
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
