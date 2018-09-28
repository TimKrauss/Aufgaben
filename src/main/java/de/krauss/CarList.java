package de.krauss;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Fuhrpark")
@XmlAccessorType(XmlAccessType.NONE)
public class CarList
{
	@XmlElement(name = "carlist")
	private ArrayList<Car> cars;

	/**
	 * Log4j logger
	 */

	/**
	 * Konstruktor in welchem die Cars-Arraylist gespeichert wird
	 */
	public CarList()
	{
		cars = new ArrayList<Car>();
	}

	/**
	 * Ersetzt die vorhandene Arraylist durch eine neue
	 * 
	 * @param c Die neue Arraylist
	 */
	public void setCars(ArrayList<Car> c)
	{
		cars = c;
	}

	/**
	 * Fügt der Arraylist ein Auto hinzu
	 * 
	 * @param c Das Auto welches hinzugefügt werden soll
	 */
	public void addCar(Car c)
	{
		cars.add(c);
	}

	/**
	 * 
	 * @param i Die Nummer des Autos welches zurück gegeben werden soll
	 * @return Gibt das ausgewählte Auto zurück
	 */
	public Car getCar(int i)
	{
		return cars.get(i);
	}

	/**
	 * 
	 * @return Die Arraylist in der sich alle Autos befinden
	 */
	public ArrayList<Car> getList()
	{
		return cars;
	}
}
