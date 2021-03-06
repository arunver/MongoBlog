package web.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import web.MODEL.UserInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserDaoImpl implements UserDao {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public UserInfo getUser(String username) {
		DBCollection collection = mongoTemplate.getCollection("User");
		BasicDBObject user = new BasicDBObject();
		user.append("username",username);
		
		DBObject userData = collection.findOne(user);
		
		if(userData!=null){
		
		UserInfo userAuth= new UserInfo();
		userAuth.setUsername((String)userData.get("username"));
		userAuth.setPassword((String)userData.get("password"));
		userAuth.setRole((String)userData.get("role"));
		return userAuth;
		}
		else
		{
			throw new UsernameNotFoundException("User not found");
			
		}
		
		//System.out.println(userAuth.getRole());
		
	}
	

}
