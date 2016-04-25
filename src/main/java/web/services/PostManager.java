package web.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import web.MODEL.Comment;
import web.MODEL.Post;
import web.MODEL.PostRequest;
import web.MODEL.PostResponse;

//, MediaType.APPLICATION_XML
@Path("/postService/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON})

public interface PostManager {
	
	@POST
	@Path("/addPost")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public PostResponse addPost(PostRequest postRequest);
	
	@GET
	@Path("/getPost/{permalink}")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Post getPost(@PathParam("permalink") String permalink);
	
	
	@POST
	@Path("/addComment")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public PostResponse addComment(Comment commentRequest);
	
	
	@GET
	@Path("/show")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public ArrayList<Post> showPost();
	
	
	@GET
	@Path("/postByTag/{tags}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Post> getPostByTag(@PathParam("tags") String tags);
	
	
	@GET
	@Path("/nextPost")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Post> getNextPost();
	
	
	@GET
	@Path("/prevPost")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Post> getPrevPost();
	
	
	@GET
	@Path("/likePost/{permalink}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void likePost(@PathParam("permalink") String permalink);
	
	
	@GET
	@Path("/getPostByAuthor/{authorName}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Post> getPostByAuthor(@PathParam("authorName") String authorName);
	
	
	@GET
	@Path("/getPostByTitle/{title}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Post> getPostByTitle(@PathParam("title") String title);
	
	

}
