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
	

	public  Comment addComment(Comment comment) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO comments (user_id, article_id, content, likes, dislikes, date_time, isApproved ) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, comment.getUserId());
		ps.setLong(2, comment.getArticleId());
		ps.setString(3, comment.getContent());
		ps.setInt(4, comment.getLikes());
		ps.setInt(5, comment.getDislikes());
		ps.setTimestamp(6, Timestamp.valueOf(comment.getTimeCreated()));
		ps.setBoolean(7, true);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		long id = rs.getLong(1);
		comment.setId(id);
		return comment;
		
	}
	
//	private void addInCommentsByArticle(long article_id, Comment comment) {
//		if(!this.commentsByArticle.containsKey(article_id)) {
//			this.commentsByArticle.put(article_id, new HashSet<>());
//		}
//		this.commentsByArticle.get(article_id).add(comment);
//	}

	public  boolean removeComment(Comment comment) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE FROM comments WHERE c.comment_id = ?", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, comment.getCommentId());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		//removeFromCommentsByArticle(comment);
		return rs.next();
	}
	
//	private void removeFromCommentsByArticle(Comment comment) {
//		this.commentsByArticle.get(comment.getArticleId()).remove(comment);
//	}

	public boolean updateComment(long comment_id, String content) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE comments c SET c.content = content WHERE c.comment_id = ?", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, comment_id);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		return rs.next();
	}
	
	public  void disaproveComments() throws SQLException{
		Connection con = dbManager.getConnection();
		String[] forbiddenWords = "fuck, maika ti, balo si mamata".split(", ");
		for (String word : forbiddenWords) {
			PreparedStatement ps = con.prepareStatement("UPDATE comments c SET c.isApproved = false WHERE c.content like %?%", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, word.trim());
			ps.executeUpdate();
		}
		// Just for fun
	}
	
	
//    public synchronized Set<Comment> getCommentsByArticle(long article_id) throws SQLException{
//		Set<Comment> comments = this.commentsByArticle.get(article_id);
//		
//		return Collections.unmodifiableSet(comments);
//	}
	
	//likes
    public  void likeComment(long comment_id) throws SQLException {
    	Connection con = dbManager.getConnection();
    	
		PreparedStatement ps = con.prepareStatement("UPDATE comments c SET c.likes = likes+1 WHERE c.comment_id = ?");
		ps.setLong(1, comment_id);
		ps.executeUpdate();
    }
    //dislikes
    public  void dislikeComment(long comment_id) throws SQLException {
    	Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE comments c SET c.dislikes = dislikes+1 WHERE c.comment_id = ?", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, comment_id);
		ps.executeUpdate();
    }
    
    public  Set<Comment> getCommentsByArticle(long id) throws SQLException{
		Set<Comment> comments = new HashSet<Comment>();
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT comment_id, user_id, article_id, content, likes, dislikes, date_time, isApproved FROM sportal.comments WHERE article_id=?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			long commentId = rs.getLong(1);
			long userId = rs.getLong(2);
			long articleId = rs.getLong(3);
			String content = rs.getString(4);
			int likes = rs.getInt(5);
			int dislikes = rs.getInt(6);
			LocalDateTime timeCreated = rs.getTimestamp(7).toLocalDateTime();
			boolean isAproved = rs.getBoolean(8);
			//TODO 
			Set<User> voters = new HashSet<>();
			
			comments.add(new Comment(commentId, userId, articleId, content, likes, dislikes, timeCreated, isAproved, voters));
		}
		return comments;
	}
    
    public Comment getCommentById(long id) throws SQLException{
		
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT comment_id, user_id, article_id, content, likes, dislikes, date_time, isApproved FROM sportal.comments WHERE comment_id=?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		long commentId = rs.getLong(1);
		long userId = rs.getLong(2);
		long articleId = rs.getLong(3);
		String content = rs.getString(4);
		int likes = rs.getInt(5);
		int dislikes = rs.getInt(6);
		LocalDateTime timeCreated = rs.getTimestamp(7).toLocalDateTime();
		boolean isAproved = rs.getBoolean(8);
		//TODO 
		Set<User> voters = new HashSet<>();
		
		return new Comment(commentId, userId, articleId, content, likes, dislikes, timeCreated, isAproved, voters);
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
			System.out.println("before commit");
			con.commit();
			System.out.println("after commit");
		} catch (SQLException e) {
			System.out.println("before rollback"+e.getMessage());
			con.rollback();
			rs.close();
		} finally{
			con.setAutoCommit(true);
		}
	}

	
}
