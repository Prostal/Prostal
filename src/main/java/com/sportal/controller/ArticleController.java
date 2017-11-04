package com.sportal.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sportal.model.Article;
import com.sportal.model.model_db.ArticleDao;

@Controller
public class ArticleController {

	@Autowired
	private ArticleDao articleDao;

	private enum Sort {
		IMMPRESSIONS, COMMENTED, LEADING
	};

	@RequestMapping(value = "/pickArticle", method = RequestMethod.GET)
	public String getArticle(HttpServletRequest request, HttpServletResponse response) {
		// review article content and all the media related to the article

		long articleId = Long.parseLong(request.getParameter("articleId"));

		Article article = null;
		try {
			article = articleDao.getArtticleById(articleId);

			// increment immpressions
			articleDao.incremenImpression(articleId);
		} catch (SQLException e) {
			System.out.println("op" + e.getMessage());
		}
		// TODO THINK ABOUT SCOPE AGAIN
		request.getSession().setAttribute("article", article);
		return "article";
		
	}

	@RequestMapping(value = "/deleteArticle", method = RequestMethod.GET)
	public String deleteArticle(HttpServletRequest request, HttpServletResponse response) {
		long articleId = Long.parseLong(request.getParameter("articleId"));
		System.out.println("delete articleId:" + articleId);
		try {
			articleDao.removeArticle(articleId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}

	@RequestMapping(value = "/categoryArticles", method = RequestMethod.GET)
	public String pickCategory(HttpServletRequest request, HttpServletResponse response) {
		// list all articles related to the category
		long categoryId = Long.parseLong(request.getParameter("category"));

		if (categoryId > 0) {
			Set<Article> articles;
			try {
				articles = articleDao.getArtticlesByCategory(categoryId);
				request.getSession().setAttribute("articles", articles);

			} catch (SQLException e) {
				System.out.println("op" + e.getMessage());
			}
		}
		// reveals submenu with all the subcategories belonging to the category
		return "categoryArticles";
	}

	@RequestMapping(value = "/Search", method = RequestMethod.GET)
	public String search(HttpServletRequest request, HttpServletResponse response) {
		String search = request.getParameter("search");

		if(search==null || search.trim().isEmpty()){
			request.setAttribute("error", "TIP of the day: there is more info between spaces");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "index";
		}else if(search.length() < 2){
			request.setAttribute("error", "Search result: Yes, we have this letter "+search);
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "index";
		}
		
		String category = request.getParameter("category");
		
		String checkbox = request.getParameter("checkbox");
		Set<Article> result = new HashSet<>();

		try {
			result.addAll(articleDao.getArtticlesByTitle(search));
			if (checkbox != null) {
				long categoryId = Long.parseLong(category);
				Set<Article> byCategory = articleDao.getArtticlesByCategory(categoryId);
				result.retainAll(byCategory);
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Ops it is not you");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "error500";
		}
		
		request.getSession().setAttribute("search", result);
		return "searchResult";
	}

	@RequestMapping(value = "/Top5", method = RequestMethod.GET)
	public String getTop5(HttpServletRequest request, HttpServletResponse response) {
		// list top 5 articles on selected condition
		String sort = request.getParameter("sort");

		Set<Article> articles = new TreeSet<Article>((o1, o2) -> (int) (o2.getImpressions() - o1.getImpressions()));

		switch (sort) {
		case "impressions":
			try {
				articles.addAll(articleDao.getTop5ByImpressions());
				request.getSession().setAttribute("articles", articles);

			} catch (SQLException e) {
				System.out.println("op");
			}
			break;
		case "leading":
			try {
				articles.addAll(articleDao.getTop5Leading());
				// filter leading by period here if ypu want
				request.getSession().setAttribute("articles", articles);

			} catch (SQLException e) {
				System.out.println("op");
			}
			break;
		case "commented":
			try {
				articles.addAll(articleDao.getTop5ByComments());
				request.getSession().setAttribute("articles", articles);
				System.out.println(articles.size());
			} catch (SQLException e) {
				System.out.println("op");
			}
			break;

		default:
			break;
		}

		// reveals submenu with all the subcategories belonging to the category
		return "top5";
	}

}
