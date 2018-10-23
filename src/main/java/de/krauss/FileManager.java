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
	private Logger logger = Logger.getLogger(FileManager.class);
	private ArrayList<FileHandler> handlerList;
	private ArrayList<Car> carList;

	/**
	 * 
	 * @param option Die Methode mit welcher eingelesen werden soll
	 * @param f      Die Datei aus welcher eingelesen werden soll
	 * @return Die eingelesene Arraylist
	 */
	public ArrayList<Car> load(int option, File f, OracleDataBase o)
	{
		switch (option)
		{
		case DUMP_FILE:
			carList = dumpFileHandler.load(f);
			break;
		case JAXB_FILE:
			carList = jaxbFileHandler.load(f);
			break;
		case TXT_FILE:
			carList = txtFileHandler.load(f);
			break;
		case XSTREAM_FILE:
			carList = xStreamFileHandler.load(f);
			break;
		case JSON_FILE:
			carList = jSonFileHandler.load(f);
			break;
		default:
			return null;
		}

		if (o != null)
		{
			for (Car c : carList)
			{
				o.addCar(c);
			}
		}
		return carList;
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
	 * @param f      Die Datei in welcher die Autos gespeichert werden sollen.s
	 */
	public void safe(CarList cars, int option, File f)
	{
		switch (option)
		{
		case DUMP_FILE:
			dumpFileHandler.safe(cars, f);
			break;
		case JAXB_FILE:
			jaxbFileHandler.safe(cars, f);
			break;
		case TXT_FILE:
			txtFileHandler.safe(cars, f);
			break;
		case XSTREAM_FILE:
			xStreamFileHandler.safe(cars, f);
			break;
		case JSON_FILE:
			jSonFileHandler.safe(cars, f);
			break;
		default:
			logger.warn("Fahrzeuge speichern wurde abgebrochen");
			return;
		}
	}

	/**
	 * Konstruktor zur Initalisierung der Handler
	 * 
	 * @param l Reader zum Lesen der Usereingaben
	 */
	public FileManager()
	{

		dumpFileHandler = new DumpFileHandler();
		jaxbFileHandler = new JAXBFileHandler();
		txtFileHandler = new TxtFileHandler();
		xStreamFileHandler = new XStreamFileHandler();
		jSonFileHandler = new JSonFileHandler();

		carList = new ArrayList<>();

		// ALLE AUSSER JAXB
		handlerList = new ArrayList<>();
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
