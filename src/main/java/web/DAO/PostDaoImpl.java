package web.DAO;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import web.MODEL.Comment;
import web.MODEL.Post;
import web.MODEL.PostRequest;
import web.MODEL.PostResponse;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

/**
 * PostDaoImpl class Interacts with the Mongo Database for various CRUD
 * operations
 * 
 * @author arun.verma
 *
 */
@Controller
public class PostDaoImpl implements PostDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static int nextPostCount = 0;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private final static Logger LOGGER = Logger.getLogger(PostDaoImpl.class.getName());

	/**
	 * addPost Method to add a post to the Mongo database
	 * 
	 * @param postRequest
	 * @return response
	 * 
	 */
	public PostResponse addPost(PostRequest postObj) throws UnknownHostException {
		/**
		 * wait for an acknowledgement from the primary server If no exception
		 * is raised, then response is "OK"
		 */

		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.writeConcern(WriteConcern.JOURNAL_SAFE);

		DBCollection collection = mongoTemplate.getCollection("posts");

		Post postRequest = postObj.getPost();

		PostResponse response = new PostResponse();
		if (postObj.getApiKey().equals("admin") && postObj.getPassword().equals("arun")) {

			String title = postRequest.getTitle();

			System.out.println("Title is: " + title);

			String permalink = title.replaceAll("\\s", "_"); // whitespace
			// becomes _
			permalink = permalink.replaceAll("\\W", "");     // get rid of non
			// alphanumeric
			permalink = permalink.toLowerCase();

			BasicDBObject userPost = new BasicDBObject();
			userPost.append("title", title);
			userPost.append("body", postRequest.getBody());
			userPost.append("author", postRequest.getAuthor());
			userPost.append("permalink", permalink);
			userPost.append("tags", postRequest.getTags());
			userPost.append("comments", new ArrayList<Comment>());
			userPost.append("date", new java.util.Date());

			try {
				collection.insert(userPost);
				response.setResponseCode(601);
				response.setMessage("Post with permalink " + permalink + " inserted");
				LOGGER.info(response);
			} catch (MongoException e) {
				response.setResponseCode(602);
				response.setMessage("Error in inserting post with permalink " + permalink);
				LOGGER.debug(response);
				LOGGER.error(e);
			}
			return response;
		} else {
			response.setResponseCode(403);
			response.setMessage("Unauthorized access");
			return response;
		}
	}

	/**
	 * getPost Method to get a particular post based on permalink
	 * 
	 * @param permalink
	 * @return postObj
	 */
	public Post getPost(String permalink) {

		Post postObj = new Post();
		try {
			DBCollection collection = mongoTemplate.getCollection("posts");

			BasicDBObject post = new BasicDBObject();
			post.append("permalink", permalink);

			DBObject resultPost = collection.findOne(post);

			postObj.setAuthor((String) resultPost.get("author"));
			postObj.setBody((String) resultPost.get("body"));
			postObj.setPermalink((String) resultPost.get("permalink"));
			postObj.setTitle((String) resultPost.get("title"));

			BasicDBList tag = (BasicDBList) resultPost.get("tags");
			int size = tag.size();
			String[] tags = new String[size];

			Iterator it = tag.iterator();
			int count = 0;
			while (it.hasNext()) {

				tags[count] = (String) it.next();
				count++;
			}
			postObj.setTags(tags);

			BasicDBList comments = (BasicDBList) resultPost.get("comments");
			List<Comment> comment = new ArrayList<Comment>();

			Iterator commIt = comments.iterator();
			while (commIt.hasNext()) {
				Comment commentObj = new Comment();
				BasicDBObject comm = (BasicDBObject) commIt.next();

				commentObj.setBody(comm.getString("body"));
				commentObj.setAuthor(comm.getString("author"));
				commentObj.setEmail(comm.getString("email"));
				commentObj.setDate(new java.util.Date());

				comment.add(commentObj);
			}

			postObj.setComments(comment);
			LOGGER.info("Post with permalink " + permalink + " successfully fetched");
			return postObj;
		} catch (MongoException e) {

			LOGGER.error("Error in fetching post with permalink :" + permalink);
		}
		return postObj;
	}

	/**
	 * addComment Method to add a comment to the respective post in Mongo
	 * database
	 * 
	 * @param commentRequest
	 * @return response
	 */
	public PostResponse addComment(Comment commentRequest) {
		String response = "";
		PostResponse postResponse= new PostResponse();
		try {

			DBCollection collection = mongoTemplate.getCollection("posts");

			BasicDBObject comment = new BasicDBObject();
			comment.put("body", commentRequest.getBody());
			comment.put("author", commentRequest.getAuthor());
			comment.put("email", commentRequest.getEmail());
			comment.put("date", new java.util.Date());

			BasicDBObject permalink = new BasicDBObject();
			permalink.put("permalink", commentRequest.getPermalink());

			BasicDBObject updateComment = new BasicDBObject();
			updateComment.put("$push", new BasicDBObject("comments", comment));
			collection.update(new BasicDBObject().append("permalink", "nice_post"), updateComment);

			postResponse.setResponseCode(603);
			postResponse.setMessage("Comment successfully added");

			return postResponse;
		} catch (MongoException e) {
			LOGGER.error("Failed to add the comment to the post collection");
			LOGGER.error(e);
			postResponse.setResponseCode(604);
			postResponse.setMessage("Failed to post the comment");

		}

		return postResponse;
	}

	/**
	 * showPost Method to fetch all the posts from the MongoDB
	 * 
	 * @param
	 * @return postList;
	 */
	public ArrayList<Post> showPost() {
		DBCollection collection = mongoTemplate.getCollection("posts");
		try {
			DBCursor cursor = collection.find().sort(new BasicDBObject("date", 1)).limit(10);

			ArrayList<Post> postList = new ArrayList<Post>();
			while (cursor.hasNext()) {
				DBObject tobj = cursor.next();
				Post postObj = new Post();

				postObj.setAuthor((String) tobj.get("author"));
				postObj.setTitle((String) tobj.get("title"));
				postObj.setBody((String) tobj.get("body"));
				postObj.setPermalink((String) tobj.get("permalink"));
				postObj.setDate((Date) tobj.get("date"));

				BasicDBList tag = (BasicDBList) tobj.get("tags");
				int size = tag.size();
				List<Comment> comment = new ArrayList<Comment>();

				BasicDBList comments = (BasicDBList) tobj.get("comments");

				String[] tags = new String[size];

				Iterator commIt = comments.iterator();
				Iterator it = tag.iterator();
				int countTag = 0;
				while (it.hasNext()) {

					tags[countTag] = (String) it.next();
					countTag++;
				}

				while (commIt.hasNext()) {
					Comment commentObj = new Comment();
					BasicDBObject comm = (BasicDBObject) commIt.next();
					commentObj.setBody(comm.getString("body"));
					commentObj.setAuthor(comm.getString("author"));
					commentObj.setEmail(comm.getString("email"));

					comment.add(commentObj);
				}
				postObj.setTags(tags);
				postObj.setComments(comment);
				postList.add(postObj);

			}
			return postList;
		} catch (MongoException e) {
			LOGGER.error("Unable to fetch all the records from the blog database");
			LOGGER.error(e);
		}

		return null;

	}

	/**
	 * getPostByTag Method to fetch posts from the blog database based on the
	 * tag searched for
	 * 
	 * @param tags
	 * @return postList
	 */
	public ArrayList<Post> getPostByTag(String tags) {
		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			BasicDBList tagsToSearch = new BasicDBList();
			tagsToSearch.add(tags);

			BasicDBObject tagSearch = new BasicDBObject("$in", tagsToSearch);
			BasicDBObject query = new BasicDBObject("tags", tagSearch);

			DBCursor cursor = collection.find(query);

			ArrayList<Post> postList = new ArrayList<Post>();
			int count = 0;
			while (cursor.hasNext()) {
				DBObject tobj = cursor.next();
				Post postObj = new Post();

				postObj.setAuthor((String) tobj.get("author"));
				postObj.setTitle((String) tobj.get("title"));
				postObj.setBody((String) tobj.get("body"));
				postObj.setPermalink((String) tobj.get("permalink"));
				postObj.setDate((Date) tobj.get("date"));

				BasicDBList tag = (BasicDBList) tobj.get("tags");
				int size = tag.size();
				List<Comment> comment = new ArrayList<Comment>();

				BasicDBList comments = (BasicDBList) tobj.get("comments");

				String[] tagss = new String[size];

				Iterator commIt = comments.iterator();
				Iterator it = tag.iterator();
				int countTag = 0;
				while (it.hasNext()) {

					tagss[countTag] = (String) it.next();
					countTag++;
				}

				while (commIt.hasNext()) {
					Comment commentObj = new Comment();
					BasicDBObject comm = (BasicDBObject) commIt.next();
					commentObj.setBody(comm.getString("body"));
					commentObj.setAuthor(comm.getString("author"));
					commentObj.setEmail(comm.getString("email"));

					comment.add(commentObj);
				}
				postObj.setTags(tagss);
				postObj.setComments(comment);
				postList.add(postObj);
				count++;

			}

			return postList;

		} catch (MongoException e) {
			LOGGER.error("Unable to fetch the posts with the tag: " + tags);
			LOGGER.error(e);
		}

		return null;
	}

	/**
	 * getNextPost Method to fetch the next set of posts from the blog database
	 * 
	 * @return nextPostList;
	 */
	public ArrayList<Post> getNextPost() {
		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			int postCount = collection.find().count();
			if (postCount != 0 && nextPostCount < (postCount / 10)) {
				nextPostCount++;
				System.out.println("Post Count is: " + nextPostCount);
			} else {
				return new ArrayList<Post>();
			}

			DBCursor cursor = collection.find().sort(new BasicDBObject("date", 1)).limit(10).skip(10 * nextPostCount);

			ArrayList<Post> nextPostList = new ArrayList<Post>();
			if (postCount < (10 * nextPostCount)) {
				return new ArrayList<Post>();
			} else {
				while (cursor.hasNext()) {
					DBObject tobj = cursor.next();
					Post postObj = new Post();

					postObj.setAuthor((String) tobj.get("author"));
					postObj.setTitle((String) tobj.get("title"));
					postObj.setBody((String) tobj.get("body"));
					postObj.setPermalink((String) tobj.get("permalink"));
					postObj.setDate((Date) tobj.get("date"));

					String[] tagss = null;
					BasicDBList tag = (BasicDBList) tobj.get("tags");
					if(tag!=null){
						int size = tag.size();
						 tagss= new String[size];
					}
					List<Comment> comment = new ArrayList<Comment>();

					BasicDBList comments = (BasicDBList) tobj.get("comments");

					
					
					Iterator it=null;
					Iterator commIt = comments.iterator();
					if(tag!=null){
						it = tag.iterator();
					}
					int countTag = 0;
					
					if(tagss!=null){
						while (it.hasNext()) {
	
							tagss[countTag] = (String) it.next();
							countTag++;
						}
					}

					while (commIt.hasNext()) {
						Comment commentObj = new Comment();
						BasicDBObject comm = (BasicDBObject) commIt.next();
						commentObj.setBody(comm.getString("body"));
						commentObj.setAuthor(comm.getString("author"));
						commentObj.setEmail(comm.getString("email"));
						/*commentObj.setDate(comm.getDate("date"));
						commentObj.setPermalink(comm.getString("permalink"));*/

						comment.add(commentObj);
					}
					
					if(tagss!=null){
						postObj.setTags(tagss);
					}

					postObj.setComments(comment);
					nextPostList.add(postObj);

				}
				return nextPostList;
			}
		} catch (MongoException e) {
			LOGGER.error("Unable to fetch the next set of posts from the blog database");
			LOGGER.error(e);
		}
		return null;
	}

	/**
	 * getPrevPost Method to fetch the previous set of posts from the blog
	 * database
	 * 
	 * @return nextPostList
	 */
	public ArrayList<Post> getPrevPost() {
		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			int postCount = collection.find().count();
			
			System.out.println("Number of posts for the blog are: "+postCount);
			if (postCount != 0 && nextPostCount > 0) {
				nextPostCount--;
			} else {
				return showPost();
			}

			DBCursor cursor = collection.find().sort(new BasicDBObject("date", 1)).limit(10).skip(10 * nextPostCount);

			ArrayList<Post> nextPostList = new ArrayList<Post>();
			if (postCount < (10 * nextPostCount)) {
				return new ArrayList<Post>();
			} else {
				while (cursor.hasNext()) {
					DBObject tobj = cursor.next();
					Post postObj = new Post();

					postObj.setAuthor((String) tobj.get("author"));
					postObj.setTitle((String) tobj.get("title"));
					postObj.setBody((String) tobj.get("body"));
					postObj.setPermalink((String) tobj.get("permalink"));
					postObj.setDate((Date) tobj.get("date"));

					BasicDBList tag = (BasicDBList) tobj.get("tags");
					int size = tag.size();
					List<Comment> comment = new ArrayList<Comment>();

					BasicDBList comments = (BasicDBList) tobj.get("comments");

					String[] tagss = new String[size];

					Iterator commIt = comments.iterator();
					Iterator it = tag.iterator();
					int countTag = 0;
					while (it.hasNext()) {

						tagss[countTag] = (String) it.next();
						countTag++;
					}

					while (commIt.hasNext()) {
						Comment commentObj = new Comment();
						BasicDBObject comm = (BasicDBObject) commIt.next();
						commentObj.setBody(comm.getString("body"));
						commentObj.setAuthor(comm.getString("author"));
						commentObj.setEmail(comm.getString("email"));

						comment.add(commentObj);
					}

					postObj.setTags(tagss);

					postObj.setComments(comment);
					nextPostList.add(postObj);

				}
				return nextPostList;
			}
		} catch (MongoException e) {
			LOGGER.error("Unable to fetch the previous set of records from the blog database");
			LOGGER.error(e);
		}
		return null;
	}

	/**
	 * likePost Method to like a post based on permalink
	 * 
	 */
	public void likePost(String permalink) {

		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			BasicDBObject incLike = new BasicDBObject().append("$inc", new BasicDBObject().append("like", 1));

			collection.update(new BasicDBObject().append("permalink", permalink), incLike);
			LOGGER.info("Post with permalink " + permalink + " liked successfully.");
		} catch (MongoException e) {
			LOGGER.error("Unable to update the like count of the post with permalink " + permalink);
			LOGGER.error(e);
		}

	}

	/**
	 * getPostByAuthor Method to fetch a posts based on author name
	 * 
	 * @param authorName
	 * @return authorPost
	 */
	public ArrayList<Post> getPostByAuthor(String authorName) {
		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			BasicDBObject author = new BasicDBObject("author", authorName);
			DBCursor cursor = collection.find(author);

			ArrayList<Post> authorPost = new ArrayList<Post>();
			int count = 0;

			while (cursor.hasNext()) {
				DBObject tobj = cursor.next();
				Post postObj = new Post();

				postObj.setAuthor((String) tobj.get("author"));
				postObj.setTitle((String) tobj.get("title"));
				postObj.setBody((String) tobj.get("body"));
				postObj.setPermalink((String) tobj.get("permalink"));
				postObj.setDate((Date) tobj.get("date"));

				BasicDBList tag = (BasicDBList) tobj.get("tags");
				int size = tag.size();
				List<Comment> comment = new ArrayList<Comment>();

				BasicDBList comments = (BasicDBList) tobj.get("comments");

				String[] tagss = new String[size];

				Iterator commIt = comments.iterator();
				Iterator it = tag.iterator();
				int countTag = 0;
				while (it.hasNext()) {

					tagss[countTag] = (String) it.next();
					countTag++;
				}

				while (commIt.hasNext()) {
					Comment commentObj = new Comment();
					BasicDBObject comm = (BasicDBObject) commIt.next();
					commentObj.setBody(comm.getString("body"));
					commentObj.setAuthor(comm.getString("author"));
					commentObj.setEmail(comm.getString("email"));

					comment.add(commentObj);
				}

				postObj.setTags(tagss);

				postObj.setComments(comment);
				authorPost.add(postObj);
				count++;

			}

			LOGGER.info("Post with author " + authorName + " fetched successfully");
			return authorPost;
		} catch (MongoException e) {
			LOGGER.error("Failed to fetch the post by authorName");
			LOGGER.error(e);
		}

		return null;
	}

	/**
	 * getPostByTitle Method to fetch posts based on title
	 * 
	 * @param title
	 * @return blogPost
	 */
	public ArrayList<Post> getPostByTitle(String title) {
		DBCollection collection = mongoTemplate.getCollection("posts");

		try {
			BasicDBObject blogTitle = new BasicDBObject("title", title);
			DBCursor cursor = collection.find(blogTitle);

			ArrayList<Post> blogPost = new ArrayList<Post>();
			int count = 0;

			while (cursor.hasNext()) {
				DBObject tobj = cursor.next();
				Post postObj = new Post();

				postObj.setAuthor((String) tobj.get("author"));
				postObj.setTitle((String) tobj.get("title"));
				postObj.setBody((String) tobj.get("body"));
				postObj.setPermalink((String) tobj.get("permalink"));
				postObj.setDate((Date) tobj.get("date"));

				BasicDBList tag = (BasicDBList) tobj.get("tags");
				int size = tag.size();
				List<Comment> comment = new ArrayList<Comment>();

				BasicDBList comments = (BasicDBList) tobj.get("comments");

				String[] tagss = new String[size];

				Iterator commIt = comments.iterator();
				Iterator it = tag.iterator();
				int countTag = 0;
				while (it.hasNext()) {

					tagss[countTag] = (String) it.next();
					countTag++;
				}

				while (commIt.hasNext()) {
					Comment commentObj = new Comment();
					BasicDBObject comm = (BasicDBObject) commIt.next();
					commentObj.setBody(comm.getString("body"));
					commentObj.setAuthor(comm.getString("author"));
					commentObj.setEmail(comm.getString("email"));

					comment.add(commentObj);
				}

				postObj.setTags(tagss);

				postObj.setComments(comment);
				blogPost.add(postObj);
				count++;

			}
			LOGGER.info("Post with title " + title + " successfully fetched");
			return blogPost;
		} catch (MongoException e) {
			LOGGER.error("Failed to fetch the post based on title");
			LOGGER.error(e);
		}

		return null;

	}
	

}
