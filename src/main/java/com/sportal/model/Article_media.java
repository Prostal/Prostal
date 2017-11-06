package com.sportal.model;

import javax.validation.constraints.NotNull;

public class Article_media {

	@NotNull
	private long article_id;
	@NotNull
	private long media_id;

	public Article_media(long article_id, long media_id) {

		this.article_id = article_id;
		this.media_id = media_id;
	}

	public long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(long article_id) {
		this.article_id = article_id;
	}

	public long getMedia_id() {
		return media_id;
	}

	public void setMedia_id(long media_id) {
		this.media_id = media_id;
	}

}
