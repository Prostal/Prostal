package com.sportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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
		
		long commentId = Long.parseLong(req.getParameter("commentId"));

		//check if logged
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);//Unauthorized
			return;
		}
		//check if voted
		try {
			if(voteDao.hasVotedForComment(user.getId(), commentId)){
				resp.setStatus(403);// Forbidden
				return;
			}
		} catch (SQLException e) {
			resp.setStatus(500);
			return;
		}
		
		Vote vote = new Vote(commentId, user.getId());
		// add vote in db
		
		long likesCount = 0;
		try {
			voteDao.insertVote(vote);
			commentDao.likeComment(commentId);
			Comment c = commentDao.getCommentById(commentId);
			likesCount = c.getLikes();
			
		} catch (SQLException e) {
			resp.setStatus(500);
			return;
		}
		
		JSONObject jObj = new JSONObject();
		jObj.put("likesCount", likesCount);
		jObj.put("commentId", commentId);
		
		resp.setContentType("application/json");
		try {
			resp.getWriter().write(jObj.toString());
		} catch (IOException e) {
			resp.setStatus(500);
			return;
		}finally {
			try {
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (IOException e) {
				resp.setStatus(500);
				return;
			}
		}
		resp.setStatus(200);
	}
	
	@RequestMapping(value="/dislike", method=RequestMethod.POST)
	@ResponseBody
	public void dislikeComment(HttpServletRequest req, HttpServletResponse resp){
		
		long commentId = Long.parseLong(req.getParameter("commentId"));

		//check if logged
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);
			return;
		}
		//check if voted
		try {
			if(voteDao.hasVotedForComment(user.getId(), commentId)){
				resp.setStatus(403);
				return;
			}
		} catch (SQLException e) {
			resp.setStatus(500);
		}
		// add vote in db
		Vote vote = new Vote(commentId, user.getId());
		long dislikesCount = 0;
		try {
			voteDao.insertVote(vote);
			commentDao.dislikeComment(commentId);
			Comment c = commentDao.getCommentById(commentId);
			dislikesCount = c.getDislikes();
			
		} catch (SQLException e) {
			resp.setStatus(500);
			return;
		}
		
		JSONObject jObj = new JSONObject();
		jObj.put("dislikesCount", dislikesCount);
		jObj.put("commentId", commentId);
		
		resp.setContentType("application/json");
		try {
			resp.getWriter().write(jObj.toString());
		} catch (IOException e) {
			resp.setStatus(500);
			return;
		}finally {
			
			try {
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (IOException e) {
				resp.setStatus(500);
				return;
			}
		}
		resp.setStatus(200);
	}

}
