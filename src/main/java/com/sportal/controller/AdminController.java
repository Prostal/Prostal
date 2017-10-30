package com.sportal.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
	public String postArticle(@RequestParam("image") MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
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
	//	String url = null;
//		try {
//			url = getUrl(request);
//		} catch (IllegalStateException | IOException | ServletException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		String original = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(original);
		String url = title.concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + url);
		try {
			file.transferTo(f);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IllegalStateException | IOException e1) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		// UPDATE IN DB
		Media media = new Media(title, url);
		mediaFiles.add(media);
		long mediaId = 0;
		try {
			if(mediaDao.getMediaByName(title)==null){
				mediaId = mediaDao.addMedia(media);
			}else{
				//if exists in another article
				Media exists = mediaDao.getMediaByName(title);
				mediaId = exists.getMedia_id();
			}
			Set<Comment> comments = new HashSet<>();
			// create article 
			Article article = new Article(title, textContent, category_id, LocalDateTime.now(), 0, isLeading, mediaFiles,comments);
			// publishArticle(article)
			long articleId = articleDao.addArticle(article);
			mediaDao.addInArticleMedia(articleId, mediaId);
		} catch (SQLException e) {
			System.out.println("op");
		}
		return "redirect:user";
		
	}
	
	private String getUrl(HttpServletRequest req) throws IllegalStateException, IOException, ServletException{
		    Part avatarPart = req.getPart("image");
		    String title = req.getParameter("title");
			InputStream fis = avatarPart.getInputStream();
			String image = Media.IMAGE_URL+title+".jpg";
			File myFile = new File(image);
			if(!myFile.exists()){
				myFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(myFile);
			int b = fis.read();
			while(b != -1){
				fos.write(b);
				b = fis.read();
			}
			fis.close();
			fos.close();
			// UPDATE IN DB
			return image;
	}
	

}
