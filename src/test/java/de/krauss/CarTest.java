package de.krauss;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarTest
{
	private Car car;

	private static final int TACHO = 10;
	private static final String MARKE = "VW", NAME = "AUTO";
	private static final Date start = new Date();
	private static final Date stop = new Date();
	private static final int CAR_ID = 5;

	@Before
	public void init()
	{
		car = new Car();
	}

	@Test
	public void test()
	{
		car.setF_Tacho(TACHO);
		Assert.assertEquals(TACHO + "", car.getF_Tacho() + "");

		car.setF_Marke(MARKE);
		Assert.assertEquals(MARKE, car.getF_Marke());

		car.setF_Name(NAME);
		Assert.assertEquals(NAME, car.getF_Name());

		Reservierung reservierung = new Reservierung(start, stop);
		car.addResv(reservierung);
		ArrayList<Reservierung> resv = new ArrayList<>();
		resv.add(reservierung);
		Assert.assertEquals(resv, car.getReservs());

		Assert.assertTrue(car.isReservedOn(start));
		Assert.assertTrue(car.isReservedOn(stop));

		car.setCAR_ID(CAR_ID);
		Assert.assertEquals(CAR_ID, car.getCAR_ID());

		car.addResv(reservierung);
	}
}
