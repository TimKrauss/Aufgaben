package de.krauss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class UserManager
{
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private final static String username = "tim";
	private final static String passwort = "Test123";
	private Connection connection;

	private Logger logger = Logger.getLogger("System");

	public UserManager()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			OracleDataSource ods = new OracleDataSource();
			ods.setURL(url);

			connection = ods.getConnection(username, passwort);
		} catch (SQLException e)
		{
			logger.fatal("FATAL: " + e.getMessage());
		} catch (ClassNotFoundException e)
		{
			logger.fatal("FATAL: " + e.getMessage());
		}
	}

	public void addUser(String Username, String passwordHash, int rechteLvl)
	{
		String query = "INSERT INTO tim.benutzer(NAME,PASSWORD,RECHTELVL) VALUES (?,?,?)";

		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, Username);
			statement.setString(2, passwordHash);
			statement.setInt(3, rechteLvl);
			statement.executeQuery();
			statement.close();
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
	}

	public String hashPasswort(String passwordToHash)
	{
		String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(passwordToHash);
		return sha256hex;
	}

	public User login(String username, String passwordHash)
	{
		String query = "SELECT * FROM tim.benutzer WHERE name='" + username + "'";

		User user = new User();

		try
		{
			Statement smt = connection.createStatement();
			ResultSet resultSet = smt.executeQuery(query);
			String dataHash = "";

			if (resultSet.next())
			{
				dataHash = resultSet.getString("password");
			} else
			{
				resultSet.close();
				smt.close();
				return null;
			}

			if (passwordHash.equals(dataHash))
			{
				user.setPasswordHash(passwordHash);
				user.setBenutzerName(resultSet.getString("name"));
				user.setRechteLvl(resultSet.getInt("rechtelvl"));
				user.setBenutzerID(resultSet.getInt("benutzerid"));
				resultSet.close();
				smt.close();
				return user;
			}
			resultSet.close();
			smt.close();
		} catch (SQLException e)
		{
			logger.fatal("SQLException beim Login : " + e.getMessage());
		}

		return null;
	}

}
