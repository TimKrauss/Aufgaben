package de.krauss.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;

public class SearchName
{
	private ArrayList<Car> results = new ArrayList<>();
	private Logger logger;

	public SearchName(Logger l)
	{
		logger = l;
	}

	public void search(Object value, CarList list)
	{
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
				results.add(c);
			}
			inList++;
		}

		if (!foundOne)
		{
			logger.info("Kein Auto mit diesem Suchkriterium gefunden");
			logger.info("---------------------------------------");
		}
	}

	public void resetResults()
	{
		results.removeAll(results);
	}

	public ArrayList<Car> getResults()
	{
		return results;
	}
}
