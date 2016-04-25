package web.Business;

import java.net.UnknownHostException;
import java.util.ArrayList;

import web.DAO.PostDao;
import web.MODEL.Comment;
import web.MODEL.Post;
import web.MODEL.PostRequest;
import web.MODEL.PostResponse;

public class PostBusinessImpl implements PostBusiness {

	private PostDao postDao;

	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}

	public PostResponse addPost(PostRequest postRequest) {
		PostResponse response = null;
		try {
			response = postDao.addPost(postRequest);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public Post getPost(String permalink) {
		// String response= postDao.getPost(permalink);
		Post post = postDao.getPost(permalink);
		return post;
	}

	public PostResponse addComment(Comment commentRequest) {
		
		return postDao.addComment(commentRequest);
	}

	public ArrayList<Post> showPost() {
		ArrayList<Post> ob = postDao.showPost();
		return ob;
	}

	public ArrayList<Post> getPostByTag(String tags) {
		return postDao.getPostByTag(tags);
	}

	public ArrayList<Post> getNextPost() {
		return postDao.getNextPost();
	}

	public ArrayList<Post> getPrevPost() {
		return postDao.getPrevPost();
	}

	public void likePost(String permalink) {
		postDao.likePost(permalink);

	}

	public ArrayList<Post> getPostByAuthor(String authorName) {
		return postDao.getPostByAuthor(authorName);
	}

	public ArrayList<Post> getPostByTitle(String title) {
		return postDao.getPostByTitle(title);
	}

}
