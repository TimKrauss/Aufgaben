package de.krauss.handler;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.Reservierung;

public class TxtFileHandlerTest
{
	private TxtFileHandler dumpFileHandler = new TxtFileHandler();

	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
//	private static final File EXISTBUTNOTARRAYLIST = NEW FILE(
//			System.getProperty("USER.HOME") + "/DESKTOP/CARS/CARS.TXT");

	@Test
	public void test()
	{

		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(TACHO);
		car.addResv(new Reservierung(new Date(), new Date()));
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));
		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());

		Assert.assertNotNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Object")));
		Assert.assertNotNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Cars.txt")));
		Assert.assertNull(dumpFileHandler.load(new File("")));

	}

}
