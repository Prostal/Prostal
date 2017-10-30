package com.sportal.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void saveComment(HttpServletRequest req, HttpServletResponse resp){
 		//check if user is logged in
		//collect data from request
		User user = (User) req.getSession().getAttribute("user");
		long userId = user.getId();
		Article article = (Article) req.getSession().getAttribute("article");
		long articleId = article.getId();
		//req.setCharacterEncoding("UTF-8");
		String content = req.getParameter("comment");
		System.out.println(content);
		//String result = URLDecoder.decode(content, "UTF-8");
		
		Comment comment = new Comment(userId, articleId, content, 0, 0, LocalDateTime.now(), true, new HashSet<>());
	
		try {
			//insert new comment related to the article where the user is (URL??)
			commentDao.addComment(comment);
		} catch (SQLException e) {
			System.out.println("op");
		}
		//refresh article comments and forward to the article URL
		resp.setStatus(200);
	}
}
