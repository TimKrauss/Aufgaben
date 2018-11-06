package de.krauss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class OracleDataBase
{
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private final static String username = "tim";
	private final static String passwort = "Test123";
	private Connection connection = null;
	private Statement runQueryStatement = null;
	private static Logger logger = Logger.getLogger("System");

	/**
	 * Stellt die Connection her
	 * 
	 */
	public OracleDataBase()
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

		} catch (SQLException e)
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

			Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement2.executeQuery("DELETE FROM autos WHERE id=" + id);
			statement2.close();
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
				car.setCarName(rset.getString("name"));
				car.setCarMarke(rset.getString("marke"));
				car.setCarTacho(rset.getInt(("tacho")));
				car.setCarID(rset.getInt("id"));

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
			logger.fatal("Keine Datenbank vorhanden!");
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
				for (Car car : cars)
				{
					if (car.getCarID() == rset.getInt("carnr"))
					{
						Reservierung resv = new Reservierung();

						resv.setResStart(rset.getTimestamp("startd"));
						resv.setResStop(rset.getTimestamp("stopd"));
						resv.setCarID(rset.getInt("CARNR"));
						resv.setRES_ID(rset.getInt("id"));
						car.addResv(resv);
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
	 * @param car Auto welches hinzugefügt werden soll
	 * @return Ob das hinzufügen des Autos fumktioniert hat
	 */
	public boolean addCar(Car car)
	{
		if (car.getCarID() == 0)
		{
			car.setCarID(getNextCarID());
		}

		String query = "INSERT INTO AUTOS(NAME, MARKE, TACHO,ID) VALUES ('" + car.getCarName() + "','"
				+ car.getCarMarke() + "'," + car.getCarTacho() + "," + car.getCarID() + ")";

		runQuery(query);

		for (Reservierung resv : car.getReservs())
		{
			if (car.getReservs().size() == 0)
				break;

			resv.setCarID(car.getCarID());
			uploadRes(resv);
		}
		closeStatement();
		return true;
	}

	/**
	 * Wird aufgerufen falls ein Auto hochgeladen wird.
	 * 
	 * @return Gibt die nächste freie Car-ID zurück
	 */
	public int getNextCarID()
	{
		try
		{
			Statement stmSequence = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet seq = stmSequence.executeQuery("select AUTOS_SEQUENCE.NEXTVAL from DUAL");
			if (seq.next())
			{
				int reInt = seq.getInt("NEXTVAL");
				seq.close();
				stmSequence.close();
				return reInt;
			}
			stmSequence.close();
			seq.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @return Gibt die nächste freie Car-ID zurück
	 */
	public int getNextReservierungID()
	{
		try
		{
			ResultSet resultSet = runQuery("select RESV_SEQ.NEXTVAL from DUAL");
			if (resultSet.next())
			{
				int reInt = resultSet.getInt("NEXTVAL");
				resultSet.close();
				return reInt;
			}
			resultSet.close();
			closeStatement();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Fügt der Datenbank eine Reservierung hinzu
	 * 
	 * @param resv Reservierung welche hinzugefügt werden soll
	 * @return Ob das Hochladen der Reservierung fuktioniert hat
	 */
	public boolean uploadRes(Reservierung resv)
	{
		String query = "INSERT INTO RES_AUTO(STARTD,STOPD,CARNR,OWNER,ID) VALUES (?,?,?,?,?)";

		try
		{
			PreparedStatement smt = connection.prepareStatement(query);

			smt.setTimestamp(1, new Timestamp(resv.getResStart().getTime()));
			smt.setTimestamp(2, new Timestamp(resv.getResStop().getTime()));
			smt.setInt(3, resv.getCarID());
			smt.setString(4, resv.getOwner());

			resv.setRES_ID(getNextReservierungID());

			smt.setInt(5, resv.getRES_ID());

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
	 * @param resvToDel Die zu löschende Reservierung
	 * @return Ob das Löschen der Reservierung funktioniert hat
	 */
	public boolean deleteReservierung(Reservierung resvToDel)
	{
		try
		{
			Statement smt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt.executeQuery("DELETE FROM res_auto WHERE id=" + resvToDel.getRES_ID());
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

	/**
	 * Führt einen SQL-Befehl aus
	 * 
	 * @param query Der Befehl zum ausführen
	 * @return Das Resultset
	 */
	public ResultSet runQuery(String query)
	{
		try
		{
			runQueryStatement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = runQueryStatement.executeQuery(query);
			return rs;
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return null;
	}

	/**
	 * Schließt das Statement welches für die Query benutzt wird
	 */
	public void closeStatement()
	{
		try
		{
			runQueryStatement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param carId Die ID des Autos welches man möchte
	 * @return Gibt das Auto zurück
	 */
	public Car getCarByID(int carId)
	{
		ResultSet resultSet = runQuery("SELECT * FROM AUTOS WHERE ID=" + carId);

		Car car = new Car();

		try
		{
			while (resultSet.next())
			{
				car.setCarName(resultSet.getString("name"));
				car.setCarMarke(resultSet.getString("marke"));
				car.setCarTacho(resultSet.getInt(("tacho")));
				car.setCarID(resultSet.getInt("id"));
			}
			resultSet.close();
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		try
		{
			resultSet.close();
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return car;
	}
}
