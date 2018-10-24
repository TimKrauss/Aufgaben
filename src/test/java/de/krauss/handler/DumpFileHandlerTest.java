package de.krauss.handler;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Car;
import de.krauss.CarList;

public class DumpFileHandlerTest
{
	private DumpFileHandler dumpFileHandler = new DumpFileHandler();

	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
	private static final File existButNotArrayList = new File(
			System.getProperty("user.home") + "/Desktop/Cars/Cars.txt");

	@Test
	public void test()
	{
		car.setCarName(NAME);
		car.setCarMarke(MARKE);
		car.setCarTacho(TACHO);
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));
		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());

		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());
		Assert.assertNotNull(dumpFileHandler.load(dumpFileHandler.getDefaultFile()));
		Assert.assertNull(dumpFileHandler.load(new File("")));
		Assert.assertNull(dumpFileHandler.load(existButNotArrayList));
	}

}
