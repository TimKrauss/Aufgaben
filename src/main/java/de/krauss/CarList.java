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
	private OracleDataBase orcb;

	public void setOrcb(OracleDataBase orcb)
	{
		this.orcb = orcb;
	}

	private Logger logger = Logger.getLogger("System");

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
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Ob das Auto hinzufügen erfolgreich war
	 */
	public boolean addCarWithReader(BufferedReader reader)
	{
		Car newCar = new Car();
		try
		{

			logger.info("Wie lautet der Fahrzeugname?");

			newCar.setCarName(reader.readLine());
			logger.info("Fahrzeugname --> " + newCar.getCarName());

			logger.info("Wie lautet die Fahrzeugmarke?");

			newCar.setCarMarke(reader.readLine());
			logger.info("Fahrzeugmarke --> " + newCar.getCarMarke());

			logger.info("Wie viele Kilometer hat das Auto auf dem Buckel?");
			newCar.setCarTacho(Utilities.addTacho(reader));
			logger.info("Kilometer: " + newCar.getCarTacho());

			addCar(newCar);

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
	 * @param carToAdd Das Auto welches hinzugefügt werden soll
	 */
	public void addCar(Car carToAdd)
	{
		if (carToAdd == null)
		{
			logger.warn("Auto == null");
			return;
		}

		if (carToAdd.getCarName() == null || carToAdd.getCarName().equals(""))
		{
			logger.warn("Füge Auto nicht hinzu da es keinen Namen hat");
		} else
		{
			cars.add(carToAdd);
			orcb.addCar(carToAdd);
		}

	}

	/**
	 * 
	 * @param idFromCar Die Nummer des Autos welches zurück gegeben werden soll
	 * @return Gibt das ausgewählte Auto zurück
	 */
	public Car getCar(int idFromCar)
	{
		Car car = null;
		try
		{
			car = cars.get(idFromCar);
		} catch (IndexOutOfBoundsException e)
		{
			logger.warn(e.getMessage());
		}
		return car;
	}

	public void addReservierung(Reservierung reservierung, Car car)
	{
		reservierung.setCarID(car.getCarID());

		orcb.uploadRes(reservierung);
		car.addResv(reservierung);
	}

	public void deleteCar(Car carToDelete)
	{
		// DATENBANK
		orcb.deleteCarFromDatabase(carToDelete.getCarID());

		// LOKAL
		cars.remove(carToDelete);
	}

	/**
	 * Reserviert ein Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
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

			res.setResvID(-1);

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

	/**
	 * 
	 * @return Gibt die Instanz der OracleDataBase zurück
	 */
	public OracleDataBase getOracleDatabase()
	{
		return orcb;
	}

	/**
	 * 
	 * @return Gibt die ArrayList, welche die Autos beinhaltet, zurück
	 */
	public ArrayList<Car> getList()
	{
		return cars;
	}

	/**
	 * Löscht die Reservierung von einem Auto
	 * 
	 * @param fromCar  Car welches die Reservierung momentan beinhaltet
	 * @param toDelete Die Reservierung welche gelöscht werden soll
	 */
	public void deleteReservierungFromCar(Car fromCar, Reservierung toDelete)
	{
		orcb.deleteReservierung(toDelete);
		fromCar.getReservs().remove(toDelete);
	}

	/**
	 * Fügt eine ArrayList mit Autos zu der Carlist hinzu
	 * 
	 * @param newCars Die Liste welche die neuen Autos beinhaltet
	 */
	public void addCars(ArrayList<Car> newCars)
	{
		for (Car car : newCars)
		{
			addCar(car);
		}
	}

	/**
	 * Löscht alle Einträge aus der Datenbank
	 */
	public void deleteEverything()
	{
		orcb.delteAllDataFromBase();
		cars.removeAll(cars);
	}

}
