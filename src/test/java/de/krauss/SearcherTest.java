package de.krauss;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SearcherTest
{
	private Searcher searcher;
	private Logger logger = Logger.getLogger(Launcher.class);
	private CarList list = new CarList();
	private static final String TACHO = "10";
	private static final int TACHO_INT = 10;
	private static final String WRONG_TACHO = "5";
	private static final String MARKE = "VW", NAME = "AUTO";
	private static final int CAR_ID = 5;

	@Before
	public void init()
	{
		searcher = new Searcher(logger);
		list = new CarList();

		Car c = new Car();
		c.setCAR_ID(CAR_ID);
		c.setF_Marke(MARKE);
		c.setF_Name(NAME);
		c.setF_Tacho(TACHO_INT);
		list.addCar(c);
	}

	@Test
	public void test()
	{
		searcher.search(list, Searcher.MARKE, MARKE);
		searcher.search(list, Searcher.MARKE, NAME);

		searcher.search(list, Searcher.NAME, MARKE);
		searcher.search(list, Searcher.NAME, NAME);

		searcher.search(list, Searcher.Tacho, TACHO);
		searcher.search(list, Searcher.Tacho, WRONG_TACHO);

		searcher.search(list, Searcher.Tacho, list);
		searcher.search(list, Searcher.Tacho, NAME);

		searcher.search(list, Searcher.NAME, list);
		searcher.search(list, Searcher.MARKE, list);

		searcher.search(list, TACHO_INT, MARKE);
	}
}
