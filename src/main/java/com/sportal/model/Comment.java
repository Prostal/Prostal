package com.sportal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Comment implements Serializable {

	private static final long serialVersionUID = -2083047749077809648L;
	private long commentId;
	@NotNull
	private long userId;
	@NotNull
	private long articleId;
	@NotNull
	@Size(min = 2, max = 200)
	private String content;
	private int likes;
	private int dislikes;
	private LocalDateTime timeCreated;
	private boolean isAproved;
	private Set<User> voters;

	public Comment(long commentId, long userId, long articleId, String content, int likes, int dislikes,
			LocalDateTime timeCreated, boolean isAproved, Set<User> voters) {
		this(userId, articleId, content, likes, dislikes, timeCreated, isAproved, voters);
		this.commentId = commentId;
	}

	public Comment(long userId, long articleId, String content, int likes, int dislikes, LocalDateTime timeCreated,
			boolean isAproved, Set<User> voters) {
		super();
		this.userId = userId;
		this.articleId = articleId;
		this.content = content;
		this.likes = likes;
		this.dislikes = dislikes;
		this.timeCreated = timeCreated;
		this.isAproved = isAproved;
		this.voters = voters;
	}

	public boolean canVote(User user) {
		return !this.voters.contains(user);
	}

	public Set<User> getVoters() {
		return Collections.unmodifiableSet(this.voters);
	}

	public long getCommentId() {
		return commentId;
	}

	public long getUserId() {
		return userId;
	}

	public long getArticleId() {
		return articleId;
	}

	public String getContent() {
		return content;
	}

	public int getLikes() {
		return likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public LocalDateTime getTimeCreated() {
		
		return timeCreated;
	}

	public boolean isAproved() {
		return isAproved;
	}

	public void setId(long id) {
		this.commentId = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (articleId ^ (articleId >>> 32));
		result = prime * result + (int) (commentId ^ (commentId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (articleId != other.articleId)
			return false;
		if (commentId != other.commentId)
			return false;
		return true;
	}

}
