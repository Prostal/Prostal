package com.sportal.controller;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sportal.model.Article;
import com.sportal.model.Category;
import com.sportal.model.User;
import com.sportal.model.model_db.ArticleDao;
import com.sportal.model.model_db.CategoryDao;

@Controller
public class HomeController {
	
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ArticleDao articleDao;

	@RequestMapping(value = "/index", method=RequestMethod.GET )
	public String start(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		//TODO get CASHED FROM DAO CATEGORY
		
		Set<Category> categories = new TreeSet<Category>((o1, o2) -> 
		o1.getName().compareToIgnoreCase(o2.getName()));
		try {
			categories.addAll(categoryDao.getAllCategories());
		} catch (SQLException e) {
			request.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		request.getServletContext().setAttribute("categories", categories);
		
		Set<Article> leadingDao;
		try {
			leadingDao = articleDao.getTop5Leading();
		} catch (SQLException e) {
			request.setAttribute("error", "database problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error";
		}
		request.getSession().setAttribute("leading", leadingDao);
		return "index";
	}
	
	@RequestMapping(value = "/loginPage", method=RequestMethod.GET )
	public String loginPage(){

		// calculate smth
		return "loginPage";
	}
	
	@RequestMapping(value = "/register", method=RequestMethod.GET )
	public String reisterPage(Model m){

		User user = new User();
		m.addAttribute("user", user);
		
		return "registerPage";
	}
	
}
