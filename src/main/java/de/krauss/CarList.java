package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "Fuhrpark")
@XmlAccessorType(XmlAccessType.NONE)
public class CarList
{
	@XmlElement(name = "carlist")
	private ArrayList<Car> cars;
	private Logger logger = Logger.getLogger(CarList.class);

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
	 * Listet alle Fahrzeuge nacheinander in der Konsole auf
	 */
	public void listCars()
	{
		int num = 1;
		int rnum = 1;

		logger.info("Anzahl an Autos: " + getList().size());
		logger.info("--------------------------------------");

		for (Car c : getList())
		{
			logger.info("Auto " + num);
			logger.info("-Fahrzeugname: " + c.getF_Name());
			logger.info("--Fahrzeugmarke: " + c.getF_Marke());
			logger.info("---Kilometerstand: " + c.getF_Tacho());

			if (c.getReservs().size() == 0)
			{
				logger.info("Keine Reservierungen vorhanden.");
			} else
			{
				logger.info("Reservierungen:");
				for (Reservierung r : c.getReservs())
				{
					logger.info("[" + rnum + "] " + Utilities.format(r.getResStart()) + " -------> "
							+ Utilities.format(r.getResStop()));
					rnum++;
				}
				rnum = 1;
			}

			logger.info("--------------------------------------");
			num++;
		}

	}

	/**
	 * Erstellt ein Auto und fügt der Cars-Arraylist das Auto hinzu
	 * 
	 * @param r    Der Reader mit welchen die Usereingaben gelesen werden können
	 * @param orcb Die Datenbank in welcher das Auto hinzufügen soll
	 * @return Ob das Auto hinzufügen erfolgreich war
	 */
	public boolean Autohinzufügen(BufferedReader r, OracleDataBase orcb)
	{
		Car newCar = new Car();
		try
		{

			logger.info("Wie lautet der Fahrzeugname?");

			newCar.setF_Name(r.readLine());
			logger.info("Fahrzeugname --> " + newCar.getF_Name());

			logger.info("Wie lautet die Fahrzeugmarke?");

			newCar.setF_Marke(r.readLine());
			logger.info("Fahrzeugmarke --> " + newCar.getF_Marke());

			newCar.setF_Tacho(Utilities.addTacho(r));
			logger.info("Kilometer: " + newCar.getF_Tacho());

			cars.add(newCar);
			orcb.addCar(newCar);
			loadCarsFromDataBase(orcb);
			System.err.println("Auto hinzugefügt!");
			logger.info("");
			System.err.flush();
			return true;
		} catch (IOException e)
		{
			logger.info("Fehler beim Lesen der User-Eingabe");
			return false;
		}
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

	/**
	 * Reserviert ein Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @param orcb   Die Datenbank in welche die REservierung hinzugefügt werden
	 *               soll
	 * @return Ob das reservieren erfolgreich war
	 */
	public boolean reservieren(BufferedReader reader, OracleDataBase orcb)
	{
		listCars();
		Car resCar = new Car();

		if (getList().size() == 0)
			return false;

		while (true)
		{

			logger.info("Welches Auto wollen sie Reservieren? (1 - " + getList().size() + ")");
			while (true)
			{
				try
				{
					resCar = getList().get((Integer.parseInt(reader.readLine()) - 1));
					break;
				} catch (Exception e)
				{
					logger.warn("Bitte eine vorhandene Zahl angeben!");
				}

			}

			Reservierung res = Reservierung.createReservierung(reader, resCar);

			if (res == null)
				return false;

			res.setRES_ID(-1);
			resCar.addResv(res);
			orcb.uploadRes(res);
			logger.info("Reserviert!");

			return true;
		}
	}

	/**
	 * Ließt alle Autos in die Datenbank ein
	 * 
	 * @param orcb
	 */
	public void loadCarsFromDataBase(OracleDataBase orcb)
	{
		cars = orcb.loadDatabase();
	}
}
