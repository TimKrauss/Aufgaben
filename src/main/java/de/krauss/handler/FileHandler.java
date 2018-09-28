package de.krauss.handler;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public abstract class FileHandler
{
	protected Logger logger = Logger.getLogger(Launcher.class);

	/**
	 * 
	 * @param f Das File von welchem die Arraylist eingelesen werden soll
	 * @return Die eingelesene Arraylist
	 */
	public abstract ArrayList<Car> load(File f);

	/**
	 * 
	 * @param cars Die Instanz der Klasse in welcher aus welcher die Arraylist
	 *             gespeichert werden soll
	 * @param f    Die Datei in welcher die Arraylist gespeichert werden soll
	 */
	public abstract void safe(CarList cars, File f);

	/**
	 * Nimmt die Datein aus dem Cars-Ordner vom Desktop
	 * 
	 * @return Das Standard-File zum lesen und speichern
	 */
	public abstract File getDefaultFile();
}
