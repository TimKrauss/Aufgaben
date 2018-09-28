package de.krauss;

import org.apache.log4j.Logger;

public class Searcher
{
	public static final int NAME = 1, MARKE = 2, Tacho = 3;
	private Logger logger;
	private CarList list;

	/**
	 * 
	 * @param l Der Logger zum Ausgeben in der Konsole
	 */
	public Searcher(Logger l)
	{
		logger = l;
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

		this.list = list;

		while (true)
		{
			switch (option)
			{
			case NAME:
				searchName(value);
				break;
			case MARKE:
				searchMarke(value);
				break;
			case Tacho:
				searchTacho(value);
				break;
			default:
				logger.info("Suche abgebrochen");
				return;
			}
			break;
		}
	}

	/**
	 * Durchsucht alle Autos und gibt die mit der richtigen Marke aus
	 */
	private void searchMarke(Object value)
	{
		logger.info("Nach welcher Marke soll gesucht werden?");

		logger.info("");

		boolean foundOne = false;
		int inList = 1;

		String marke = "";

		if (value instanceof String)
		{
			marke = (String) value;
		} else
		{
			logger.fatal("Objekt ist kein String");
			return;
		}

		logger.info("---------------------------------------");

		for (Car c : list.getList())
		{
			if (c.getF_Marke().equals(marke))
			{
				if (!foundOne)
					foundOne = true;

				logger.info("Name: " + c.getF_Name());
				logger.info("Marke: " + c.getF_Marke());
				logger.info("Tachostand: " + c.getF_Tacho());
				logger.info("Dieses Auto ist die Nummer " + inList + "!");
				logger.info("---------------------------------------");
			}
			inList++;
		}

		if (!foundOne)
		{
			logger.info("Kein Auto mit diesem Suchkriterium gefunden");
			logger.info("---------------------------------------");
		}

	}

	/**
	 * Durchsucht alle Autos und gibt die mit dem richtigen Tachostand aus
	 */
	private void searchTacho(Object value)
	{
		logger.info("Nach welchem Tachostand soll gesucht werden?");

		logger.info("");

		boolean foundOne = false;
		int inList = 1;

		int tacho = 0;

		if (value instanceof String)
		{
			try
			{
				tacho = Integer.parseInt((String) value);
			} catch (Exception e)
			{
				logger.fatal("Keine gültige Zahl");
				return;
			}
		} else
		{
			logger.fatal("Ungültiges Objekt");
			return;
		}

		logger.info("---------------------------------------");

		for (Car c : list.getList())
		{
			if (tacho == c.getF_Tacho())
			{
				if (!foundOne)
					foundOne = true;

				logger.info("Name: " + c.getF_Name());
				logger.info("Marke: " + c.getF_Marke());
				logger.info("Tachostand: " + c.getF_Tacho());
				logger.info("Dieses Auto ist die Nummer " + inList + "!");
				logger.info("---------------------------------------");
			}
			inList++;
		}

		if (!foundOne)
		{
			logger.info("Kein Auto mit diesem Suchkriterium gefunden");
			logger.info("---------------------------------------");
		}

	}

	/**
	 * Durchsucht alle Autos und gibt die mit dem richtigen Namen aus
	 */
	private void searchName(Object value)
	{
		logger.info("Nach welchem Namen soll gesucht werden?");

		logger.info("");

		boolean foundOne = false;
		int inList = 1;

		String name = "";

		if (value instanceof String)
		{
			name = (String) value;
		} else
		{
			logger.fatal("Objekt ist kein String");
			return;
		}

		logger.info("---------------------------------------");

		for (Car c : list.getList())
		{
			if (c.getF_Name().equals(name))
			{
				if (!foundOne)
					foundOne = true;

				logger.info("Name: " + c.getF_Name());
				logger.info("Marke: " + c.getF_Marke());
				logger.info("Tachostand: " + c.getF_Tacho());
				logger.info("Dieses Auto ist die Nummer " + inList + "!");
				logger.info("---------------------------------------");
			}
			inList++;
		}

		if (!foundOne)
		{
			logger.info("Kein Auto mit diesem Suchkriterium gefunden");
			logger.info("---------------------------------------");
		}
	}
}
