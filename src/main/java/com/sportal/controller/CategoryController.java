package com.sportal.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public String addCategory(HttpServletRequest request, HttpServletResponse response) {

		String name = request.getParameter("category");

		if ((name == null || name.trim().isEmpty()) || name.length() < 3 || name.length() > 45) {
			request.setAttribute("error", "Category name does not meet our requirements min 3 max 45 symbols ");
			ResponseEntity.status(HttpStatus.FORBIDDEN);
			return "user";
		}

		try {
			if (!categoryDao.existsCategory(name)) {
				categoryDao.addCategory(new Category(name));
				request.setAttribute("error", "Category " + name + " has been created");
			} else {
				request.setAttribute("error", "Category name already exists");
				ResponseEntity.status(HttpStatus.FORBIDDEN);
				return "user";
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Category problem " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			return "error500";
		}
		return "user";
	}

}
