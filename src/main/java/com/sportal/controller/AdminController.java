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
import org.apache.tika.Tika;
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

	private final static String SUPPORTED_VIDEO_FORMAT = "video/mp4";
	private final static String SUPPORTED_IMAGE_FORMAT = "image/jpeg";

	@RequestMapping(value = "/postArticle", method = RequestMethod.POST)
	public String postArticle(@RequestParam("image") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		//collect data from request
	
		String title = request.getParameter("title");
		if(title==null || title.trim().isEmpty() || title.length()>200){
			request.setAttribute("error", "Title does not meet our requirements");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "user";
		}
		String textContent = request.getParameter("textContent");
		if(textContent==null || textContent.trim().isEmpty() || textContent.length()>2000){
			request.setAttribute("error", "Text content does not meet our requirements");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "user";
		}
		String leading = request.getParameter("isLeading");
		boolean isLeading = leading!=null;
		
		long category_id = Long.parseLong(request.getParameter("category"));
		
		Set<Media> mediaFiles = new HashSet<>();
	
		try {
			
			Set<Comment> comments = new HashSet<>();
			// create article 
			Article article = new Article(title, textContent, category_id, LocalDateTime.now(), 0, isLeading, mediaFiles,comments);
			// publishArticle(article)
			long articleId = articleDao.addArticle(article);
			
			
			String original = file.getOriginalFilename();
			
			if(!supportedFormat(AdminController.SUPPORTED_IMAGE_FORMAT, file, request)){
				return "user";
			}
			
			String extension = FilenameUtils.getExtension(original);
			String url = String.valueOf(articleId).concat(".").concat(extension);
			File f = new File(WebInitializer.LOCATION + File.separator + url);
			
			try {
				file.transferTo(f);
			} catch (IllegalStateException | IOException e1) {
				request.setAttribute("error", "File transfer went wrong");
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				return "user";
			}
			// UPDATE IN DB
			
			Media media = new Media(title, f.getAbsolutePath(), false);//false -> not video
			mediaFiles.add(media);
			long mediaId = 0;
			if(mediaDao.getMediaByName(title)==null){
				mediaId = mediaDao.addMedia(media);
				mediaDao.addInArticleMedia(articleId, mediaId);
			}else{
				//if exists in another article
				Media exists = mediaDao.getMediaByName(title);
				mediaId = exists.getMedia_id();
			}
			
		} catch (SQLException e) {
			request.setAttribute("error", "Media DB problem ");
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "user";
		}
		request.setAttribute("error", title+" added successfully ");
		return "user";
		
	}
	
	private boolean supportedFormat(String supportedType, MultipartFile file, HttpServletRequest request) {
		Tika tika = new Tika();
		
		if(file.getSize()>WebInitializer.MAX_REQUEST_SIZE){
			request.setAttribute("error", "this file exceeds the max. file size");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return false;
		}
		try {
			String filetype = tika.detect(file.getBytes());
			System.out.println(filetype);
			if(!filetype.equals(supportedType)){
				request.setAttribute("error", "this file format is not supported");
				ResponseEntity.status(HttpStatus.FORBIDDEN);
				return false;
			}
		} catch (IOException e) {
			request.setAttribute("error", "IO problem"+e.getCause().getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return false;
		}
		
		return true;
	}

	@RequestMapping(value = "/addVideo", method = RequestMethod.POST)
	public String addArticleMedia(@RequestParam("video") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		//collect data from request
		
		long articleId  = Long.parseLong(request.getParameter("articleId"));
		Article article = null;
		try {
			article = articleDao.getArtticleById(articleId);
		} catch (SQLException e2) {
			System.out.println("add video"+e2.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}
		
		
		String newname = articleId+"articleId"+article.getMediaFiles().size();//next num
		String original = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(original);
		
		
		if(!supportedFormat(AdminController.SUPPORTED_VIDEO_FORMAT, file, request)){
			return "user";
		}
		
		String url = newname.concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + url);
		
		try {
			file.transferTo(f);
			
		} catch (IllegalStateException | IOException e1) {
			request.setAttribute("error", "IO problem "+e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "index500";
		}
			
		// UPDATE IN DB
		Media media = new Media(newname, f.getAbsolutePath(), true);
		long mediaId = 0;
		//TODO REPEATING CODE FIX
		try {
			if(mediaDao.getMediaByName(newname)==null){
				mediaId = mediaDao.addMedia(media);
				mediaDao.addInArticleMedia(articleId, mediaId);
			}else{
				//if exists in another article
				Media exists = mediaDao.getMediaByName(newname);
				mediaId = exists.getMedia_id();
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			request.setAttribute("error", "DB problem "+e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "index500";
		}
		
		
		return "index";
	}
	


	

}
