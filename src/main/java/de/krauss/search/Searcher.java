package de.krauss.search;

import org.apache.log4j.Logger;

import de.krauss.CarList;

public class Searcher
{
	public static final int NAME = 1, MARKE = 2, Tacho = 3;
	private SearchMarke searchMarke;
	private SearchName searchName;
	private SearchTacho searchTacho;
	private Logger logger;

	/**
	 * 
	 * @param l Der Logger zum Ausgeben in der Konsole
	 */
	public Searcher(Logger l)
	{
		logger = l;
		searchMarke = new SearchMarke(logger);
		searchName = new SearchName(logger);
		searchTacho = new SearchTacho(logger);
	}

	/**
	 * Startet die Suche und fragt nach welcher Eigenschaft gesucht werden soll
	 * 
	 * @param list   Die Liste welche durchsucht werden soll
	 * @param option Wonach gesucht werden soll
	 * @param value  Das Object wonach gesucht werden soll
	 */
	public void search(CarList list, int option, Object value)
	{

		while (true)
		{
			switch (option)
			{
			case NAME:
				searchName.search(value, list);
				break;
			case MARKE:
				searchMarke.search(value, list);
				break;
			case Tacho:
				searchTacho.search(value, list);
				break;
			default:
				logger.info("Suche abgebrochen");
				return;
			}
			break;
		}
	}

}
