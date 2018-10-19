package de.krauss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class OracleDataBase
{
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String username = "tim";
	private final String passwort = "Test123";
	private Connection connection = null;
	private Logger logger;

	/**
	 * Stellt die Connection her
	 * 
	 * @param l Den Logger zur UserAusgabe
	 */
	public OracleDataBase(Logger l)
	{

		logger = l;
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

	/**
	 * Löscht alle Autos samt Reservierungen aus der Datenbank
	 * 
	 * @return Ob das Löschen erfolgreich war
	 */
	public boolean delteAllDataFromBase()
	{
		try
		{
			Statement rsvdel = connection.createStatement();
			rsvdel.executeQuery("DELETE FROM res_auto");
			rsvdel.close();

			Statement smt2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt2.executeQuery("DELETE FROM autos");
			smt2.close();
			return true;

		} catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return false;
	}

	/**
	 * Löscht ein Auto aus der Datenbank
	 * 
	 * @param id Die ID des zu löschenden Autos
	 * @return Ob das löschen erfolgreich war
	 */
	public boolean deleteCarFromDatabase(int id)
	{
		try
		{
			Statement rsvdel = connection.createStatement();
			rsvdel.executeQuery("DELETE FROM res_auto WHERE Carnr=" + id);
			rsvdel.close();

			Statement smt2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt2.executeQuery("DELETE FROM autos WHERE id=" + id);
			smt2.close();
			return true;

		} catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @return True, wenn die Connection geschlossen wurde
	 */
	public boolean closeConnection()
	{
		try
		{
			if (!connection.isClosed())
			{
				connection.close();
				return true;
			}
			return false;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Lädt alle Autos samt Reservierungen aus der Datenbank ein
	 * 
	 * @return Die Arraylist beinhaltet die Autos mit Reservierungen aus der
	 *         Datenbank
	 */
	public ArrayList<Car> loadDatabase()
	{
		try
		{
			Statement smt = connection.createStatement();

			String query = "SELECT * FROM Autos ORDER BY id";

			ResultSet rset = smt.executeQuery(query);

			ArrayList<Car> cars = new ArrayList<>();

			Car car = new Car();

			while (rset.next())
			{
				car.setF_Name(rset.getString("name"));
				car.setF_Marke(rset.getString("marke"));
				car.setF_Tacho(rset.getInt(("tacho")));
				car.setCAR_ID(rset.getInt("id"));

				cars.add(car);
				car = new Car();
			}

			addReservierungenToList(cars);

			rset.close();
			smt.close();
			return cars;
		} catch (SQLException e)
		{
			logger.fatal(e);
		} catch (NullPointerException e)
		{
			JOptionPane.showMessageDialog(null, "Keine Datenbank erreichbar", "Keine Datenbank", JOptionPane.OK_OPTION);
			System.exit(1);
		}
		return null;
	}

	/**
	 * Fügt Autos aus der Datenbank einer Arraylist hinzu
	 * 
	 * @param cars Die Liste zu welcher die Autos hinzugefügt werden sollen
	 */
	private void addReservierungenToList(ArrayList<Car> cars)
	{
		try
		{
			Statement smt = connection.createStatement();

			String query = "SELECT * FROM RES_AUTO ORDER BY id";

			ResultSet rset = smt.executeQuery(query);

			while (rset.next())
			{
				for (Car c : cars)
				{
					if (c.getCAR_ID() == rset.getInt("carnr"))
					{
						Reservierung r = new Reservierung();

						r.setResStart(rset.getTimestamp("startd"));
						r.setResStop(rset.getTimestamp("stopd"));
						r.setCarID(rset.getInt("CARNR"));
						r.setRES_ID(rset.getInt("id"));
						c.addResv(r);
					}
				}
			}
			smt.close();
			rset.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fügt der Datenbank ein Auto hinzu
	 * 
	 * @param c Auto welches hinzugefügt werden soll
	 * @return Ob das hinzufügen des Autos fumktioniert hat
	 */
	public boolean addCar(Car c)
	{
		String query = "INSERT INTO AUTOS(NAME, MARKE, TACHO) VALUES ('" + c.getF_Name() + "','" + c.getF_Marke() + "',"
				+ c.getF_Tacho() + ")";

		try
		{
			Statement smt;
			smt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt.executeQuery(query);
			smt.close();
			return true;
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return false;

	}

	/**
	 * Fügt der Datenbank eine Reservierung hinzu
	 * 
	 * @param r Reservierung welche hinzugefügt werden soll
	 * @return Ob das Hochladen der Reservierung fuktioniert hat
	 */
	public boolean uploadRes(Reservierung r)
	{
		String query = "INSERT INTO RES_AUTO(STARTD,STOPD,CARNR) VALUES (?,?,?)";

		try
		{
			PreparedStatement smt = connection.prepareStatement(query);

			smt.setTimestamp(1, new Timestamp(r.getResStart().getTime()));
			smt.setTimestamp(2, new Timestamp(r.getResStop().getTime()));
			smt.setInt(3, r.getCarID());

			smt.executeQuery();
			smt.close();
			logger.info("Reservierung hochgeladen");
			return true;
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return false;
	}

	/**
	 * Löscht eine Reservierung aus der Datenbank
	 * 
	 * @param del Die zu löschende Reservierung
	 * @return Ob das Löschen der Reservierung funktioniert hat
	 */
	public boolean deleteReservierung(Reservierung del)
	{
		try
		{
			Statement smt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt.executeQuery("DELETE FROM res_auto WHERE id=" + del.getRES_ID());
			smt.close();
			return true;
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @return Gibt dem Username der Datenbank zurück
	 */
	public String getDataBaseUser()
	{
		return username;
	}
}
