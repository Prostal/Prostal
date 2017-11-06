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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sportal.WebInitializer;
import com.sportal.model.Article;
import com.sportal.model.Category;
import com.sportal.model.Comment;
import com.sportal.model.Media;
import com.sportal.model.User;
import com.sportal.model.model_db.ArticleDao;
import com.sportal.model.model_db.CategoryDao;
import com.sportal.model.model_db.MediaDao;

@Controller
@MultipartConfig
public class AdminController {

	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MediaDao mediaDao;

	private final static String SUPPORTED_VIDEO_FORMAT = "video/mp4";
	private final static String SUPPORTED_IMAGE_FORMAT = "image/jpeg";
	

	@RequestMapping(value = "/postArticle", method = RequestMethod.POST)
	public String postArticle(@RequestParam("image") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		
		//credential verification
		if(!isAuthorized(request)){
			response.setStatus(401);
			request.setAttribute("error", "you are not authorized");
			return "redirect:index";
		}
		
		// collect data from request
		String title = request.getParameter("title");
		if (title == null || title.trim().isEmpty() || title.length() > 200) {
			request.setAttribute("error", "Title does not meet our requirements");
			response.setStatus(403);
			return "user";
		}
		String textContent = request.getParameter("textContent");

		if(textContent==null || textContent.trim().isEmpty() || textContent.length()>5000){

			request.setAttribute("error", "Text content does not meet our requirements");
			response.setStatus(403);
			return "user";
		}
		if (!supportedFormat(AdminController.SUPPORTED_IMAGE_FORMAT, file, request, response)) {
			return "user";
		}
		String leading = request.getParameter("isLeading");
		boolean isLeading = leading != null;

		long category_id = Long.parseLong(request.getParameter("category"));

		Set<Media> mediaFiles = new HashSet<>();

		try {

			Set<Comment> comments = new HashSet<>();
			// create article
			Article article = new Article(title, textContent, category_id, LocalDateTime.now(), 0, isLeading,
					mediaFiles, comments);
			// publishArticle(article)
			long articleId = articleDao.addArticle(article);

			String original = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(original);
			String url = String.valueOf(articleId).concat(".").concat(extension);
			File f = new File(WebInitializer.LOCATION + File.separator + url);

			try {
				file.transferTo(f);
			} catch (IllegalStateException | IOException e1) {
				request.setAttribute("error", "File transfer went wrong");
				response.setStatus(500);
				return "error500";
			}
			// UPDATE IN DB

			Media media = new Media(title, f.getAbsolutePath(), false);// false -> not video
			mediaFiles.add(media);
			long mediaId = 0;
			if (mediaDao.getMediaByName(title) == null) {
				mediaId = mediaDao.addMedia(media);
				mediaDao.addInArticleMedia(articleId, mediaId);
			} else {
				// if exists in another article
				Media exists = mediaDao.getMediaByName(title);
				mediaId = exists.getMedia_id();
			}

		} catch (SQLException e) {
			request.setAttribute("error", "Media DB problem ");
			response.setStatus(500);
			return "error500";
		}
		request.setAttribute("error", title + " added successfully ");
		return "user";

	}
	
	@RequestMapping(value = "/deleteArticle", method = RequestMethod.GET)
	public String deleteArticle(HttpServletRequest request, HttpServletResponse response) {
		
		//credential verification
		if(!isAuthorized(request)){
			response.setStatus(401);
			request.setAttribute("error", "you are not authorized");
			return "redirect:index";
		}
		
		long articleId = Long.parseLong(request.getParameter("articleId"));
		try {
			articleDao.removeArticle(articleId);
		} catch (SQLException e) {
			request.setAttribute("error", "DB problem" + e.getMessage());
			response.setStatus(500);
			return "index500";
		}
		return "user";
	}
	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public String addCategory(HttpServletRequest request, HttpServletResponse response) {

		//credential verification
		if(!isAuthorized(request)){
			response.setStatus(401);
			request.setAttribute("error", "you are not authorized");
			return "redirect:index";
		}
		
		String name = request.getParameter("category");

		if ((name == null || name.trim().isEmpty()) || name.length() < 3 || name.length() > 45) {
			request.setAttribute("error", "Category name does not meet our requirements min 3 max 45 symbols ");
			response.setStatus(403);
			return "user";
		}

		try {
			if (!categoryDao.existsCategory(name)) {
				categoryDao.addCategory(new Category(name));
				request.setAttribute("error", "Category " + name + " has been created");
			} else {
				request.setAttribute("error", "Category name already exists");
				response.setStatus(403);
				return "user";
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Category problem " + e.getMessage());
			response.setStatus(500);
			return "error500";
		}
		return "user";
	}

	@RequestMapping(value = "/addVideo", method = RequestMethod.POST)
	public String addArticleMedia(@RequestParam("video") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		
		//credential verification
		if(!isAuthorized(request)){
			response.setStatus(401);
			request.setAttribute("error", "you are not authorized");
			return "redirect:index";
		}
		
		// collect data from request
		long articleId = Long.parseLong(request.getParameter("articleId"));
		Article article = null;
		try {
			article = articleDao.getArtticleById(articleId);
		} catch (SQLException e2) {
			request.setAttribute("error", "DB problem" + e2.getMessage());
			response.setStatus(500);
			return "error500";
		}

		String newname = articleId + "articleId" + article.getMediaFiles().size();// next
																					// num
		String original = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(original);

		if (!supportedFormat(AdminController.SUPPORTED_VIDEO_FORMAT, file, request, response)) {
			return "user";
		}

		String url = newname.concat(".").concat(extension);
		File f = new File(WebInitializer.LOCATION + File.separator + url);

		try {
			file.transferTo(f);

		} catch (IllegalStateException | IOException e1) {
			request.setAttribute("error", "IO problem " + e1.getMessage());
			response.setStatus(500);
			return "index500";
		}

		// UPDATE IN DB
		Media media = new Media(newname, f.getAbsolutePath(), true);
		long mediaId = 0;
		try {
			if (mediaDao.getMediaByName(newname) == null) {
				mediaId = mediaDao.addMedia(media);
				mediaDao.addInArticleMedia(articleId, mediaId);
			} else {
				// if exists in another article
				Media exists = mediaDao.getMediaByName(newname);
				mediaId = exists.getMedia_id();
			}

		} catch (SQLException e) {
			request.setAttribute("error", "DB problem " + e.getMessage());
			response.setStatus(500);
			return "index500";
		}

		return "index";
	}
	
	private boolean isAuthorized(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		return user.isAdmin();
	}

	private boolean supportedFormat(String supportedType, MultipartFile file, HttpServletRequest request, HttpServletResponse res) {
		Tika tika = new Tika();

		if (file.getSize() > WebInitializer.MAX_REQUEST_SIZE) {
			request.setAttribute("error", "this file exceeds the max. file size");
			res.setStatus(403);
			return false;
		}
		try {
			String filetype = tika.detect(file.getBytes());
			if (!filetype.equals(supportedType)) {
				request.setAttribute("error", "this file format is not supported");
				res.setStatus(403);
				return false;
			}
		} catch (IOException e) {
			request.setAttribute("error", "IO problem" + e.getCause().getMessage());
			res.setStatus(500);
			return false;
		}

		return true;
	}
}
