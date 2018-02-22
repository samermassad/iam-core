package fr.epita.iam.services.users.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.NoIdentityFoundException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.connections.JDBCConnection;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the JDBC User DAO.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>UserJDBCDAOManager dao = new UserJDBCDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class UserJDBCDAO implements UserDAO {

	private static final Logger LOGGER = new Logger(UserJDBCDAO.class);

	/**
	 * Adds the user if not in read only mode and IdentityID is not duplicated and
	 * exists in the Identities table. Otherwise throws {@link ReadOnlyException},
	 * {@link DuplicateException} or {@link NoIdentityFoundException}
	 * 
	 * @param user
	 *            - user to add
	 * @throws CreationException
	 * @throws ReadOnlyException
	 * @throws DuplicateException
	 * @throws SearchException
	 * @throws TransformerException
	 */
	@Override
	public void create(User user) throws CreationException {

		LOGGER.info("Creating user in database: " + user);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCConnection.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO LOGIN_USERS(USERNAME, HASHED_PASSWORD, IDENTITYID) VALUES (?, ?, ?)");
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getHashedPassword());
			preparedStatement.setInt(3, user.getIdentityID());
			preparedStatement.execute();
		} catch (final Exception e) {
			LOGGER.error(
					"Error occured while creating in database the user " + user + "got that error " + e.getMessage());
			throw new CreationException(e, user);
		} finally {
			JDBCConnection.close(connection, preparedStatement, null);
		}
	}

	/**
	 * Get ID of an identity from UID.
	 * 
	 * @param uid
	 * @return id of the identity
	 */
	public int getId(String uid) {
		Connection connection = null;
		int id = 0;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			preparedStatement = connection.prepareStatement("SELECT ID FROM IDENTITIES WHERE LOWER(UID)=?");
			preparedStatement.setMaxRows(1);
			preparedStatement.setString(1, uid.toLowerCase());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString("ID"));
			}
			rs.close();
		} catch (final Exception e) {
			LOGGER.error("Error occured while searching database for identity id: " + uid + "got that error "
					+ e.getMessage());
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		return id;
	}

	/**
	 * Search for a user.
	 * 
	 * @param criteria
	 *            - criteria of user to search for
	 * @return list of matched identities - {(String)username,
	 *         (String)hashedpassword, (int)identityID, (String)indentity's UID}
	 * @throws SearchException
	 */
	@Override
	public List<User> search(User criteria) throws SearchException {
		final List<User> results = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			// SELECT L.USERNAME, L.HASHED_PASSWORD, L.IDENTITYID, I.UID
			// FROM IDENTITIES I
			// INNER JOIN LOGIN_USERS L ON L.IDENTITYID = I.ID
			// WHERE (I.UID = '{UID}') AND (L.USERNAME LIKE '{name}');
			final String sqlString = "SELECT L.USERNAME, L.HASHED_PASSWORD, L.IDENTITYID, I.UID " + "FROM IDENTITIES I "
					+ "INNER JOIN LOGIN_USERS L ON L.IDENTITYID = I.ID " + "WHERE (? IS NULL OR L.USERNAME LIKE ?) "
					+ "AND (? = 0 OR L.IDENTITYID = ?)";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, criteria.getUserName());
			preparedStatement.setString(2, "%" + criteria.getUserName() + "%");
			preparedStatement.setInt(3, criteria.getIdentityID());
			preparedStatement.setInt(4, criteria.getIdentityID());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				final User currentUser = new User();
				currentUser.setUserName(rs.getString("USERNAME"));
				currentUser.setPassword(rs.getString("HASHED_PASSWORD"));
				currentUser.setIdentityID(rs.getInt("IDENTITYID"));
				currentUser.setUid(rs.getString("UID"));
				results.add(currentUser);
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
	 * Updates the information of a user in the database if not in read only mode.
	 * Otherwise throws {@link ReadOnlyException}
	 * 
	 * @param from
	 *            - original user
	 * @param to
	 *            - edited user
	 * @throws ReadOnlyException
	 * @throws UpdateException
	 * @throws TransformerException
	 */
	@Override
	public void update(User from, User to) throws UpdateException {
		if (from.getIdentityID() == to.getIdentityID()) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = JDBCConnection.getConnection();
				// UPDATE LOGIN_USERS SET USERNAME='Samer Massad', HASHED_PASSWORD='Test'
				// WHERE IDENTITYID=(SELECT ID FROM IDENTITIES WHERE UID='2');
				final String sqlString = "UPDATE LOGIN_USERS SET USERNAME=?, HASHED_PASSWORD = COALESCE(?, HASHED_PASSWORD) "
						+ "WHERE IDENTITYID=?";
				preparedStatement = connection.prepareStatement(sqlString);
				preparedStatement.setString(1, to.getUserName());
				preparedStatement.setString(2, to.getHashedPassword());
				preparedStatement.setInt(3, to.getIdentityID());
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
	 * Delete a user from database if not in read only mode. Otherwise throws
	 * {@link ReadOnlyException}
	 * 
	 * @param identity
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	@Override
	public void delete(User user) throws DeleteException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCConnection.getConnection();
			final String sqlString = "DELETE FROM LOGIN_USERS "
					+ "WHERE IDENTITYID=(SELECT ID FROM IDENTITIES WHERE UID=?)";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, user.getUid());
			final int rs = preparedStatement.executeUpdate();
			if (rs == 0) {
				LOGGER.warning("Trying to delete an user from the database that doesn't exist: " + user);
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while deleting user from database: " + user, e);
			throw new DeleteException(e, user);
		} finally {
			JDBCConnection.close(connection, preparedStatement, null);
		}
	}

	/**
	 * @param criteria
	 *            - criteria of the user
	 * @return user
	 */
	@Override
	public User getUserByIdentityId(User criteria) throws SearchException {
		Connection connection = null;
		User result = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			final String sqlString = "SELECT * FROM LOGIN_USERS " + "WHERE IDENTITYID = ?";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setMaxRows(1);
			preparedStatement.setInt(1, criteria.getIdentityID());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = new User();
				result.setUserName(rs.getString("USERNAME"));
				result.setHashedPassword(rs.getString("HASHED_PASSWORD"));
				result.setIdentityID(Integer.parseInt(rs.getString("IDENTITYID")));
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while getting user by UID from the database: " + criteria.getIdentityID(), e);
			throw new SearchException(e, criteria);
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		return result;
	}

	/**
	 * 
	 * @param login
	 *            - login credentials
	 * @return true if login succeeded, false otherwise
	 * @throws SearchException
	 */
	@Override
	public boolean login(User login) throws SearchException {
		Connection connection = null;
		boolean success = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			// SELECT ID
			// FROM LOGIN_USERS
			// WHERE (USERNAME = '{used username}') AND (HASHEDPASSWORD =
			// '{hash_of_used_password}');
			final String sqlString = "SELECT ID FROM LOGIN_USERS " + "WHERE (USERNAME = ?) "
					+ "AND (HASHED_PASSWORD = ?)";
			preparedStatement = connection.prepareStatement(sqlString);

			preparedStatement.setString(1, login.getUserName());
			preparedStatement.setString(2, login.getHashedPassword());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				success = true;
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while logining in", e);
			throw new SearchException(e, login);
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		if (success) {
			LOGGER.info("Successful login from database using username: " + login.getUserName());
		} else {
			LOGGER.warning("Failed login from database using username: " + login.getUserName());
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.epita.iam.services.users.dao.UserDAO#checkOldPwd(fr.epita.iam.datamodel.
	 * User, fr.epita.iam.datamodel.User)
	 */
	@Override
	public boolean checkOldPwd(User oldUser) throws SearchException {
		// not needed
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.users.dao.UserDAO#usernameExists(fr.epita.iam.datamodel.User)
	 */
	@Override
	public boolean usernameExists(User user) throws SearchException {
		Connection connection = null;
		boolean success = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCConnection.getConnection();
			// SELECT ID
			// FROM LOGIN_USERS
			// WHERE (USERNAME = '{used username}');
			final String sqlString = "SELECT IDENTITYID FROM LOGIN_USERS " + "WHERE (USERNAME = ?)";
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, user.getUserName());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if(Integer.parseInt(rs.getString("IDENTITYID")) != user.getIdentityID()) {
					// not the current user
					success = true;
					break;
				}
				
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while checking if username exists", e);
			throw new SearchException(e, user);
		} finally {
			JDBCConnection.close(connection, preparedStatement, rs);
		}
		return success;
	}

}
