package com.sportal.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sportal.model.Article;
import com.sportal.model.Comment;
import com.sportal.model.User;
import com.sportal.model.model_db.CommentDao;

@RestController
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value="/comment", method=RequestMethod.POST)
	@ResponseBody
	public void Comment(HttpServletRequest req, HttpServletResponse resp){
 		//check if user is logged in
		
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);
			return;
		}
		//collect data from request
		long userId = user.getId();
		Article article = (Article) req.getSession().getAttribute("article");
		long articleId = article.getId();
		//req.setCharacterEncoding("UTF-8");
		String content = req.getParameter("comment");
		
		//String result = URLDecoder.decode(content, "UTF-8");
		
		Comment comment = new Comment(userId, articleId, content, 0, 0, LocalDateTime.now(), true, new HashSet<>());
	
		try {
			//insert new comment related to the article where the user is
			comment = commentDao.addComment(comment);
		} catch (SQLException e) {
			System.out.println("commentService"+e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		//refresh article comments and forward to the article URL
		String responseJsonString = "{\"username\": \""+user.getUsername()+"\", \"userId\": "+userId+", \"commentTime\": \""+comment.getTimeCreated().toString()+"\"}";
		resp.setContentType("application/json");
		try {
			resp.getWriter().write(responseJsonString);
		} catch (IOException e) {
			System.out.println("postarticle"+e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}finally{
			try {
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (IOException e) {
				System.out.println("postarticle"+e.getMessage());
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				
			}
		}
		
		resp.setStatus(200);
		
	}
	
}
