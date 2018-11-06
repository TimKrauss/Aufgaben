package de.krauss;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.handler.DumpFileHandler;
import de.krauss.handler.FileHandler;
import de.krauss.handler.JAXBFileHandler;
import de.krauss.handler.JSonFileHandler;
import de.krauss.handler.TxtFileHandler;
import de.krauss.handler.XStreamFileHandler;

public class FileManager implements Serializable
{

	private static final long serialVersionUID = -7316598012514218303L;

	public static final int DUMP_FILE = 1, JAXB_FILE = 2, TXT_FILE = 3, XSTREAM_FILE = 4, JSON_FILE = 5;

	/**
	 * Log4j logger
	 */

	private DumpFileHandler dumpFileHandler;
	private JAXBFileHandler jaxbFileHandler;
	private TxtFileHandler txtFileHandler;
	private XStreamFileHandler xStreamFileHandler;
	private JSonFileHandler jSonFileHandler;
	private Logger logger = Logger.getLogger("System");
	private ArrayList<FileHandler> handlerList;
	private ArrayList<Car> arrayListWithCars;

	/**
	 * Übergibt die Autos + Reservierungen OHNE RES_ID,CAR_ID,RES:CAR_ID
	 * 
	 * @param option    Die Methode mit welcher eingelesen werden soll
	 * @param file      Die Datei aus welcher eingelesen werden soll
	 * @param checkList Die Carlist welche überprüft werden soll
	 * @return Die eingelesene Arraylist
	 */
	public ArrayList<Car> load(int option, File file, CarList checkList)
	{
		switch (option)
		{
		case DUMP_FILE:
			arrayListWithCars = dumpFileHandler.load(file);
			break;
		case JAXB_FILE:
			arrayListWithCars = jaxbFileHandler.load(file);
			break;
		case TXT_FILE:
			arrayListWithCars = txtFileHandler.load(file);
			break;
		case XSTREAM_FILE:
			arrayListWithCars = xStreamFileHandler.load(file);
			break;
		case JSON_FILE:
			arrayListWithCars = jSonFileHandler.load(file);
			break;
		default:
			return null;
		}

		if (arrayListWithCars == null)
		{
			return null;
		}

		for (Car checkCar : checkList.getList())
		{
			for (Car newCar : arrayListWithCars)
			{
				if (checkCar.getCarName().equalsIgnoreCase(newCar.getCarName()))
				{
					logger.info("Gleicher Autoname gefunden! ( " + newCar.getCarName() + " )");
					logger.info("Dennoch wird das Auto erstmal hinzugefügt");
					// XXX Falls Verbesserung bei gleichem Namen
				}
			}
		}
		return arrayListWithCars;
	}

	/**
	 * 
	 * @param handler Der Handler von welchem das StandardFile zurückgegeben wird
	 * @return Das Standard-File für den Handler
	 */
	public File getDefaultFile(int handler)
	{
		switch (handler)
		{
		case DUMP_FILE:
			return dumpFileHandler.getDefaultFile();

		case JAXB_FILE:
			return jaxbFileHandler.getDefaultFile();

		case TXT_FILE:
			return txtFileHandler.getDefaultFile();

		case XSTREAM_FILE:
			return xStreamFileHandler.getDefaultFile();
		case JSON_FILE:
			return jSonFileHandler.getDefaultFile();
		default:
			return null;
		}
	}

	/**
	 * Speichert im Standard-File auf dem Desktop
	 * 
	 * @param cars   Die Instanz der Klasse in welcher die Arraylist gespeichert ist
	 * @param option Mit welcher Methode die Arraylist gespeichert werden soll
	 */
	public void safe(CarList cars, int option)
	{
		switch (option)
		{
		case DUMP_FILE:
			dumpFileHandler.safe(cars, null);
			break;
		case JAXB_FILE:
			jaxbFileHandler.safe(cars, null);
			break;
		case TXT_FILE:
			txtFileHandler.safe(cars, null);
			break;
		case XSTREAM_FILE:
			xStreamFileHandler.safe(cars, null);
			break;
		case JSON_FILE:
			jSonFileHandler.safe(cars, null);
			break;
		default:
			logger.warn("Fahrzeuge speichern wurde abgebrochen");
			return;
		}
	}

	/**
	 * Speichert alle Farhzeuge in einer Datei
	 * 
	 * @param cars   Liste der Autos welche gespeichert werden soll
	 * @param option Option mit welcher Methode die Autos gepseichert werden sollen
	 * @param file   Die Datei in welcher die Autos gespeichert werden sollen.s
	 */
	public void safe(CarList cars, int option, File file)
	{
		switch (option)
		{
		case DUMP_FILE:
			dumpFileHandler.safe(cars, file);
			break;
		case JAXB_FILE:
			jaxbFileHandler.safe(cars, file);
			break;
		case TXT_FILE:
			txtFileHandler.safe(cars, file);
			break;
		case XSTREAM_FILE:
			xStreamFileHandler.safe(cars, file);
			break;
		case JSON_FILE:
			jSonFileHandler.safe(cars, file);
			break;
		default:
			logger.warn("Fahrzeuge speichern wurde abgebrochen");
			return;
		}
	}

	/**
	 * Konstruktor zur Initalisierung der Handler
	 * 
	 */
	public FileManager()
	{

		dumpFileHandler = new DumpFileHandler();
		jaxbFileHandler = new JAXBFileHandler();
		txtFileHandler = new TxtFileHandler();
		xStreamFileHandler = new XStreamFileHandler();
		jSonFileHandler = new JSonFileHandler();

		arrayListWithCars = new ArrayList<>();
		handlerList = new ArrayList<>();

		// ALLE AUSSER JAXB
		handlerList.add(dumpFileHandler);
		handlerList.add(jSonFileHandler);
		handlerList.add(xStreamFileHandler);
		handlerList.add(xStreamFileHandler);
	}

	/**
	 * Gibt die Option aus mit welcher die Datei eingelesen werden soll
	 * 
	 * @param importFile Das zu überprüfende File
	 * @return Die Option womit das File eingelesen werden sollte
	 */
	public int detectOption(File importFile)
	{
		String extension = importFile.getName().split("\\.")[1];

		switch (extension)
		{
		case "xml":
			return FileManager.XSTREAM_FILE;
		case "json":
			return FileManager.JSON_FILE;
		case "dump":
			return FileManager.DUMP_FILE;
		case "txt":
			return FileManager.TXT_FILE;
		default:
			return FileManager.DUMP_FILE; // Meisten ObjectSerialization heißen nicht DUMP-File
		}

	}
}
