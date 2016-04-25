package web.MODEL;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="postRequest")
public class PostRequest {
	Post post;
	String apiKey;
	String password;
	
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
