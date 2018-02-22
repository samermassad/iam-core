package fr.epita.iam.services.identity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.connections.JDBCConnection;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the JDBC DAO.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>IdentityJDBCDAOManager dao = new IdentityJDBCDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class IdentityJDBCDAO implements IdentityDAO {

	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);

	/**
	 * Add an identity to the database.
	 * 
	 * @param identity - the identity to add
	 * @throws CreationException
	 */	 
	@Override
	public void create(Identity identity) throws CreationException {

		LOGGER.info("Creating identity in database: " + identity);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCConnection.getConnection();
			preparedStatement = connection
					.prepareStatement("INSERT INTO IDENTITIES(UID, EMAIL, DISPLAY_NAME) VALUES (?, ?, ?)");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getEmail());
			preparedStatement.setString(3, identity.getDisplayName());
			preparedStatement.execute();
		} catch (final Exception e) {
			LOGGER.error("Error occured while creating in database the identity " + identity + "got that error "
					+ e.getMessage());
			throw new CreationException(e, identity);
		} finally {
			JDBCConnection.close(connection, preparedStatement, null);
		}
	}

	/**
	 * Search the database for an identity
	 * 
	 * @param criteria - criteria of the identity to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	@Override
	public List<Identity> search(Identity criteria) throws SearchException {

		final List<Identity> results = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			final String sqlString = "SELECT DISPLAY_NAME, EMAIL, UID FROM IDENTITIES "
					+ "WHERE (? IS NULL OR DISPLAY_NAME LIKE ?) " + "AND (? IS NULL OR EMAIL LIKE ?) "
					+ "AND (? IS NULL OR LOWER(UID) = ?)";
			preparedStatement = connection.prepareStatement(sqlString);

			preparedStatement.setString(1, criteria.getDisplayName());
			preparedStatement.setString(2, "%" + criteria.getDisplayName() + "%");
			preparedStatement.setString(3, criteria.getEmail());
			preparedStatement.setString(4, "%" + criteria.getEmail() + "%");
			preparedStatement.setString(5, criteria.getUid());
			if(criteria.getUid() != null) preparedStatement.setString(6, criteria.getUid().toLowerCase());
			else preparedStatement.setString(6, null);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				final Identity currentIdentity = new Identity();
				currentIdentity.setDisplayName(rs.getString("DISPLAY_NAME"));
				currentIdentity.setEmail(rs.getString("EMAIL"));
				currentIdentity.setUid(rs.getString("UID"));

				results.add(currentIdentity);
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while performing search in the database", e);
			throw new SearchException(e, criteria);
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		return results;
	}

	/**
	 * Update information of an identity in the database
	 * 
	 * @param from - original identity
	 * @param to - edited identity
	 * @throws UpdateException
	 */
	@Override
	public void update(Identity from, Identity to) throws UpdateException {
		if (from.getUid().equals(to.getUid())) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = JDBCConnection.getConnection();

				final String sqlString = "UPDATE IDENTITIES " + "SET DISPLAY_NAME=? " + ", EMAIL=? " + "WHERE UID=?";
				System.out.println(sqlString);
				preparedStatement = connection.prepareStatement(sqlString);

				preparedStatement.setString(1, to.getDisplayName());
				preparedStatement.setString(2, to.getEmail());
				preparedStatement.setString(3, to.getUid());
				preparedStatement.executeUpdate();
			} catch (ClassNotFoundException | SQLException e) {
				LOGGER.error("error while performing search in the database", e);
				throw new UpdateException(e, to);
			} finally {
				JDBCConnection.close(connection, preparedStatement, null);
			}
		} else {
			LOGGER.error("Cannot change the UID of the user.");
			throw new UpdateException(to);
		}
	}

	/**
	 * Delete an identity from the database, also deletes the user associated with it.
	 * 
	 * @param identity - identity to delete
	 * @throws DeleteException
	 */
	@Override
	public void delete(Identity identity) throws DeleteException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCConnection.getConnection();
			final String sqlString = "DELETE FROM IDENTITIES WHERE UID = ?";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, identity.getUid());
			final int rs = preparedStatement.executeUpdate();
			if (rs == 0) {
				LOGGER.warning("Trying to delete an identity from the database that doesn't exist: " + identity);
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while deleting user from database: " + identity, e);
			throw new DeleteException(e, identity);
		} finally {
			JDBCConnection.close(connection, preparedStatement, null);
		}
	}

	@Override
	public Identity getUserByUid(Identity criteria) throws SearchException {
		Connection connection = null;
		Identity result = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			final String sqlString = "SELECT * FROM IDENTITIES WHERE UID = ?";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setMaxRows(1);
			preparedStatement.setString(1, criteria.getUid());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = new Identity();
				result.setDisplayName(rs.getString("DISPLAY_NAME"));
				result.setEmail(rs.getString("EMAIL"));
				result.setUid(rs.getString("UID"));
			}
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while getting user by UID from the database: " + criteria.getUid(), e);
			throw new SearchException(e, criteria);
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		return result;
	}

}
