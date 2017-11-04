package com.sportal.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sportal.WebInitializer;
import com.sportal.model.Article;
import com.sportal.model.Comment;
import com.sportal.model.Media;
import com.sportal.model.model_db.ArticleDao;
import com.sportal.model.model_db.MediaDao;


@Controller
@MultipartConfig
public class AdminController {

	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MediaDao mediaDao;

	

	@RequestMapping(value = "/postArticle", method = RequestMethod.POST)
	public String postArticle(@RequestParam("image") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		//collect data from request
	
		String title = request.getParameter("title");
		String textContent = request.getParameter("textContent");
		String leading = request.getParameter("isLeading");
		boolean isLeading = leading!=null;
		String category = request.getParameter("category");
		//TODO VALIDATE ALL
		if(category==null || category.trim().isEmpty() ){
			return "user";
		}
		long category_id = Long.parseLong(category);
		
		Set<Media> mediaFiles = new HashSet<>();
		

			
		try {
			
			Set<Comment> comments = new HashSet<>();
			// create article 
			Article article = new Article(title, textContent, category_id, LocalDateTime.now(), 0, isLeading, mediaFiles,comments);
			// publishArticle(article)
			long articleId = articleDao.addArticle(article);
			String original = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(original);
			String url = String.valueOf(articleId).concat(".").concat(extension);
			File f = new File(WebInitializer.LOCATION + File.separator + url);
			
				try {
					file.transferTo(f);
					
				} catch (IllegalStateException | IOException e1) {
					System.out.println(e1.getMessage());
					System.out.println("op");
					return "index";
				}
			// UPDATE IN DB
			System.out.println("url:"+f.getAbsolutePath());
			Media media = new Media(title, f.getAbsolutePath(), false);//not video
			mediaFiles.add(media);
			long mediaId = 0;
			if(mediaDao.getMediaByName(title)==null){
				mediaId = mediaDao.addMedia(media);
			}else{
				//if exists in another article
				Media exists = mediaDao.getMediaByName(title);
				mediaId = exists.getMedia_id();
			}
			mediaDao.addInArticleMedia(articleId, mediaId);
			System.out.println("after media dao");
			
		} catch (SQLException e) {
			System.out.println("postarticle"+e.getMessage());
//			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		return "user";
		
	}
	
	enum SupportedFormat {MP4, OGG, WEBM};
	@RequestMapping(value = "/addVideo", method = RequestMethod.POST)
	public String addArticleMedia(@RequestParam("video") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		//collect data from request
		//TODO Global Exeption handler
		if(file.getSize()>WebInitializer.MAX_REQUEST_SIZE){
			request.setAttribute("error", "this file exceeds the max. file size");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "user";
		}
		long articleId  = Long.parseLong(request.getParameter("articleId"));
		Article article = null;
		try {
			article = articleDao.getArtticleById(articleId);
		} catch (SQLException e2) {
			System.out.println("add video"+e2.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}
		String title = article.getTitle();
		
		String newname = articleId+"articleId"+article.getMediaFiles().size();//next num
		String original = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(original);
		
		if(!supportedFormat(extension)){
			request.setAttribute("error", "not supported format "+extension);
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "user";
		}
		String url = newname.concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + url);
		
			try {
				file.transferTo(f);
				
			} catch (IllegalStateException | IOException e1) {
				System.out.println("admin"+e1.getMessage());
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				return "error500";
			}
			
		// UPDATE IN DB
		System.out.println("url:"+f.getAbsolutePath());
		Media media = new Media(title, f.getAbsolutePath(), true);
		long mediaId;
		try {
			mediaId = mediaDao.addMedia(media);
			mediaDao.addInArticleMedia(articleId, mediaId);
		} catch (SQLException e) {
			System.out.println("admin"+e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}
		return "index";
	}
	
	private boolean supportedFormat(String extension) {
		for (SupportedFormat format : SupportedFormat.values()) {
			System.out.println(format+" "+extension);
			if(format.toString().equalsIgnoreCase(extension)){
				return true;
			}
		}
		return false;
	}

	

}
