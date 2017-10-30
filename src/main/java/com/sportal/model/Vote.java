package com.sportal.model;

import javax.validation.constraints.NotNull;

public class Vote {
	
	
	private long voteId;
	@NotNull
	private long commentId;
	@NotNull
	private long userId;
	
	
	public Vote(long voteId, long commentId, long userId) {
		this(commentId, userId);
		this.voteId = voteId;
	}
	
	public Vote(long commentId, long userId) {
		this.commentId = commentId;
		this.userId = userId;
	}

	public long getVoteId() {
		return voteId;
	}

	public long getCommentId() {
		return commentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setVoteId(long voteId) {
		this.voteId = voteId;
	}

	
	
}
