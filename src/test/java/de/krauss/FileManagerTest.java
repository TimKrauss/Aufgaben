package de.krauss;

import java.io.File;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest
{
	private Car car;
	private Reservierung resv;
	private FileManager fileManager;
	private static final String NAME = "JUnit_Name", MARKE = "JUnit_Marke";
	private static final int Tacho = 10, CAR_ID = 5;
	private CarList list;
	private OracleDataBase orcb = new OracleDataBase();

	@Before
	public void init()
	{
		car = new Car();
		car.setCarName(NAME);
		car.setCarMarke(MARKE);
		car.setCarTacho(Tacho);
		car.setCarID(CAR_ID);

		list = new CarList();
		list.setOrcb(orcb);
		list.addCar(car);

		resv = new Reservierung(new Date(), new Date());
		resv.setCarID(CAR_ID);

		fileManager = new FileManager();
	}

	@Test
	public void test()
	{

		fileManager.safe(list, FileManager.DUMP_FILE);
		fileManager.safe(list, FileManager.JAXB_FILE);
		fileManager.safe(list, FileManager.TXT_FILE);
		fileManager.safe(list, FileManager.JSON_FILE);
		fileManager.safe(list, FileManager.XSTREAM_FILE);

		fileManager.detectOption(fileManager.getDefaultFile(FileManager.TXT_FILE));
		fileManager.detectOption(fileManager.getDefaultFile(FileManager.DUMP_FILE));
		fileManager.detectOption(fileManager.getDefaultFile(FileManager.JSON_FILE));
		fileManager.detectOption(fileManager.getDefaultFile(FileManager.XSTREAM_FILE));

		fileManager.safe(list, FileManager.DUMP_FILE, fileManager.getDefaultFile(FileManager.DUMP_FILE));
		fileManager.safe(list, FileManager.JAXB_FILE, fileManager.getDefaultFile(FileManager.JAXB_FILE));
		fileManager.safe(list, FileManager.TXT_FILE, fileManager.getDefaultFile(FileManager.TXT_FILE));
		fileManager.safe(list, FileManager.JSON_FILE, fileManager.getDefaultFile(FileManager.JSON_FILE));
		fileManager.safe(list, FileManager.XSTREAM_FILE, fileManager.getDefaultFile(FileManager.XSTREAM_FILE));

		Assert.assertNull(fileManager.load(FileManager.DUMP_FILE, new File(""), list));
		Assert.assertNull(fileManager.load(FileManager.JAXB_FILE, new File(""), list));
		Assert.assertNull(fileManager.load(FileManager.JSON_FILE, new File(""), list));
		Assert.assertNull(fileManager.load(FileManager.TXT_FILE, new File(""), list));
		Assert.assertNull(fileManager.load(FileManager.XSTREAM_FILE, new File(""), list));

		Assert.assertNotNull(
				fileManager.load(FileManager.DUMP_FILE, fileManager.getDefaultFile(FileManager.DUMP_FILE), list));
		Assert.assertNotNull(
				fileManager.load(FileManager.JAXB_FILE, fileManager.getDefaultFile(FileManager.JAXB_FILE), list));
		Assert.assertNotNull(
				fileManager.load(FileManager.JSON_FILE, fileManager.getDefaultFile(FileManager.JSON_FILE), list));
		Assert.assertNotNull(
				fileManager.load(FileManager.TXT_FILE, fileManager.getDefaultFile(FileManager.TXT_FILE), list));
		Assert.assertNotNull(
				fileManager.load(FileManager.XSTREAM_FILE, fileManager.getDefaultFile(FileManager.XSTREAM_FILE), list));

	}

	@After
	public void tearDown()
	{
		orcb.closeConnection();
	}
}
