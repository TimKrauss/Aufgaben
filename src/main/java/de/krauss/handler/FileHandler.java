package de.krauss.handler;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.car.Car;
import de.krauss.car.CarList;

public interface FileHandler
{
	Logger logger = Logger.getLogger("System");

	/**
	 * 
	 * @param f Das File von welchem die Arraylist eingelesen werden soll
	 * @return Die eingelesene Arraylist
	 */
	ArrayList<Car> load(File f);

	/**
	 * 
	 * @param cars Die Instanz der Klasse in welcher aus welcher die Arraylist
	 *             gespeichert werden soll
	 * @param f    Die Datei in welcher die Arraylist gespeichert werden soll
	 */
	void safe(CarList cars, File f);

	/**
	 * Nimmt die Datein aus dem Cars-Ordner vom Desktop
	 * 
	 * @return Das Standard-File zum lesen und speichern
	 */
	File getDefaultFile();
}
