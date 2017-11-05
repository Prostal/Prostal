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
		String name = category.getName();
		if(name==null || name.trim().isEmpty()){
			return;
		}
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, name);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		long id = rs.getLong(1);
		category.setCategoryId(id);
		//categories.put(id, category);
	}
	
	public boolean existsCategory(String name) throws SQLException{

		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT count(*) as count FROM categories c WHERE c.name = ?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		rs.next();
	
		return rs.getInt("count")>0;
	}
	
	public Set<Category> getAllCategories() throws SQLException{
		
		Set<Category> categories = new HashSet<>();
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT c.category_id , c.name FROM categories c ");
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			categories.add(new Category(rs.getLong(1), rs.getString(2)));
			
		}
		return categories;
	}
	
	public boolean removeCategory(long category_id) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE FROM categories c WHERE c.category_id = ?", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, category_id);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		//this.categories.remove(category_id);
		return rs.next();
	}
	
	
	public  Category getCategoryByName(String name) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT category_id, name FROM categories c WHERE c.name = ?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return new Category( rs.getLong("category_id"),name);
	}
	
	public  Category getCategoryById(long categoryId) throws SQLException{
		Connection con = dbManager.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT category_id, name FROM categories c WHERE c.category_id = ?");
		ps.setLong(1, categoryId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return new Category( categoryId,rs.getString(2));
	}
	

	
	
}
