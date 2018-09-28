package de.krauss;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest
{
	private Car car;
	private ArrayList<Car> list;
	private Reservierung RES;

	private static final String NAME = "JUnit_Name", MARKE = "JUnit_Marke";
	private static final int Tacho = 10, CAR_ID = 5;

	@Before
	public void init()
	{
		car = new Car();
		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(Tacho);
		car.setCAR_ID(CAR_ID);

		RES = new Reservierung(new Date(), new Date());
		RES.setCarID(CAR_ID);
	}

	@Test
	public void test()
	{

		Car c = list.get(list.size() - 1);

		Assert.assertEquals(NAME, c.getF_Name());
		Assert.assertEquals(MARKE, c.getF_Marke());
		Assert.assertEquals((int) Tacho, (int) car.getF_Tacho());

	}
}
