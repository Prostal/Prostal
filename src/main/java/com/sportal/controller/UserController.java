package com.sportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sportal.WebInitializer;
import com.sportal.model.User;
import com.sportal.model.model_db.UserDao;

@Controller
@MultipartConfig
public class UserController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// check for login credentials
		// TODO SPRING Validation
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// check if user exists in db
		try {
			if (userDao.existsUser(username, password)) {

				User user = userDao.getUser(username);
				// update session
				request.getSession().setAttribute("user", user);
				return "index";
			} else {
				// forgot pass?
				request.setAttribute("error", "user does not exist");
				return "loginPage";
			}
		} catch (SQLException e) {
			request.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		

	}

	@RequestMapping(value = "/Logout", method = RequestMethod.POST)
	public String logOut(HttpServletRequest request) {
		// check if user is logged
		if (request.getSession().getAttribute("user") != null) {
			// update session
			request.getSession().invalidate();
			// redirect to main page OR PREVIOUS??

		}
		return "index";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute User user, HttpServletRequest request) {
		// check for register credentials

		// TODO STRONGER PASS
		
		try {
			if (!userDao.existsUser(user.getUsername(), user.getPassword())) {
				// insert user in db
				userDao.insertUser(user);
				// update session
				request.getSession().setAttribute("user", user);
				// forward to main html

				return "index";
			}
		} catch (SQLException e1) {
			System.out.println("ne stavash za nashata basa" + e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}

		return "index";

	}

	@RequestMapping(value = "/userPage", method = RequestMethod.GET)
	public String getUser(HttpServletRequest request, HttpServletResponse response) {

		// get user from session
		User user = (User) request.getSession().getAttribute("user");
		// update user
		
		try {

			if (user!=null) {
				String username = user.getUsername();
				user = userDao.getUser(username);
				request.getSession().setAttribute("user", user);
				return "user";
			}

		} catch (SQLException e) {
			request.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		
		return "index";
	}

	@RequestMapping(value = "/avatarUpload", method = RequestMethod.POST)
	public String avatarUpload(@RequestParam("avatar") MultipartFile file, HttpServletRequest req, HttpServletResponse resp){

		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			return "index";
		}
		String username = user.getUsername();
		
		String original = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(original);//nice, a
		String avatar = username.concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + avatar);
		try {
			file.transferTo(f);
		} catch (IllegalStateException | IOException e1) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		// UPDATE IN DB
		try {
			userDao.updateAvatar(avatar, user.getUsername());
			user.setAvatarUrl(avatar);
		} catch (SQLException e) {
			req.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}

		return "index";
	}

	@RequestMapping(value = "/getAvatar", method = RequestMethod.GET)
	public String displayAvatar(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String avatar = user.getAvatarUrl();

		if (avatar == null) {
			avatar = "default.jpg";
		}
		File f = new File(WebInitializer.LOCATION + File.separator + avatar);

		
		try (ServletOutputStream out = response.getOutputStream()){
			
			Files.copy(f.toPath(), out);
			out.flush();
			out.close();
		} catch (IOException e) {
			request.setAttribute("error", "input problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		
		return "user";
	}
	
	@RequestMapping(value = "/showAvatar/{userId}", method = RequestMethod.GET)
	public void showMedia(@PathVariable("userId") Long userId, HttpServletRequest request, HttpServletResponse response){
		

		User user = null;
		try {
			user = userDao.getUserById(userId);
		} catch (SQLException e1) {
			request.setAttribute("error", "database problem : " + e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return;
		}
		
		String avatar = user.getAvatarUrl();
		if (avatar == null) {
			avatar = "default.jpg";
		}
		File f = new File(WebInitializer.LOCATION + File.separator + avatar);

		try {
			
			Files.copy(f.toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			request.setAttribute("error", "input problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return ;
		}finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				return ;
			}
		}
		
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		email.matches(ePattern);
		return m.matches();
	}
	
	

}
