package de.krauss.search;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.OracleDataBase;

public class Searcher
{
	public static final int NAME = 1, MARKE = 2, Tacho = 3;
	private OracleDataBase orcb;
	private Logger logger = Logger.getLogger("System");

	/**
	 * 
	 * 
	 * @param l Der Logger zum Ausgeben in der Konsole
	 */
	public Searcher()
	{
		orcb = new OracleDataBase();
	}

	/**
	 * Startet die Suche und fragt nach welcher Eigenschaft gesucht werden soll
	 * 
	 * @param list   Die Liste welche durchsucht werden soll
	 * @param option Wonach gesucht werden soll
	 * @param value  Das Object wonach gesucht werden soll
	 */
	public void search(int option, String value)
	{
		String query = "";
		switch (option)
		{
		case NAME:
			query = "SELECT ID FROM AUTOS WHERE NAME LIKE '%" + value + "%'";
			break;
		case MARKE:
			query = "SELECT ID FROM AUTOS WHERE MARKE LIKE '%" + value + "%'";
			break;
		case Tacho:
			query = "SELECT ID FROM AUTOS WHERE TACHO LIKE '%" + value + "%'";
			break;
		default:
			return;
		}

		try
		{
			ResultSet rs = orcb.runQuery(query);

			while (rs.next())
			{
				Car f = orcb.getCarByID(rs.getInt("ID"));
				logger.info("--------------");
				logger.info("Name: " + f.getCarName());
				logger.info("Marke: " + f.getCarMarke());
				logger.info("Tacho: " + f.getCarTacho());
				logger.info("--------------");
			}
			rs.close();
			orcb.closeStatement();
		} catch (Exception e)
		{
			logger.fatal(e.getMessage());
		}
	}

	public OracleDataBase getDataBase()
	{
		return orcb;
	}

}
