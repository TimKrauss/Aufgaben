package de.krauss.handler;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public class XStreamFileHandlerTest
{
	private XStreamFileHandler dumpFileHandler = new XStreamFileHandler();

	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
	private static final File existButNotArrayList = new File(
			System.getProperty("user.home") + "/Desktop/Cars/dumpfile");

	@Test
	public void test()
	{

		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(TACHO);
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));
		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());

		Assert.assertNull(dumpFileHandler.load(new File("")));
		Assert.assertNull(dumpFileHandler.load(existButNotArrayList));
		Assert.assertNotNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Cars.xml")));
	}

}
