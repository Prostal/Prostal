package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportal.model.Article;
import com.sportal.model.Comment;
import com.sportal.model.Media;


@Component
public  class ArticleDao extends Dao{

	
	@Autowired 
	private MediaDao mediaDao;
	
	@Autowired
	private CommentDao commentDao;

	public long addArticle(Article article) throws SQLException{
		
		Connection con = dbManager.getConnection();
		String insert = "INSERT INTO articles (category_id, title, content, datetime, impressions, isLeading) VALUES (?,?,?,?,?,?)";
		ResultSet rs = null;
		try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, article.getCategory_id());
			ps.setString(2, article.getTitle());
			ps.setString(3, article.getTextContent());
			ps.setTimestamp(4, Timestamp.valueOf(article.getCreated()));//https://www.youtube.com/watch?v=CEjU9KVABao
			ps.setLong(5, article.getImpressions());
			ps.setBoolean(6, article.isLeading());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			long article_id = rs.getLong(1);
			article.setId(article_id);
			return article.getId();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public void removeArticle(long articleId) throws SQLException{
		Connection con = dbManager.getConnection();
		
		String deleteArticleMedia = "DELETE FROM article_media WHERE article_id=?";
		String deleteArticle = "delete from articles  where article_id = ?";
		
		try (PreparedStatement ps = con.prepareStatement(deleteArticleMedia)) {
			con.setAutoCommit(false);
			ps.setLong(1, articleId);
			ps.executeUpdate();
			
			commentDao.deleteComments(articleId);
			con.setAutoCommit(false);//double return :)
			PreparedStatement ps2 = con.prepareStatement(deleteArticle);
			ps2.setLong(1, articleId);
			ps2.executeUpdate();
			
			con.commit();
			ps2.close();
			
		} catch (SQLException e) {
			System.out.println("before article rollback"+e.getMessage());
			con.rollback();
		} finally{
			
			con.setAutoCommit(true);
		}
		
	}
	
	
	//get all artticles by category_id
	public Set<Article> getArtticlesByCategory(long categoryId) throws SQLException{
		Set<Article> articles = new LinkedHashSet<Article>();
		Connection con  = dbManager.getConnection();
		String select = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading  FROM  articles as a  WHERE a.category_id= ? ORDER BY a.datetime desc ";
		ResultSet rs = null;
		
		try(PreparedStatement ps = con.prepareStatement(select)){
			ps.setLong(1, categoryId);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				long articleId = rs.getLong(1);
				String title = rs.getString(3);
				String textContent = rs.getString(4);
				LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
				long impressions = rs.getInt(6);
				boolean isLeading = rs.getInt(7)==1;
				Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
				Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
				Article a = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles,comments);
				
				articles.add(a);
			}
			return articles;
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	//get all articles by title
		public Set<Article> getArtticlesByTitle(String  title) throws SQLException{
			Set<Article> articles = new HashSet<Article>();
			Connection con  = dbManager.getConnection();
			String select = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading  FROM  articles as a  WHERE a.title LIKE ?";
			ResultSet rs = null;
			try(PreparedStatement ps = con.prepareStatement(select)){
				ps.setString(1, "%"+title+"%");
				rs = ps.executeQuery();
				while (rs.next()) {
					
					long articleId = rs.getLong(1);
					long categoryId = rs.getLong(2);
					title = rs.getString(3);
					String textContent = rs.getString(4);
					LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
					long impressions = rs.getInt(6);
					boolean isLeading = rs.getInt(7)==1;
					Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
					Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
					Article a = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles,comments);
					articles.add(a);
				}
				
				return articles;
			}
			finally {
				if (rs != null) {
					rs.close();
				}
			}
			
		}
		
		public Article getArtticleById(long articleId) throws SQLException{
			
			Connection con  = dbManager.getConnection();
			String select = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading  FROM  articles as a  WHERE a.article_id=?";
			ResultSet rs = null;
			try(PreparedStatement ps = con.prepareStatement(select)){
				ps.setLong(1, articleId);
				rs = ps.executeQuery();
				rs.next();
					
				long categoryId = rs.getLong(2);
				String title = rs.getString(3);
				String textContent = rs.getString(4);
				LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
				long impressions = rs.getInt(6);
				boolean isLeading = rs.getInt(7)==1;
				Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
				Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
				Article article = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles, comments);
					
				return article;
			}
			finally {
				if (rs != null) {
					rs.close();
				}
			}
		}
		
		public void incremenImpression(long articleId) throws SQLException {
			
			Connection con  = dbManager.getConnection();
			String update = "UPDATE articles as a SET a.impressions=a.impressions+1  WHERE a.article_id=?";
			try(PreparedStatement ps = con.prepareStatement(update)){
				ps.setLong(1, articleId);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw e;
			}
			
			
		}
		
		public Set<Article> getTop5ByImpressions() throws SQLException{
			Set<Article> articles = new LinkedHashSet<Article>();
			Connection con  = dbManager.getConnection();
			ResultSet rs = null;
			String select = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading  FROM  articles as a  order by a.impressions desc limit 5";
			try(PreparedStatement ps = con.prepareStatement(select)){
				rs = ps.executeQuery();
				while (rs.next()) {
					
					long articleId = rs.getLong(1);
					long categoryId = rs.getLong(2);
					String title = rs.getString(3);
					String textContent = rs.getString(4);
					LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
					long impressions = rs.getInt(6);
					boolean isLeading = rs.getInt(7)==1;
					Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
					Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
					Article a = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles,comments);
					articles.add(a);
				}
				
				return articles;
			}
			finally {
				if (rs != null) {
					rs.close();
				}
			}
			
			
		}
		
		public Set<Article> getTop5Leading() throws SQLException{
			Set<Article> articles = new LinkedHashSet<>();
			
			Connection con  = dbManager.getConnection();
			ResultSet rs = null;
			String select = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading  FROM  articles as a WHERE a.isLeading=1  order by a.datetime desc limit 5";
			try(PreparedStatement ps = con.prepareStatement(select)){
				rs = ps.executeQuery();
				while (rs.next()) {
					
					long articleId = rs.getLong(1);
					long categoryId = rs.getLong(2);
					String title = rs.getString(3);
					String textContent = rs.getString(4);
					LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
					long impressions = rs.getInt(6);
					boolean isLeading = rs.getInt(7)==1;
					Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
					Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
					Article a = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles,comments);
					articles.add(a);
				}
				
				return articles;
			}
			finally {
				if (rs != null) {
					rs.close();
				}
			}
			
			
		}
		
		public Set<Article> getTop5ByComments() throws SQLException{
			Set<Article> articles = new LinkedHashSet<Article>();
			Connection con  = dbManager.getConnection();
			ResultSet rs = null;
			String selectMostCommented = "SELECT a.article_id, a.category_id, a.title, a.content, a.datetime, a.impressions, a.isLeading, count(c.comment_id) as comments FROM articles AS a JOIN comments c ON a.article_id = c.article_id GROUP BY a.article_id order by comments desc limit 5";
			try(PreparedStatement ps = con.prepareStatement(selectMostCommented)){
				rs = ps.executeQuery();
				while (rs.next()) {
					long articleId = rs.getLong(1);
					long categoryId = rs.getLong(2);
					String title = rs.getString(3);
					System.out.println(title);
					String textContent = rs.getString(4);
					LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
					long impressions = rs.getInt(6);
					boolean isLeading = rs.getInt(7)==1;
					Set<Media> mediaFiles = mediaDao.getMediaByArticle(articleId);
					Set<Comment> comments = commentDao.getCommentsByArticle(articleId);
					Article a = new Article(articleId, title, textContent, categoryId, created, impressions, isLeading, mediaFiles,comments);
					articles.add(a);
				}
				
				return articles;
			}
			finally {
				if (rs != null) {
					rs.close();
				}
			}
			
			
		}
}
