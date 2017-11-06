package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sportal.model.Category;

@Component
public  class CategoryDao extends Dao{
	


	public void addCategory(Category category) throws SQLException{
		
		Connection con = dbManager.getConnection();
		String insert = "INSERT INTO categories (name) VALUES (?)";
		ResultSet rs = null;
		try(PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, category.getName());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			long id = rs.getLong(1);
			category.setCategoryId(id);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
		
	}
	
	public boolean existsCategory(String name) throws SQLException{

		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT count(*) AS count FROM categories c WHERE c.name = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setString(1, name);
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count")>0;
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public Set<Category> getAllCategories() throws SQLException{
		
		Set<Category> categories = new HashSet<>();
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT c.category_id , c.name FROM categories c ";
		try(PreparedStatement ps = con.prepareStatement(select)){
			rs = ps.executeQuery();
			while (rs.next()) {
				categories.add(new Category(rs.getLong(1), rs.getString(2)));
			}
			return categories;
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public void removeCategory(long category_id) throws SQLException{
		Connection con = dbManager.getConnection();
		String delete = "DELETE FROM categories c WHERE c.category_id = ?";
		
		try(PreparedStatement ps = con.prepareStatement(delete)){
			ps.setLong(1, category_id);
			ps.executeUpdate();
			
		}catch (SQLException e) {
			throw e;
		}
		
	}
	
	
	public  Category getCategoryByName(String name) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT category_id, name FROM categories c WHERE c.name = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setString(1, name);
			rs = ps.executeQuery();
			rs.next();
			return new Category( rs.getLong("category_id"),name);
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public  Category getCategoryById(long categoryId) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT category_id, name FROM categories c WHERE c.category_id = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setLong(1, categoryId);
			rs = ps.executeQuery();
			rs.next();
			return new Category( categoryId,rs.getString(2));
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	

	
	
}
