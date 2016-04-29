package web.DAO;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import web.MODEL.UserInfo;

public class AuthenticationService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		UserInfo user= userDao.getUser(username);
		GrantedAuthority authority= new SimpleGrantedAuthority(user.getRole());
		UserDetails userDetails= (UserDetails)new User(user.getUsername(),user.getPassword(),Arrays.asList(authority));
		//System.out.println(userDetails.getAuthorities());
		return userDetails;
	}

}
