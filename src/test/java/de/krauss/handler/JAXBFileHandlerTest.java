package de.krauss.handler;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.krauss.Launcher;
import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.utils.OracleDataBase;

public class JAXBFileHandlerTest
{
	private JAXBFileHandler dumpFileHandler = new JAXBFileHandler();
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

		dumpFileHandler.safe(carlist, new File(""));
		dumpFileHandler.safe(carlist, dumpFileHandler.getDefaultFile());

		Assert.assertNull(dumpFileHandler.load(new File(Launcher.HOME_DIR + "Object")));
		Assert.assertNull(dumpFileHandler.load(new File("")));
		Assert.assertNull(dumpFileHandler.load(existButNotArrayList));

		orcb.closeConnection();
	}

}
