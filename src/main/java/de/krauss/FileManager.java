package de.krauss;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.handler.DumpFileHandler;
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
	private Logger logger;

	/**
	 * 
	 * @param option Die Methode mit welcher eingelesen werden soll
	 * @param f      Die Datei aus welcher eingelesen werden soll
	 * @return Die eingelesene Arraylist
	 */
	public ArrayList<Car> load(int option, File f)
	{
		switch (option)
		{
		case DUMP_FILE:
			return dumpFileHandler.load(f);

		case JAXB_FILE:
			return jaxbFileHandler.load(f);

		case TXT_FILE:
			return txtFileHandler.load(f);

		case XSTREAM_FILE:
			return xStreamFileHandler.load(f);
		case JSON_FILE:
			return jSonFileHandler.load(f);
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param cars   Die Instanz der Klasse in welcher die Arraylist gespeichert ist
	 * @param option Mit welcher Methode die Arraylist gespeichert werden soll
	 * @param l      Der Logger mit welchem man dem User antworten kann
	 */
	public void safe(CarList cars, int option, Logger l)
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
			l.warn("Fahrzeuge speichern wurde abgebrochen");
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
	public FileManager(Logger l)
	{
		logger = l;

		dumpFileHandler = new DumpFileHandler();
		jaxbFileHandler = new JAXBFileHandler();
		txtFileHandler = new TxtFileHandler();
		xStreamFileHandler = new XStreamFileHandler();
		jSonFileHandler = new JSonFileHandler();

	}
}
