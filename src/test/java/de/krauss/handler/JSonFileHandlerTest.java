package de.krauss.handler;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.utils.OracleDataBase;

public class JSonFileHandlerTest
{
	private JSonFileHandler dumpFileHandler = new JSonFileHandler();
	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
	private static final File existButNotArrayList = new File(
			System.getProperty("user.home") + "/Desktop/Cars/Cars.txt");

	private OracleDataBase orcb = new OracleDataBase();

	@Test
	public void test()
	{
		carlist.setOrcb(orcb);
		car.setCarName(NAME);
		car.setCarMarke(MARKE);
		car.setCarTacho(TACHO);
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());
		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));

		Assert.assertNull(dumpFileHandler.load(new File("")));
		Assert.assertNull(dumpFileHandler.load(existButNotArrayList));

		ArrayList<Car> cars = dumpFileHandler.load(dumpFileHandler.getDefaultFile());
		Assert.assertNotNull(cars);

		orcb.closeConnection();
	}

}
