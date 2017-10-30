package com.sportal.model.model_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class Dao {
	
	@Autowired
	protected DBManager dbManager;

}
