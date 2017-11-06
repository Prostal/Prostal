package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportal.model.Comment;
import com.sportal.model.User;


@Component
public  class CommentDao extends Dao{
	
	@Autowired
	private VoteDao voteDao;
	
	@Autowired
	private UserDao userDao;
	

	public  Comment addComment(Comment comment) throws SQLException{
		Connection con = dbManager.getConnection();
		String insert = "INSERT INTO comments (user_id, article_id, content, likes, dislikes, date_time, isApproved ) VALUES (?,?,?,?,?,?,?)";
		ResultSet rs = null;
		try(PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, comment.getUserId());
			ps.setLong(2, comment.getArticleId());
			ps.setString(3, comment.getContent());
			ps.setInt(4, comment.getLikes());
			ps.setInt(5, comment.getDislikes());
			ps.setTimestamp(6, Timestamp.valueOf(comment.getTimeCreated()));
			ps.setBoolean(7, true);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			long id = rs.getLong(1);
			comment.setId(id);
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
		return comment;
		
	}
	

	public  boolean removeComment(Comment comment) throws SQLException{
		Connection con = dbManager.getConnection();
		String delete = "DELETE FROM comments WHERE c.comment_id = ?";
		ResultSet rs = null;
		try(PreparedStatement ps = con.prepareStatement(delete, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, comment.getCommentId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return rs.next();
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	

	public boolean updateComment(long comment_id, String content) throws SQLException{
		Connection con = dbManager.getConnection();
		String update = "UPDATE comments c SET c.content = content WHERE c.comment_id = ?";
		ResultSet rs = null;
		try(PreparedStatement ps = con.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, comment_id);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return rs.next();
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	
	//likes
    public  void likeComment(long comment_id) throws SQLException {
    	Connection con = dbManager.getConnection();
    	String update = "UPDATE comments c SET c.likes = likes+1 WHERE c.comment_id = ?";
    	try(PreparedStatement ps = con.prepareStatement(update)){
    		ps.setLong(1, comment_id);
    		ps.executeUpdate();
    	}catch (SQLException e) {
			throw e;
		}
		
		
    }
    
    //dislikes
    public  void dislikeComment(long comment_id) throws SQLException {
    	Connection con = dbManager.getConnection();
    	String update = "UPDATE comments c SET c.dislikes = dislikes+1 WHERE c.comment_id = ?";
    	try(PreparedStatement ps = con.prepareStatement(update)){
    		ps.setLong(1, comment_id);
    		ps.executeUpdate();
    	}catch (SQLException e) {
			throw e;
		}
    }
    
    public  Set<Comment> getCommentsByArticle(long id) throws SQLException{
    	HashSet<Comment> comments = new HashSet<Comment>();
		Connection con = dbManager.getConnection();
		String select = "SELECT comment_id, user_id, article_id, content, likes, dislikes, date_time, isApproved FROM sportal.comments WHERE article_id=?";
		ResultSet rs = null;
		
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setLong(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				long commentId = rs.getLong(1);
				long userId = rs.getLong(2);
				long articleId = rs.getLong(3);
				String content = rs.getString(4);
				int likes = rs.getInt(5);
				int dislikes = rs.getInt(6);
				LocalDateTime timeCreated = rs.getTimestamp(7).toLocalDateTime();
				boolean isAproved = rs.getBoolean(8);
				
				Set<User> voters = userDao.getVoters(commentId);
				Comment comment = new Comment(commentId, userId, articleId, content, likes, dislikes, timeCreated, isAproved, voters);
				
				comments.add(comment);
			}
			return comments;
		} 
		finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
    
    public Comment getCommentById(long id) throws SQLException{
		
		Connection con = dbManager.getConnection();
		String select = "SELECT comment_id, user_id, article_id, content, likes, dislikes, date_time, isApproved FROM sportal.comments WHERE comment_id=?";
		ResultSet rs = null;
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();
			
			long commentId = rs.getLong(1);
			long userId = rs.getLong(2);
			long articleId = rs.getLong(3);
			String content = rs.getString(4);
			int likes = rs.getInt(5);
			int dislikes = rs.getInt(6);
			LocalDateTime timeCreated = rs.getTimestamp(7).toLocalDateTime();
			boolean isAproved = rs.getBoolean(8);
			
			Set<User> voters = userDao.getVoters(commentId);
			
			return new Comment(commentId, userId, articleId, content, likes, dislikes, timeCreated, isAproved, voters);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
    public void deleteComments(long articleId) throws SQLException  {
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		
		String selectComments = "SELECT comment_id FROM comments WHERE article_id = ?";
		String deleteComments = "DELETE FROM comments WHERE article_id = ?";
		try (PreparedStatement ps = con.prepareStatement(selectComments)) {
			con.setAutoCommit(false);
			ps.setLong(1, articleId);
			ps.executeQuery();
			rs = ps.executeQuery();
			while(rs.next()){
				long commentId = rs.getLong(1);
				voteDao.deleteVotes(commentId);
			}
			try(PreparedStatement ps2 = con.prepareStatement(deleteComments)){
				ps2.setLong(1, articleId);
				ps2.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			
		} finally{
			rs.close();
			con.setAutoCommit(true);
		}
	}

	
}
