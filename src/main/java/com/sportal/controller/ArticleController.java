package com.sportal.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sportal.model.Article;
import com.sportal.model.User;
import com.sportal.model.model_db.ArticleDao;
import com.sportal.model.model_db.CategoryDao;
import com.sportal.model.model_db.UserDao;

@Controller
public class ArticleController {

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CategoryDao categoryDao;

	@RequestMapping(value = "/pickArticle", method = RequestMethod.GET)
	public String getArticle(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		// review article content and all the media related to the article

		long articleId = Long.parseLong(request.getParameter("articleId"));

		Article article = null;
		try {
			article = articleDao.getArtticleById(articleId);
			Set<User> commentators = userDao.getCommentators(articleId);
			HashMap<Long, String> mapp = new HashMap<>();
			for (User user : commentators) {
				mapp.put(user.getId(), user.getUsername());

			}
			map.addAttribute("commentators", mapp);
			// increment immpressions
			articleDao.incrementImpression(articleId);
		} catch (SQLException e) {
			request.setAttribute("error", "DB problem " + e.getMessage());
			response.setStatus(500);
			return "index500";
		}

		request.getSession().setAttribute("article", article);
		return "article";

	}


	@RequestMapping(value = "/categoryArticles", method = RequestMethod.GET)
	public String pickCategory(HttpServletRequest request, HttpServletResponse response) {
		// list all articles related to the category
		long categoryId = Long.parseLong(request.getParameter("category"));

		if (categoryId > 0) {

			try {
				request.getSession().setAttribute("articles", articleDao.getArtticlesByCategory(categoryId));
				request.setAttribute("category", categoryDao.getCategoryById(categoryId));

			} catch (SQLException e) {
				request.setAttribute("error", "DB problem" + e.getMessage());
				response.setStatus(500);
				return "index500";
			}
		} else {
			request.setAttribute("error", "no such category");
			response.setStatus(403);
			return "index";
		}
		// reveals submenu with all the subcategories belonging to the category
		return "categoryArticles";
	}

	@RequestMapping(value = "/Search", method = RequestMethod.GET)
	public String search(HttpServletRequest request, HttpServletResponse response) {
		String search = request.getParameter("search");

		if (search == null || search.trim().isEmpty()) {
			request.setAttribute("error", "TIP of the day: there is more info between spaces");
			response.setStatus(403);
			return "index";
		} else if (search.length() < 2) {
			request.setAttribute("error", "Search result: Yes, we have this letter " + search);
			response.setStatus(403);
			return "index";
		}

		String checkbox = request.getParameter("checkbox");
		Set<Article> result = new LinkedHashSet<>();

		try {
			result.addAll(articleDao.getArtticlesByTitle(search));
			if (checkbox != null) {
				long categoryId = Long.parseLong(request.getParameter("category"));
				Set<Article> byCategory = articleDao.getArtticlesByCategory(categoryId);
				result.retainAll(byCategory);
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Ops it is not you");
			response.setStatus(500);
			return "error500";
		}
		if (result.isEmpty()) {
			request.setAttribute("error", "no matches were found for the specified query:  " + search);
		}

		request.getSession().setAttribute("search", result);
		return "searchResult";
	}

	@RequestMapping(value = "/Top5", method = RequestMethod.GET)
	public String getTop5(HttpServletRequest request, HttpServletResponse response) {
		// list top 5 articles on selected condition
		String sort = request.getParameter("sort");

		try {
			switch (sort) {
			case "impressions":
				request.setAttribute("articles", articleDao.getTop5ByImpressions());
				break;
			case "leading":
				request.setAttribute("articles", articleDao.getTop5Leading());
				break;
			case "commented":
				request.setAttribute("articles", articleDao.getTop5ByComments());
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			request.setAttribute("error", "DB problem" + e.getMessage());
			response.setStatus(500);
			return "index500";
		}

		// reveals submenu with all the subcategories belonging to the category
		return "top5";
	}

}
