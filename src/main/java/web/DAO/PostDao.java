package web.DAO;

import java.net.UnknownHostException;
import java.util.ArrayList;

import web.MODEL.Comment;
import web.MODEL.Post;
import web.MODEL.PostRequest;
import web.MODEL.PostResponse;

public interface PostDao {
	
	public PostResponse	 addPost(PostRequest postRequest) throws UnknownHostException;
	
	public Post getPost(String permalink);
	
	public PostResponse addComment(Comment commentRequest);
	
	public ArrayList<Post> showPost();
	
	public ArrayList<Post> getPostByTag(String tags);
	
	public ArrayList<Post> getNextPost();
	
	public ArrayList<Post> getPrevPost();
	
	public void likePost(String permalink);
	
	public ArrayList<Post> getPostByAuthor(String authorName);
	
	public ArrayList<Post> getPostByTitle(String title);

}
