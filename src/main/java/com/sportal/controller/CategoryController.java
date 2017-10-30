package com.sportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sportal.model.Category;
import com.sportal.model.model_db.CategoryDao;


@Controller
public class CategoryController {
	
	@Autowired
	private CategoryDao categoryDao;
	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public String addCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name;
		if ((name = request.getParameter("category"))==null) {
			return "user";
		}
		
		try {
			if (!categoryDao.existsCategory(name)) {
				categoryDao.addCategory(new Category(name));
			}
		} catch (SQLException e) {
			System.out.println("op");
		}
		return "user";
	}
	
	
	
}
