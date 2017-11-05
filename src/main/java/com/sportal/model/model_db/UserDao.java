package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sportal.model.User;
import com.sportal.util.Encrypter;

@Component
public  class UserDao extends Dao{
	
	
	public void insertUser(User u) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String insert = "INSERT INTO users (username, password, email, isAdmin, isBanned, date_time_registered) VALUES (?, ?, ?, ?, ?, ?)";
		try(PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, u.getUsername());
			ps.setString(2, Encrypter.encrypt(new StringBuilder(u.getPassword()).reverse().toString()));//reversed password to be hashed
			ps.setString(3, u.getEmail());
		
			ps.setBoolean(4, u.isAdmin());
			ps.setBoolean(5, u.isBanned());
			ps.setTimestamp(6, Timestamp.valueOf(u.getRegistered()));
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
		
			u.setId(rs.getLong(1));
			
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	public boolean existsUser(String username, String password) throws SQLException {

		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT count(*) as count FROM users u WHERE u.username = ? AND u.password = ?";
		
		try(PreparedStatement ps = con.prepareStatement(select)) {
			ps.setString(1, username);
			ps.setString(2, Encrypter.encrypt(new StringBuilder(password).reverse().toString()));
			rs = ps.executeQuery();
			rs.next();
			
			return rs.getInt("count")>0;
			
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public boolean existsUserMail(String username, String mail) throws SQLException {

		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT count(*) as count FROM users u WHERE u.username = ? OR u.email = ?";
		
		try(PreparedStatement ps = con.prepareStatement(select)) {
			ps.setString(1, username);
			ps.setString(2, mail);
			rs = ps.executeQuery();
			rs.next();
			
			return rs.getInt("count")>0;
			
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public User getUser(String username) throws SQLException{
		
		Connection con = dbManager.getConnection();
		ResultSet rs=null;
		String select = "SELECT user_id, username, password, email, avatar_url, age, date_time_registered, isBanned, isAdmin FROM users WHERE username = ?";
		try(PreparedStatement ps = con.prepareStatement(select)) {
			
			ps.setString(1, username);
			rs = ps.executeQuery();
			rs.next();
			
			return new User(
							rs.getLong("user_id"), 
							username, 
							rs.getString("password"), 
							rs.getString("email"),
							rs.getString("avatar_url"),
							18,//rs.getInt("age")
							rs.getTimestamp("date_time_registered").toLocalDateTime(),
							rs.getBoolean("isBanned"),
							rs.getBoolean("isAdmin")
							);
		} 
		finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	public User getUserById(long userId) throws SQLException{
		
		Connection con = dbManager.getConnection();
		ResultSet rs=null;
		String select = "SELECT user_id, username, password, email, avatar_url, age, date_time_registered, isBanned, isAdmin FROM users WHERE user_id = ?";
		try(PreparedStatement ps = con.prepareStatement(select)) {
			
			ps.setLong(1, userId);
			rs = ps.executeQuery();
			rs.next();
			
			return new User(
							userId, 
							rs.getString("username"), 
							rs.getString("password"), 
							rs.getString("email"),
							rs.getString("avatar_url"),
							18,//rs.getInt("age")
							rs.getTimestamp("date_time_registered").toLocalDateTime(),
							rs.getBoolean("isBanned"),
							rs.getBoolean("isAdmin")
							);
		} 
		finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	public boolean updateAvatar(String avatar_url, String username) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String update = "UPDATE users u SET u.avatar_url = ? WHERE u.username = ?";
		try (PreparedStatement ps = con.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, avatar_url);
			ps.setString(2, username);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
			return rs.next();
		}  
		finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	public boolean isAdmin(String username, String password) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "select u.isAdmin  from users u where u.name = ?";
		
		try (PreparedStatement ps = con.prepareStatement(select)){
			ps.setString(1, username);
			ps.setString(2, new StringBuilder(password).reverse().toString());
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt("u.isAdmin") == 1;
		}
		finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	
	public Set<User> getCommentators(long articleId) throws SQLException {
		Set<User> commentators = new HashSet<>();
		String commentator = "SELECT u.user_id, u.email, u.password, u.username, u.age, u.avatar_url, u.isAdmin, u.isBanned, u.date_time_registered from users u JOIN comments c ON c.user_id = u.user_id WHERE c.article_id = ?";
		Connection con = dbManager.getConnection();
		ResultSet rs=null;
		
		try(PreparedStatement ps = con.prepareStatement(commentator)) {
			ps.setLong(1, articleId);
			rs = ps.executeQuery();
			while(rs.next()){
				commentators.add(new User(
									rs.getLong("u.user_id"),
									rs.getString("u.username"),
									rs.getString("u.password"),
									rs.getString("u.email"),
									rs.getString("u.avatar_url"),
									rs.getInt("u.age"),
									rs.getTimestamp("u.date_time_registered").toLocalDateTime(),
									rs.getBoolean("isBanned"),
									rs.getBoolean("isAdmin")));
			}
			
			
		} 
		finally {
			if (rs != null) {
				rs.close();
			}
		}
		return commentators;
	}
	
	
	public Set<User> getVoters(long commentId) throws SQLException {
		Set<User> voteUsers = new HashSet<>();
		String voters = "SELECT u.user_id, u.email, u.password, u.username, u.age, u.avatar_url, u.isAdmin, u.isBanned, u.date_time_registered  from users u join votes v  on u.user_id = v.users_user_id join comments c on c.comment_id = v.comments_comment_id group by c.comment_id =?";
		Connection con = dbManager.getConnection();
		ResultSet rs=null;
		
		try(PreparedStatement ps = con.prepareStatement(voters)) {
			ps.setLong(1, commentId);
			rs = ps.executeQuery();
			while(rs.next()){
				voteUsers.add(new User(
									rs.getLong("u.user_id"),
									rs.getString("u.username"),
									rs.getString("u.password"),
									rs.getString("u.email"),
									rs.getString("u.avatar_url"),
									rs.getInt("u.age"),
									rs.getTimestamp("u.date_time_registered").toLocalDateTime(),
									rs.getBoolean("isBanned"),
									rs.getBoolean("isAdmin")));
			}
			
			
		} 
		finally {
			if (rs != null) {
				rs.close();
			}
		}
		return voteUsers;
	}
}
