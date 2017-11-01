package com.sportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sportal.model.Comment;
import com.sportal.model.User;
import com.sportal.model.Vote;
import com.sportal.model.model_db.CommentDao;
import com.sportal.model.model_db.VoteDao;

@RestController
public class LikeService {
	
	@Autowired
	VoteDao voteDao;
	
	@Autowired
	CommentDao commentDao;

	@RequestMapping(value="/like", method=RequestMethod.POST)
	@ResponseBody
	public void likeComment(HttpServletRequest req, HttpServletResponse resp){
		
		System.out.println("commentId "+req.getParameter("commentId"));
		long commentId = Long.parseLong(req.getParameter("commentId"));
		//vote(req, resp,commentId);
		//test only
//		try {
//			Comment test = commentDao.getCommentById(commentId);
//			System.out.println("likes = "+test.getLikes());
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		//check if logged
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);
			return;
		}
		//check if voted
		try {
			if(voteDao.hasVotedForComment(user.getId(), commentId)){
				//commentDao.deleteComments(testClean);
				resp.setStatus(402);
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add vote in db
		/* Vote vote = new Vote(commentId, user.getId());
		try {
			// voteDao.insertVote(vote);
			// commentDao.likeComment(commentId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {

			resp.setStatus(200);
			String responseJsonString = "{\"articleId\": 123, \"likesCount\": 123, \"username\": \"cocko\"}";
			resp.setContentType("application/json");
			resp.getWriter().write(responseJsonString);
			resp.getWriter().flush();
			resp.getWriter().close();	
		} catch (IOException ignored) {}
	}
	
	
	@RequestMapping(value="/dislike", method=RequestMethod.POST)
	@ResponseBody
	public void dislikeComment(HttpServletRequest req, HttpServletResponse resp){
		
		long commentId = Long.parseLong(req.getParameter("commentId"));
		
//TODO FIX REPATED CODE
		//vote(req, resp,commentId);
		//check if logged
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);
			return;
		}
		//check if voted
		try {
			if(voteDao.hasVotedForComment(user.getId(), commentId)){
				//commentDao.deleteComments(testClean);
				resp.setStatus(402);
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add vote in db
		Vote vote = new Vote(commentId, user.getId());
		try {
			voteDao.insertVote(vote);
			commentDao.dislikeComment(commentId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resp.setStatus(200);
	}

}
