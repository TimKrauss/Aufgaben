package de.krauss;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest
{
	private Car car;
	private Reservierung RES;
	private FileManager fm;
	private static final String NAME = "JUnit_Name", MARKE = "JUnit_Marke";
	private static final int Tacho = 10, CAR_ID = 5;
	private CarList list;

	@Before
	public void init()
	{
		car = new Car();
		car.setF_Name(NAME);
		car.setF_Marke(MARKE);
		car.setF_Tacho(Tacho);
		car.setCAR_ID(CAR_ID);

		list = new CarList();
		list.addCar(car);

		RES = new Reservierung(new Date(), new Date());
		RES.setCarID(CAR_ID);

		fm = new FileManager();
	}

	@Test
	public void test()
	{

		fm.safe(list, FileManager.DUMP_FILE);
		fm.safe(list, FileManager.JAXB_FILE);
		fm.safe(list, FileManager.TXT_FILE);
		fm.safe(list, FileManager.JSON_FILE);
		fm.safe(list, FileManager.XSTREAM_FILE);

		fm.detectOption(fm.getDefaultFile(FileManager.TXT_FILE));
		fm.detectOption(fm.getDefaultFile(FileManager.DUMP_FILE));
		fm.detectOption(fm.getDefaultFile(FileManager.JSON_FILE));
		fm.detectOption(fm.getDefaultFile(FileManager.XSTREAM_FILE));

		fm.safe(list, FileManager.DUMP_FILE, fm.getDefaultFile(FileManager.DUMP_FILE));
		fm.safe(list, FileManager.JAXB_FILE, fm.getDefaultFile(FileManager.JAXB_FILE));
		fm.safe(list, FileManager.TXT_FILE, fm.getDefaultFile(FileManager.TXT_FILE));
		fm.safe(list, FileManager.JSON_FILE, fm.getDefaultFile(FileManager.JSON_FILE));
		fm.safe(list, FileManager.XSTREAM_FILE, fm.getDefaultFile(FileManager.XSTREAM_FILE));

		Assert.assertNull(fm.load(FileManager.DUMP_FILE, new File(""), list));
		Assert.assertNull(fm.load(FileManager.JAXB_FILE, new File(""), list));
		Assert.assertNull(fm.load(FileManager.JSON_FILE, new File(""), list));
		Assert.assertNull(fm.load(FileManager.TXT_FILE, new File(""), list));
		Assert.assertNull(fm.load(FileManager.XSTREAM_FILE, new File(""), list));

		Assert.assertNotNull(fm.load(FileManager.DUMP_FILE, fm.getDefaultFile(FileManager.DUMP_FILE), list));
		Assert.assertNotNull(fm.load(FileManager.JAXB_FILE, fm.getDefaultFile(FileManager.JAXB_FILE), list));
		Assert.assertNotNull(fm.load(FileManager.JSON_FILE, fm.getDefaultFile(FileManager.JSON_FILE), list));
		Assert.assertNotNull(fm.load(FileManager.TXT_FILE, fm.getDefaultFile(FileManager.TXT_FILE), list));
		Assert.assertNotNull(fm.load(FileManager.XSTREAM_FILE, fm.getDefaultFile(FileManager.XSTREAM_FILE), list));

	}
}
