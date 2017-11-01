package com.sportal.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
		// BELOW offers list with links to the last few (3) articles related to
		// the article tags
		// AT RIGHT offers a column with the last articles belonging to the sub
		// category
		// list comments related to the article
		// show buttons to sort comments by (1: Date/Time DESC; 2: Date/Time
		// ASC; 3: Most liked DESC; 4: Most disliked DESC; 5: Absolute rating
		// (Likes-Dislikes))
		// shows number of previews(impressions)
		// FB comment plug in??
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
			System.out.println("op" + e.getMessage());
		}
		// TODO THINK ABOUT LOW SCOPE
		
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
				articles.addAll(articleDao.getTop3Leading());
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
