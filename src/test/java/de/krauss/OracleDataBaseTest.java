package de.krauss;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.krauss.handler.OracleDataBase;

public class OracleDataBaseTest
{
	private OracleDataBase orb;
	private Car car;
	private static final String CAR_NAME = "NAME", CAR_MARKE = "MARKE";
	private static final int CAR_TACHO = 1, CAR_ID = 200;
	private Reservierung res;

	@Before
	public void init()
	{
		orb = new OracleDataBase(Logger.getLogger(Launcher.class));
		car = new Car();

		car.setCAR_ID(CAR_ID);
		car.setF_Marke(CAR_MARKE);
		car.setF_Name(CAR_NAME);
		car.setF_Tacho(CAR_TACHO);

		res = new Reservierung();
		res.setResStart(new Date());
		res.setResStop(new Date());
		res.setID(CAR_ID);
	}

	@Test
	public void test()
	{
		Assert.assertTrue(orb.delteAllDataFromBase());

		Assert.assertTrue(orb.addCar(car));
		Assert.assertTrue(orb.addCar(car));

		Assert.assertTrue(orb.uploadRes(res));
		Assert.assertTrue(orb.uploadRes(res));
		Car car2 = orb.loadDatabase().get(0);
		Assert.assertEquals(car.getF_Name(), car2.getF_Name());
		Assert.assertEquals(car.getF_Marke(), car2.getF_Marke());
		Assert.assertEquals(car.getF_Tacho(), car2.getF_Tacho());

		Assert.assertTrue(orb.deleteReservierung(res));

		Assert.assertTrue(orb.deleteCarFromDatabase(CAR_ID));

		Assert.assertTrue(orb.closeConnection());

		Assert.assertFalse(orb.delteAllDataFromBase());

		Assert.assertFalse(orb.addCar(car));

		Assert.assertFalse(orb.uploadRes(res));
		Assert.assertFalse(orb.deleteReservierung(res));

		Assert.assertFalse(orb.deleteCarFromDatabase(CAR_ID));

		Assert.assertFalse(orb.closeConnection());
	}

}
