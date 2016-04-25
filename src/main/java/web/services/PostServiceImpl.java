package web.services;

import java.util.ArrayList;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import web.Business.PostBusiness;
import web.MODEL.Comment;
import web.MODEL.Post;
import web.MODEL.PostRequest;
import web.MODEL.PostResponse;

public class PostServiceImpl implements PostManager {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	
	private PostBusiness postBusiness;
	
	public void setPostBusiness(PostBusiness postBusiness)
	{
		this.postBusiness= postBusiness;
	}

	public PostResponse addPost(PostRequest postRequest) {
		return postBusiness.addPost(postRequest);
	}

	public Post getPost(String permalink) {
		
		return postBusiness.getPost(permalink);
	}

	public PostResponse addComment(Comment commentRequest) {
		
		return postBusiness.addComment(commentRequest);
	}

	public ArrayList<Post> showPost() {
		
		 return postBusiness.showPost();
	}

	public ArrayList<Post> getPostByTag(String tags) {
	
		return postBusiness.getPostByTag(tags);
	}

	public ArrayList<Post> getNextPost() {
		return postBusiness.getNextPost();
	}
	
	public ArrayList<Post> getPrevPost(){
		return postBusiness.getPrevPost();
	}
	
	public void likePost(String permalink) {
		postBusiness.likePost(permalink);
		
	}
	
	public ArrayList<Post> getPostByAuthor(String authorName)
	{
		return postBusiness.getPostByAuthor(authorName);
	}
	
	public ArrayList<Post> getPostByTitle(String title)
	{
		return postBusiness.getPostByTitle(title);
		
	}

}
