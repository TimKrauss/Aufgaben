package de.krauss.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.activity.InvalidActivityException;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.OracleDataBase;
import de.krauss.Utilities;

public class Searcher
{
	public static final int NAME = 1, MARKE = 2, Tacho = 3;
	private OracleDataBase orcb;
	private Logger logger = Logger.getLogger("System");

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
			ResultSet rs = getDataBase().runQuery(query);

			while (rs.next())
			{
				if (!anyCarFound)
					anyCarFound = true;

				Car car = getDataBase().getCarByID(rs.getInt("ID"));
				logger.info("--------------");
				logger.info("Name: " + car.getCarName());
				logger.info("Marke: " + car.getCarMarke());
				logger.info("Tacho: " + car.getCarTacho());
				logger.info("--------------");
			}
			rs.close();
			getDataBase().closeStatement();

			if (!anyCarFound)
			{
				logger.info("Es wurde kein Auto mit diesem Suchkriterium gefunden");
			}

		} catch (SQLException e)
		{
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Sucht nach verschiedenen Parametern (leer lassen wenn egal)
	 * 
	 * @param autoName   Der Autoname wonach gesucht werden soll
	 * @param autoMarke  Die Automarke wonach gesucht werden soll
	 * @param tachoStand Der Tachostand wonach gesucht werden soll
	 * @return
	 */
	public ArrayList<Car> searchAll(String autoName, String autoMarke, String tachoStand)
	{
		ArrayList<Car> carList = new ArrayList<>();

		String query = "SELECT ID FROM AUTOS WHERE NAME LIKE '%" + autoName + "%' AND MARKE LIKE '%" + autoMarke
				+ "%' AND TACHO LIKE '%" + tachoStand + "%'";

		try
		{
			boolean anyCarFound = false;
			ResultSet resultSet = orcb.runQuery(query);

			while (resultSet.next())
			{
				if (!anyCarFound)
					anyCarFound = true;

				Car car = getDataBase().getCarByID(resultSet.getInt("ID"));
				logger.info("--------------");
				logger.info("Name: " + car.getCarName());
				logger.info("Marke: " + car.getCarMarke());
				logger.info("Tacho: " + car.getCarTacho());
				logger.info("--------------");
				carList.add(car);
			}
			resultSet.close();
			getDataBase().closeStatement();

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

	/**
	 * 
	 * @return Gibt die Datenbank-Instanz zurück
	 */
	public OracleDataBase getDataBase()
	{
		return orcb;
	}

	/**
	 * Führt eine Suche durch bei dem über den Reader die Antworten eingelesen
	 * werden
	 * 
	 * @param reader
	 */
	public void searchWithReader(BufferedReader reader)
	{
		int choose = 0;

		logger.info("Nach welchem Merkmal möchten sie suchen?");
		logger.info("[" + Searcher.NAME + "] Name");
		logger.info("[" + Searcher.MARKE + "] Marke");
		logger.info("[" + Searcher.Tacho + "] Tacho");

		while (true)
		{
			try
			{
				choose = Integer.parseInt(reader.readLine());

				if (choose > 0 || choose < 4)
				{
					switch (choose)
					{
					case Searcher.NAME:
						logger.info("Bitte geben sie den Namen des Fahrzeuges an:");
						search(choose, reader.readLine());
						break;
					case Searcher.MARKE:
						logger.info("Bitte geben sie die Marke des Fahrzeuges an:");
						search(choose, reader.readLine());
						break;
					case Searcher.Tacho:
						logger.info("Bitte geben sie den Kilometerstand an:");
						search(choose, Utilities.addTacho(reader) + "");
						break;
					default:
						break;
					}

					break;
				}
			} catch (NumberFormatException e)
			{
				logger.fatal("Bitte eine Zahl ohne Buchstaben eingeben");
			} catch (InvalidActivityException e)
			{
				logger.fatal("Bitte eine gültige Zahl eingeben");
			} catch (IOException e)
			{
				logger.fatal("Reader hat Probleme beim Lesen der UserEingabe");
			}

		}
	}

	/**
	 * 
	 * @param orcb Setzt die Datenbank
	 */
	public void setOrcb(OracleDataBase orcb)
	{
		this.orcb = orcb;
	}
}
