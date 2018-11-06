package de.krauss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.krauss.search.Searcher;

public class SearcherTest
{
	private Searcher searcher;
	private Car car;

	private String NAME = "NAME", MARKE = "MARKE";
	private int TACHO = 1;
	private OracleDataBase orcb = new OracleDataBase();

	@Before
	public void init()
	{
		searcher = new Searcher();
		searcher.setOrcb(orcb);
		car = new Car();
		car.setCarName(NAME);
		car.setCarMarke(MARKE);
		car.setCarTacho(TACHO);
		car.setCarID(searcher.getDataBase().getNextCarID());
		searcher.getDataBase().addCar(car);
	}

	@Test
	public void test()
	{
		searcher.search(Searcher.NAME, NAME);
		searcher.search(Searcher.MARKE, MARKE);
		searcher.search(Searcher.Tacho, String.valueOf(TACHO));
	}

	@After
	public void tearDown()
	{
		searcher.getDataBase().deleteCarFromDatabase(car.getCarID());
		searcher.getDataBase().closeConnection();
	}

}
