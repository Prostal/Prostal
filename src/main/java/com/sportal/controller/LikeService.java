package com.sportal.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
		
		System.out.println(req.getParameter("commentId"));
		long commentId = Long.parseLong(req.getParameter("commentId"));
		vote(req, resp,commentId);
		System.out.println("status"+resp.getStatus());
		if(resp.getStatus()!=200){
			return;
		}
		try {
			commentDao.likeComment(commentId);
		} catch (SQLException e) {
			System.out.println("op"+e.getMessage());
		}
		resp.setStatus(200);
	}
	
	@RequestMapping(value="/dislike", method=RequestMethod.POST)
	@ResponseBody
	public void dislikeComment(HttpServletRequest req, HttpServletResponse resp){
		long commentId = Long.parseLong(req.getParameter("commentId"));
		vote(req, resp, commentId);
		if(resp.getStatus()!=200){
			return;
		}
		try {
			commentDao.dislikeComment(commentId);
		} catch (SQLException e) {
			System.out.println("op"+e.getMessage());
		}
		resp.setStatus(200);
	}
	
	private void vote(HttpServletRequest req, HttpServletResponse resp, long commentId){
		
		//check if logged
		User user = (User) req.getSession().getAttribute("user");
		if(user==null){
			resp.setStatus(401);
			return;
		}
		//check if voted
		try {
			if(voteDao.hasVotedForComment(user.getId(), commentId)){
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
