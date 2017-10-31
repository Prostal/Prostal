package com.sportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sportal.model.Media;
import com.sportal.model.model_db.MediaDao;

@Controller
public class MediaController {

	@Autowired
	private MediaDao mediaDao;

	@RequestMapping(value = "/ShowMedia", method = RequestMethod.GET)
	public void showMedia(HttpServletRequest request, HttpServletResponse response){

		long mediaId = Long.parseLong(request.getParameter("mediaId"));
		Media media = null ;
		try {
			media = mediaDao.getMediaById(mediaId);
		} catch (SQLException e1) {
			System.out.println("op "+e1.getMessage());
		}
		String mediaUrl = media.getUrl();
		File myFile = new File(mediaUrl);
		try {
			Path path = myFile.toPath();
			Files.copy(path, response.getOutputStream());
		} 
		catch (IOException e) {
			e.getStackTrace();
			System.out.println("op "+e.getMessage());
		}
		finally{
			 try {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (IOException e) {
				e.getStackTrace();
				System.out.println("op "+e.getMessage());
			}
		}
	}
}
