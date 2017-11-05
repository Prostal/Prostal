package com.sportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
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

	// Spring form used
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute User user, HttpServletRequest request) {
		// check for register credentials
		String username = user.getUsername();
		String mail = user.getEmail();

		// for testers only
		// if(username.trim().isEmpty()){
		// return "redirect:https://www.space.com/";
		// }

		if (!validUsername(username) || !validPassword(user.getPassword()) || !isValidEmailAddress(mail)) {
			request.setAttribute("error", "form does not meet our requirements");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "registerPage";
		}

		try {
			if (!userDao.existsUserMail(user.getUsername(), user.getEmail())) {
				// insert user in db
				userDao.insertUser(user);
				// update session
				request.getSession().setAttribute("user", user);
				// forward to main html
				return "index";
			} else {
				request.setAttribute("error", "username or e-mail has already been used");
				ResponseEntity.status(HttpStatus.FORBIDDEN);
				return "registerPage";

			}
		} catch (SQLException e1) {
			System.out.println("register Error" + e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
	}

	@RequestMapping(value = "/userPage", method = RequestMethod.GET)
	public String getUser(HttpServletRequest request, HttpServletResponse response) {

		// get user from session
		User user = (User) request.getSession().getAttribute("user");
		// update user

		try {

			if (user != null) {
				String username = user.getUsername();
				user = userDao.getUser(username);
				request.getSession().setAttribute("user", user);
				return "user";
			}else{
				System.out.println("logout");
				return "loginPage";
			}

		} catch (SQLException e) {
			request.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}

	}

	@RequestMapping(value = "/avatarUpload", method = RequestMethod.POST)
	public String avatarUpload(@RequestParam("avatar") MultipartFile file, HttpServletRequest req,
			HttpServletResponse resp) {

		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			req.setAttribute("error", "Log in first ");
			ResponseEntity.status(HttpStatus.UNAUTHORIZED);
			return "index";
		}

		String supportedType = "image/jpeg";
		String original = file.getOriginalFilename();
		Tika tika = new Tika();
		try {
			String filetype = tika.detect(file.getBytes());

			if (!filetype.equals(supportedType)) {
				req.setAttribute("error", "this file format is not supported");
				ResponseEntity.status(HttpStatus.FORBIDDEN);
				return "user";
			}
		} catch (IOException e) {
			req.setAttribute("error", "IO problem" + e.getCause().getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "user";
		}
		String extension = FilenameUtils.getExtension(original);

		String avatar = user.getId() + "user".concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + avatar);
		try {
			file.transferTo(f);
		} catch (IllegalStateException | IOException e1) {
			req.setAttribute("error", "IO problem : " + e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}
		// UPDATE IN DB
		try {
			userDao.updateAvatar(avatar, user.getUsername());
			user.setAvatarUrl(avatar);
		} catch (SQLException e) {
			req.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}

		return "index";
	}

	@RequestMapping(value = "/showAvatar/{userId}", method = RequestMethod.GET)
	public void showMedia(@PathVariable("userId") Long userId, HttpServletRequest request,
			HttpServletResponse response) {

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
			return;
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				return;
			}
		}

	}

	private boolean validPassword(String password) {

		return password != null && !password.trim().isEmpty() && password.length() < 201;
	}

	private boolean validUsername(String username) {
		return username != null && !username.trim().isEmpty() && username.length() < 46;
	}

	private boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		email.matches(ePattern);
		return m.matches();
	}

}
