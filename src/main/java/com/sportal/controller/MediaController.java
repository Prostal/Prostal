package com.sportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
			request.setAttribute("error", "SQL problem : " + e1.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String mediaUrl = media.getUrl();
		File f = new File(mediaUrl);
		
		try {

			Files.copy(f.toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			request.setAttribute("error", "input problem : " + e.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				
			}
		}
	}
}
