package de.krauss;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest
{
	private FileManager fileManager;
	private Logger logger = Logger.getLogger(Launcher.class);
	private Car car;
	private ArrayList<Car> list;
	private Reservierung RES;

	private static final String NAME = "JUnit_Name", MARKE = "JUnit_Marke";
	private static final int Tacho = 10, CAR_ID = 5;

	@Before
	public void init()
	{
		fileManager = new FileManager(logger);
		car = new Car();
		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(Tacho);
		car.setCAR_ID(CAR_ID);

		RES = new Reservierung(new Date(), new Date());
		RES.setID(CAR_ID);
	}

	@Test
	public void test()
	{
		fileManager.safeCarToDB(car);
		list = fileManager.loadDatabase();

		Car c = list.get(list.size() - 1);

		Assert.assertEquals(NAME, c.getF_Name());
		Assert.assertEquals(MARKE, c.getF_Marke());
		Assert.assertEquals((int) Tacho, (int) car.getF_Tacho());

		Assert.assertTrue(fileManager.uploadRes(RES));
		Assert.assertTrue(fileManager.deleteReservierung(RES));

		Assert.assertTrue(fileManager.delDatabase(CAR_ID));
	}
}
