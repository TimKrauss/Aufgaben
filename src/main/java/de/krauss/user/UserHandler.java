package de.krauss.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.Reservierung;
import de.krauss.Utilities;
import de.krauss.search.Searcher;
import javafx.application.Platform;

public class UserHandler
{
	private CarList carlist;
	private Searcher searcher;
	private Logger logger = Logger.getLogger("System");

	/**
	 * K�mmert sich um die Eingabe des User in der Konsole
	 * 
	 * @param reader   lie�t die Usereingabe
	 * @param launcher F�r die Standard-Ausgaben
	 */
	public void handleUserInpunt(BufferedReader reader, Launcher launcher)
	{
		String txt = "";

		try
		{
			txt = reader.readLine();
		} catch (IOException e1)
		{
			logger.fatal(e1.getMessage());
			System.exit(1);
		}

		switch (txt)
		{
		case "ja":
			carlist.addCarWithReader(reader);
			break;
		case "nein":
			logger.info("Dann eben nicht :(");
			break;
		case "list":
			carlist.listCars();
			break;
		case "reservieren":
			carlist.reservieren(reader);
			break;
		case "del":
			deleteCar(reader);
			break;
		case "rdel":
			reservierungL�schen(reader);
			break;

		case "search":
			searcher.searchWithReader(reader);
			break;
		default:
			logger.info("Ung�ltige Eingabe");
			break;
		}
		launcher.standardCall();
		launcher.updateList();
	}

	/**
	 * Entfernt eine Reservierung von einem Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden k�nnen
	 * @return Ob das l�schen erfolgreich war
	 */
	public boolean reservierungL�schen(BufferedReader reader)
	{
		ArrayList<Car> resCars = new ArrayList<>();
		int resnum = 1;
		Car carToDelete = null;
		int rnum = 1;
		int resvNummerChoosen = 0;

		for (Car car : carlist.getList())
		{
			if (car.getReservs().size() != 0)
			{
				logger.info("( " + resnum + " ) " + car.getCarName());
				resCars.add(car);
				resnum++;
			}
		}

		if (resCars.size() == 0)
		{
			System.err.println("Kein Auto mit Reservierungen vorhanden!");
			return false;
		}

		logger.info("Von welchem Auto wollen sie die Reservierung l�schen? (Zahl eingeben)");

		try
		{
			carToDelete = resCars.get(Integer.parseInt(reader.readLine()) - 1);

		} catch (Exception e)
		{
			logger.fatal(e.getMessage());
			return false;
		}

		logger.info("Name: " + carToDelete.getCarName());

		for (Reservierung resv : carToDelete.getReservs())
		{
			logger.info("[" + rnum + "] " + Utilities.format(resv.getResStart()) + " -------> "
					+ Utilities.format(resv.getResStop()));
			rnum++;
		}

		logger.info("Welche von den Reservierungen wollen sie l�schen? (1 - " + (rnum - 1) + ")");

		while (true)
		{
			try
			{
				resvNummerChoosen = Integer.parseInt(reader.readLine()) - 1;
				break;
			} catch (NumberFormatException e)
			{
				logger.warn("Bitte geben sie eine g�ltige Zahl ein!");
			} catch (IOException e)
			{
				logger.fatal(e.getMessage());
				Platform.exit();
			}
		}

		carlist.deleteReservierungFromCar(carToDelete, carToDelete.getReservs().get(resvNummerChoosen));

		logger.info("Die Reservierung wurde gel�scht");
		return true;
	}

	private void deleteCar(BufferedReader reader)
	{
		int counter = 1;

		if (carlist.getList().size() == 0)
		{
			logger.warn("Kein Auto vorhanden!");
			return;
		}

		for (Car car : carlist.getList())
		{
			logger.info("[" + counter + "] " + car.getCarName());
			counter++;
		}

		logger.info("Welches Auto soll gel�scht werden?");

		Car carToDelete = Utilities.chooseCarFromList(carlist, reader);
		// LOKAL + DATENBANK
		carlist.deleteCar(carToDelete);

	}

	public void setCarlist(CarList car)
	{
		carlist = car;
	}

	public void setSearcher(Searcher sEARCHER)
	{
		searcher = sEARCHER;
	}

}
