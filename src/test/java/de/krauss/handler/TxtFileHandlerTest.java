package de.krauss.handler;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Launcher;
import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.car.Reservierung;
import de.krauss.utils.OracleDataBase;

public class TxtFileHandlerTest
{
	private TxtFileHandler dumpFileHandler = new TxtFileHandler();
	private CarList carlist = new CarList();
	private Car car = new Car();
	private static final String NAME = "NAME", MARKE = "MARKE";
	private static final int TACHO = 5;
//	private static final File EXISTBUTNOTARRAYLIST = NEW FILE(
//			System.getProperty("USER.HOME") + "/DESKTOP/CARS/CARS.TXT");
	private OracleDataBase orcb = new OracleDataBase();

	@Test
	public void test()
	{
		carlist.setOrcb(orcb);
		car.setCarName(NAME);
		car.setCarMarke(MARKE);
		car.setCarTacho(TACHO);
		car.addResv(new Reservierung(new Date(), new Date()));
		carlist.addCar(car);

		dumpFileHandler.safe(carlist, null);
		dumpFileHandler.safe(carlist, new File(""));
		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());

		Assert.assertNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Object")));
		Assert.assertNotNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Cars.txt")));
		Assert.assertNull(dumpFileHandler.load(new File("")));
	}

}
