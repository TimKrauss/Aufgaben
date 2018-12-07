package de.krauss.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.krauss.car.Car;
import de.krauss.car.Reservierung;
import de.krauss.utils.OracleDataBase;

public class OracleDataBaseTest
{
	private OracleDataBase orb;
	private Car car, car2;
	private static final String CAR_NAME = "NAME", CAR_MARKE = "MARKE";
	private static final int CAR_TACHO = 1, CAR_ID = 200;
	private Reservierung reservierung;

	@Before
	public void init()
	{
		orb = new OracleDataBase();
		car = new Car();
		car2 = new Car();

		car.setCarID(CAR_ID);
		car.setCarMarke(CAR_MARKE);
		car.setCarName(CAR_NAME);
		car.setCarTacho(CAR_TACHO);

		car2.setCarID(0);
		car2.setCarMarke(CAR_MARKE);
		car2.setCarName(CAR_NAME);
		car2.setCarTacho(CAR_TACHO);

		reservierung = new Reservierung();
		reservierung.setResStart(new Date());
		reservierung.setResStop(new Date());
		reservierung.setCarID(CAR_ID);
		reservierung.setOwner("Tim");
	}

	@Test
	public void test()
	{
		Assert.assertTrue(orb.delteAllDataFromBase());

		Assert.assertTrue(orb.addCar(car));
		Assert.assertTrue(orb.addCar(car2));

		Assert.assertTrue(orb.uploadRes(reservierung));
		Assert.assertTrue(orb.uploadRes(reservierung));
		Car car2 = orb.loadDatabase().get(0);
		Assert.assertEquals(car.getCarName(), car2.getCarName());
		Assert.assertEquals(car.getCarMarke(), car2.getCarMarke());
		Assert.assertEquals(car.getCarTacho(), car2.getCarTacho());

		Assert.assertTrue(orb.deleteReservierung(reservierung));

		Assert.assertTrue(orb.deleteCarFromDatabase(CAR_ID));

		Assert.assertTrue(orb.closeConnection());

		Assert.assertFalse(orb.delteAllDataFromBase());

		Assert.assertTrue(orb.addCar(car));

		Assert.assertFalse(orb.uploadRes(reservierung));
		Assert.assertFalse(orb.deleteReservierung(reservierung));

		Assert.assertFalse(orb.deleteCarFromDatabase(CAR_ID));
		orb.delteAllDataFromBase();
		Assert.assertFalse(orb.closeConnection());
	}

}
