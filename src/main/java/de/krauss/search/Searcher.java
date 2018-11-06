package de.krauss.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.OracleDataBase;

public class Searcher
{
	public static final int NAME = 1, MARKE = 2, Tacho = 3;
	private OracleDataBase orcb;

	public void setOrcb(OracleDataBase orcb)
	{
		this.orcb = orcb;
	}

	private Logger logger = Logger.getLogger("System");

	/**
	 * 
	 * 
	 */
	public Searcher()
	{
		//
	}

	/**
	 * Startet die Suche und fragt nach welcher Eigenschaft gesucht werden soll
	 * 
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
			boolean anyCarFound = false;
			ResultSet rs = getOrcb().runQuery(query);

			while (rs.next())
			{
				if (!anyCarFound)
					anyCarFound = true;

				Car f = getOrcb().getCarByID(rs.getInt("ID"));
				logger.info("--------------");
				logger.info("Name: " + f.getCarName());
				logger.info("Marke: " + f.getCarMarke());
				logger.info("Tacho: " + f.getCarTacho());
				logger.info("--------------");
			}
			rs.close();
			getOrcb().closeStatement();

			if (!anyCarFound)
			{
				logger.info("Es wurde kein Auto mit diesem Suchkriterium gefunden");
			}

		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
	}

	public OracleDataBase getDataBase()
	{
		return orcb;
	}

	public ArrayList<Car> searchAll(String autoName, String autoMarke, String tachoStand)
	{
		ArrayList<Car> carList = new ArrayList<>();

		String query = "SELECT ID FROM AUTOS WHERE NAME LIKE '%" + autoName + "%' AND MARKE LIKE '%" + autoMarke
				+ "%' AND TACHO LIKE '%" + tachoStand + "%'";

		try
		{
			boolean anyCarFound = false;
			ResultSet resultSet = getOrcb().runQuery(query);

			while (resultSet.next())
			{
				if (!anyCarFound)
					anyCarFound = true;

				Car car = getOrcb().getCarByID(resultSet.getInt("ID"));
				logger.info("--------------");
				logger.info("Name: " + car.getCarName());
				logger.info("Marke: " + car.getCarMarke());
				logger.info("Tacho: " + car.getCarTacho());
				logger.info("--------------");
				carList.add(car);
			}
			resultSet.close();
			getOrcb().closeStatement();

			if (!anyCarFound)
			{
				logger.info("Es wurde kein Auto mit diesem Suchkriterium gefunden");
			}
		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
		return carList;
	}

	public OracleDataBase getOrcb()
	{
		return orcb;
	}

}
