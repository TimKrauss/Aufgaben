package de.krauss.handler;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Car;
import de.krauss.CarList;

public class JSonFileHandlerTest
{
	private JSonFileHandler dumpFileHandler = new JSonFileHandler();

	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
	private static final File existButNotArrayList = new File(
			System.getProperty("user.home") + "/Desktop/Cars/Cars.txt");

	@Test
	public void test()
	{

		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(TACHO);
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());
		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));

		Assert.assertNull(dumpFileHandler.load(new File("")));
		Assert.assertNull(dumpFileHandler.load(existButNotArrayList));

		ArrayList<Car> cars = dumpFileHandler.load(dumpFileHandler.getDefaultFile());
		Assert.assertNull(cars);
	}

}
