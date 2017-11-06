package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sportal.model.Media;

@Component
public  class MediaDao extends Dao{

	
	
	public long addMedia(Media media) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String insert = "INSERT INTO media (name, content_url, isVideo) VALUES (?, ?, ?)";
		try(PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, media.getName());
			ps.setString(2, media.getUrl());
			ps.setBoolean(3, media.getIsVideo());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			media.setId(rs.getLong(1));
			
			return media.getMedia_id();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public boolean addInArticleMedia(long articleId, long mediaId) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs =  null;
		String insert = "INSERT INTO article_media (article_id, media_id) VALUES (?, ?)";
		try(PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, articleId);
			ps.setLong(2, mediaId);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return rs.next();
			
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public boolean removeMedia(long media_id) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String delete = "DELETE FROM media m WHERE m.media_id = ?";
		try(PreparedStatement ps = con.prepareStatement(delete, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, media_id);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return rs.next();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	
	public  Media getMediaByName(String name) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT media_id, name, content_url, isVideo FROM media m WHERE m.name = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setString(1, name);
			rs = ps.executeQuery();
			if(!rs.next()){
				return null;
			}
			return new Media(rs.getLong("media_id"), name, rs.getString("content_url"), rs.getBoolean("isVideo"));
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	
	public  Media getMediaById(long mediaId) throws SQLException{
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT m.name, m.content_url, isVideo FROM media m WHERE m.media_id = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			
			ps.setLong(1, mediaId);
			rs = ps.executeQuery();
			if(!rs.next()){
				return null;
			}
			return new Media(mediaId, rs.getString(1), rs.getString(2),rs.getBoolean(3));
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	public  Set<Media> getMediaByArticle(long id) throws SQLException{
		Set<Media> mediaFiles = new HashSet<Media>();
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT m.media_id, m.name, m.content_url, m.isVideo, am.article_id FROM media as m JOIN article_media as am WHERE m.media_id = am.media_id and am.article_id = ?";
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				long media_id = rs.getLong(1);
				String name = rs.getString(2);
				String url = rs.getString(3);
				boolean isVideo = rs.getBoolean(4);
				mediaFiles.add(new Media(media_id, name, url, isVideo));
			}
			return mediaFiles;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		
	}
	

	
	
}
