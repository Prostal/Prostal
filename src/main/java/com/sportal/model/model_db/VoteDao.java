package com.sportal.model.model_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.sportal.model.Vote;

@Component
public class VoteDao extends Dao {

	public long insertVote(Vote vote) throws SQLException {
		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String insert = "INSERT INTO votes (comments_comment_id, users_user_id) VALUES (?,?)";
		try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, vote.getCommentId());
			ps.setLong(2, vote.getUserId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			vote.setVoteId(rs.getLong(1));

			return vote.getVoteId();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

	}

	// check if userId has voted for commentId
	public boolean hasVotedForComment(long userId, long commentId) throws SQLException {

		Connection con = dbManager.getConnection();
		ResultSet rs = null;
		String select = "SELECT count(*) as count FROM votes v WHERE v.comments_comment_id=? AND v.users_user_id= ?";

		try (PreparedStatement ps = con.prepareStatement(select)) {
			ps.setLong(1, commentId);
			ps.setLong(2, userId);
			rs = ps.executeQuery();
			rs.next();

			return rs.getInt("count") > 0;

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	public void deleteVotes(long commentsCommentId) throws SQLException {
		Connection con = dbManager.getConnection();
		String sql = "delete FROM votes where comments_comment_id = ?";
		// try with resources no finally, no catch:)
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentsCommentId);
			ps.executeUpdate();
		}

	}
}
