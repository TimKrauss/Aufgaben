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
	private final static String username = "tim";
	private final static String passwort = "Test123";
	private Connection connection = null;
	private Statement runQueryStatement = null;
	private Logger logger = Logger.getLogger(OracleDataBase.class);

	/**
	 * Stellt die Connection her
	 * 
	 * @param l Den Logger zur UserAusgabe
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
	 * L�scht alle Autos samt Reservierungen aus der Datenbank
	 * 
	 * @return Ob das L�schen erfolgreich war
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
	 * L�scht ein Auto aus der Datenbank
	 * 
	 * @param id Die ID des zu l�schenden Autos
	 * @return Ob das l�schen erfolgreich war
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
	 * L�dt alle Autos samt Reservierungen aus der Datenbank ein
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
			JOptionPane.showMessageDialog(null, "Keine Datenbank erreichbar", "Keine Datenbank", JOptionPane.OK_OPTION);
			System.exit(1);
		}
		return null;
	}

	/**
	 * F�gt Autos aus der Datenbank einer Arraylist hinzu
	 * 
	 * @param cars Die Liste zu welcher die Autos hinzugef�gt werden sollen
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
					if (c.getCarID() == rset.getInt("carnr"))
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
	 * F�gt der Datenbank ein Auto hinzu
	 * 
	 * @param c Auto welches hinzugef�gt werden soll
	 * @return Ob das hinzuf�gen des Autos fumktioniert hat
	 */
	public boolean addCar(Car c)
	{
		try
		{
			if (c.getCarID() == 0)
			{
				c.setCarID(getNextCarID());
			}

			String query = "INSERT INTO AUTOS(NAME, MARKE, TACHO,ID) VALUES ('" + c.getCarName() + "','"
					+ c.getCarMarke() + "'," + c.getCarTacho() + "," + c.getCarID() + ")";

			Statement smt;
			smt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			smt.executeQuery(query);
			smt.close();

			for (Reservierung r : c.getReservs())
			{
				if (c.getReservs().size() == 0)
					break;

				r.setCarID(c.getCarID());
				uploadRes(r);
			}
			return true;
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return false;

	}

	/**
	 * 
	 * @return Gibt die n�chste freie Car-ID zur�ck
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
	 * @return Gibt die n�chste freie Car-ID zur�ck
	 */
	public int getNextReservierungID()
	{
		try
		{
			Statement stmSequence = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet seq = stmSequence.executeQuery("select RESV_SEQ.NEXTVAL from DUAL");
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
	 * F�gt der Datenbank eine Reservierung hinzu
	 * 
	 * @param r Reservierung welche hinzugef�gt werden soll
	 * @return Ob das Hochladen der Reservierung fuktioniert hat
	 */
	public boolean uploadRes(Reservierung r)
	{
		String query = "INSERT INTO RES_AUTO(STARTD,STOPD,CARNR,OWNER,ID) VALUES (?,?,?,?,?)";

		try
		{
			PreparedStatement smt = connection.prepareStatement(query);

			smt.setTimestamp(1, new Timestamp(r.getResStart().getTime()));
			smt.setTimestamp(2, new Timestamp(r.getResStop().getTime()));
			smt.setInt(3, r.getCarID());
			smt.setString(4, r.getOwner());

			r.setRES_ID(getNextReservierungID());

			smt.setInt(5, r.getRES_ID());

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
	 * L�scht eine Reservierung aus der Datenbank
	 * 
	 * @param del Die zu l�schende Reservierung
	 * @return Ob das L�schen der Reservierung funktioniert hat
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
	 * @return Gibt dem Username der Datenbank zur�ck
	 */
	public String getDataBaseUser()
	{
		return username;
	}

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

	public Car getCarByID(int int1)
	{
		ResultSet rs = runQuery("SELECT * FROM AUTOS WHERE ID=" + int1);

		Car car = new Car();

		try
		{
			while (rs.next())
			{
				car.setCarName(rs.getString("name"));
				car.setCarMarke(rs.getString("marke"));
				car.setCarTacho(rs.getInt(("tacho")));
				car.setCarID(rs.getInt("id"));
			}
			rs.close();
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		try
		{
			rs.close();
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return car;
	}
}
