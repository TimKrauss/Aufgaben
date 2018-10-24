package de.krauss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.krauss.search.Searcher;

public class SearcherTest
{
	private Searcher search;
	private Car c;

	private String NAME = "NAME", MARKE = "MARKE";
	private int TACHO = 1;

	@Before
	public void init()
	{
		search = new Searcher();
		c = new Car();
		c.setF_Name(NAME);
		c.setF_Marke(MARKE);
		c.setF_Tacho(TACHO);
		c.setCAR_ID(search.getDataBase().getNextCarID());
		search.getDataBase().addCar(c);
	}

	@Test
	public void test()
	{
		search.search(Searcher.NAME, NAME);
		search.search(Searcher.MARKE, MARKE);
		search.search(Searcher.Tacho, String.valueOf(TACHO));
	}

	@After
	public void tearDown()
	{
		search.getDataBase().deleteCarFromDatabase(c.getCAR_ID());
		search.getDataBase().closeConnection();
	}

}
