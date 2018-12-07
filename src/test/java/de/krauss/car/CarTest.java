package de.krauss.car;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.krauss.car.Car;
import de.krauss.car.Reservierung;

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
		car.setCarTacho(TACHO);
		Assert.assertEquals(TACHO + "", car.getCarTacho() + "");

		car.setCarMarke(MARKE);
		Assert.assertEquals(MARKE, car.getCarMarke());

		car.setCarName(NAME);
		Assert.assertEquals(NAME, car.getCarName());

		Reservierung reservierung = new Reservierung(start, stop);
		car.addResv(reservierung);
		ArrayList<Reservierung> resv = new ArrayList<>();
		resv.add(reservierung);
		Assert.assertEquals(resv, car.getReservs());

		car.setCarID(CAR_ID);
		Assert.assertEquals(CAR_ID, car.getCarID());

		car.addResv(reservierung);
	}
}
