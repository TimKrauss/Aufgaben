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
	private OracleDataBase orcb = new OracleDataBase();

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

		logger.info("Anzahl an Autos: " + cars.size());
		logger.info("--------------------------------------");

		for (Car c : cars)
		{
			logger.info("Auto " + num);
			logger.info("-Fahrzeugname: " + c.getCarName());
			logger.info("--Fahrzeugmarke: " + c.getCarMarke());
			logger.info("---Kilometerstand: " + c.getCarTacho());

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
	public boolean addCarWithReader(BufferedReader r)
	{
		Car newCar = new Car();
		try
		{

			logger.info("Wie lautet der Fahrzeugname?");

			newCar.setCarName(r.readLine());
			logger.info("Fahrzeugname --> " + newCar.getCarName());

			logger.info("Wie lautet die Fahrzeugmarke?");

			newCar.setCarMarke(r.readLine());
			logger.info("Fahrzeugmarke --> " + newCar.getCarMarke());

			newCar.setCarTacho(Utilities.addTacho(r));
			logger.info("Kilometer: " + newCar.getCarTacho());

			// LOKALE LISTE
			cars.add(newCar);

			// DATENBANK
			orcb.addCar(newCar);

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
		orcb.addCar(c);
	}

	/**
	 * 
	 * @param i Die Nummer des Autos welches zurück gegeben werden soll
	 * @return Gibt das ausgewählte Auto zurück
	 */
	public Car getCar(int i)
	{
		Car car = null;
		try
		{
			car = cars.get(i);
		} catch (IndexOutOfBoundsException e)
		{
			logger.warn(e.getMessage());
		}
		return car;
	}

	public void addReservierung(Reservierung r, Car c)
	{
		r.setCarID(c.getCarID());

		orcb.uploadRes(r);
		c.addResv(r);
	}

	public void deleteCar(Car c)
	{
		// DATENBANK
		orcb.deleteCarFromDatabase(c.getCarID());

		// LOKAL
		cars.remove(c);
	}

	/**
	 * Reserviert ein Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @param orcb   Die Datenbank in welche die REservierung hinzugefügt werden
	 *               soll
	 * @return Ob das reservieren erfolgreich war
	 */
	public boolean reservieren(BufferedReader reader)
	{
		listCars();
		Car resCar = new Car();

		if (cars.size() == 0)
			return false;

		while (true)
		{

			logger.info("Welches Auto wollen sie Reservieren? (1 - " + cars.size() + ")");
			while (true)
			{
				try
				{
					resCar = cars.get((Integer.parseInt(reader.readLine()) - 1));
					break;
				} catch (IOException | NumberFormatException e)
				{
					logger.warn("Bitte eine vorhandene Zahl angeben!");
				}

			}

			Reservierung res = Reservierung.createReservierung(reader, resCar);

			if (res == null)
				return false;

			res.setRES_ID(-1);

			// LOKAL
			resCar.addResv(res);
			// DATENBANK
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
	public void addCarsFromDataBase()
	{
		ArrayList<Car> addablesCars = new ArrayList<>();

		if (cars.size() == 0)
		{
			cars.addAll(orcb.loadDatabase());
			return;
		}

		for (Car alreadyIn : cars)
		{
			for (Car putIn : orcb.loadDatabase())
			{
				if (alreadyIn.getCarID() != putIn.getCarID())
				{
					addablesCars.add(putIn);
				} else
				{
					logger.info("Auto aus der Liste entfernt, da schon vorhanden (ID: " + putIn.getCarID() + " )");
				}
			}
		}
		logger.info(addablesCars.size());
		cars.addAll(addablesCars);

	}

	public OracleDataBase getOracleDatabase()
	{
		return orcb;
	}

	public ArrayList<Car> getList()
	{
		return cars;
	}

	public void deleteReservierungFromCar(Car fromCar, Reservierung toDelete)
	{
		orcb.deleteReservierung(toDelete);
		fromCar.getReservs().remove(toDelete);
	}

	public void addCars(ArrayList<Car> newCars)
	{
		for (Car car : newCars)
		{
			addCar(car);
		}
	}

}
