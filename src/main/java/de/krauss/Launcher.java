package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;

import javax.activity.InvalidActivityException;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import de.krauss.gfx.MainFrameController;
import de.krauss.search.Searcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Launcher extends Application implements Serializable
{
	private static final long serialVersionUID = 1636324934042718631L;

	@XmlElement(name = "carlist")
	private CarList carlist;

	private Logger logger = Logger.getLogger("System");
	private Logger fileLogger = Logger.getLogger("Usereingabe");

	public static final String HOME_DIR = System.getProperty("user.home") + "/Desktop/Cars/";
	private Searcher searcher;
	private MainFrameController controller;
	private Thread userReaderThread;
	private FileManager fm;

	/**
	 * Kümmert sich um die Eingabe des User in der Konsole // ICH WEIß DU WIRST
	 * MECKERN
	 * 
	 * @param reader ließt die Usereingabe
	 */
	public void handleUserInpunt(BufferedReader reader)
	{
		String txt = "";

		try
		{
			txt = reader.readLine();
			fileLogger.info(txt);
		} catch (IOException e1)
		{
			logger.fatal(e1.getMessage());
			System.exit(1);
		}

		switch (txt)
		{
		case "ja":
			carlist.addCarWithReader(reader);
			updateList();
			standardCall();
			break;
		case "nein":
			logger.info("Dann eben nicht :(");
			break;
		case "list":
			carlist.listCars();
			updateList();
			standardCall();
			break;
		case "reservieren":
			carlist.reservieren(reader);
			updateList();
			standardCall();
			break;
		case "del":
			int counter = 1;

			if (carlist.getList().size() == 0)
			{
				logger.warn("Kein Auto vorhanden!");
				return;
			}

			for (Car c : carlist.getList())
			{
				logger.info("[" + counter + "] " + c.getCarName());
				counter++;
			}

			logger.info("Welches Auto soll gelöscht werden?");

			Car carToDelete = Utilities.chooseCarFromList(carlist, reader);
			// LOKAL + DATENBANK
			carlist.deleteCar(carToDelete);

			updateList();
			standardCall();

			break;
		case "rdel":
			rLöschen(reader);
			updateList();
			standardCall();
			break;

		case "search":
			logger.info("Nach welchem Merkmal möchten sie suchen?");
			logger.info("[+" + Searcher.NAME + "] Name");
			logger.info("[+" + Searcher.MARKE + "] Marke");
			logger.info("[+" + Searcher.Tacho + "] Tacho");

			int choose = 0;

			while (true)
			{
				try
				{
					choose = Integer.parseInt(reader.readLine());

					if (choose > 0 || choose < 4)
					{
						switch (choose)
						{
						case Searcher.NAME:
							logger.info("Bitte geben sie den Namen des Fahrzeuges an:");
							searcher.search(choose, reader.readLine());
							break;
						case Searcher.MARKE:
							logger.info("Bitte geben sie die Marke des Fahrzeuges an:");
							searcher.search(choose, reader.readLine());
							break;
						case Searcher.Tacho:
							logger.info("Bitte geben sie den Kilometerstand an:");
							searcher.search(choose, reader.readLine());
							break;
						default:
							break;
						}

						break;
					}
					new InvalidActivityException();
				} catch (NumberFormatException e)
				{
					logger.fatal("Bitte eine Zahl ohne Buchstaben eingeben");
				} catch (InvalidActivityException e)
				{
					logger.fatal("Bitte eine gültige Zahl eingeben");
				} catch (IOException e)
				{
					logger.fatal("Reader hat Probleme beim Lesen der UserEingabe");
				}

			}

			standardCall();
			break;
		default:
			logger.info("Ungültige Eingabe");
			break;

		}

	}

	/**
	 * 
	 * @param in Der InputStream, welchen der Reader lesen soll
	 * @return Den funktionierende Reader
	 */
	public BufferedReader createReader(Reader in)
	{
		BufferedReader r = new BufferedReader(in);
		return r;
	}

	/**
	 * Startet das Programm
	 * 
	 * @param args Die Start-Paramter
	 */
	public static void main(String[] args)
	{
		Application.launch(args);
	}

	/**
	 * Gibt die ganzen Standardfragen nacheinadner aus
	 * 
	 * @return Ob das Ausgeben erfolgreich war
	 */
	public boolean standardCall()
	{
		logger.info("Ein weiteres Auto hinzufügen? (ja oder nein)");
		logger.info("Bereits hinzugefügte Autos ansehen? (list)");
		logger.info("Ein bereits hinzugefügtes Auto reservieren? (reservieren)");
		logger.info("Eine bereits hinzugefügte Reservierung löschen? (rdel)");
		logger.info("Vorhandene Autos durchsuchen? (search)");
		return true;
	}

	/**
	 * Entfernt eine Reservierung von einem Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Ob das löschen erfolgreich war
	 */
	public boolean rLöschen(BufferedReader reader)
	{
		ArrayList<Car> resCars = new ArrayList<>();
		int resnum = 1;
		Car carToDelete = null;
		int rnum = 1;
		int resvNummerChoosen = 0;

		for (Car c : carlist.getList())
		{
			if (c.getReservs().size() != 0)
			{
				logger.info("( " + resnum + " ) " + c.getCarName());
				resCars.add(c);
				resnum++;
			}
		}

		if (resCars.size() == 0)
		{
			System.err.println("Kein Auto mit Reservierungen vorhanden!");
			return false;
		}

		logger.info("Von welchem Auto wollen sie die Reservierung löschen? (Zahl eingeben)");

		try
		{
			carToDelete = resCars.get(Integer.parseInt(reader.readLine()) - 1);

		} catch (Exception e)
		{
			logger.fatal(e.getMessage());
			return false;
		}

		logger.info("Name: " + carToDelete.getCarName());

		for (Reservierung r : carToDelete.getReservs())
		{
			logger.info("[" + rnum + "] " + Utilities.format(r.getResStart()) + " -------> "
					+ Utilities.format(r.getResStop()));
			rnum++;
		}

		logger.info("Welche von den Reservierungen wollen sie löschen? (1 - " + (rnum - 1) + ")");

		while (true)
		{
			try
			{
				resvNummerChoosen = Integer.parseInt(reader.readLine()) - 1;
				break;
			} catch (NumberFormatException e)
			{
				logger.warn("Bitte geben sie eine gültige Zahl ein!");
			} catch (IOException e)
			{
				logger.fatal(e.getMessage());
				System.exit(1);
			}
		}

		carlist.deleteReservierungFromCar(carToDelete, carToDelete.getReservs().get(resvNummerChoosen));

		logger.info("Die Reservierung wurde gelöscht");
		return true;
	}

	/**
	 * Startet einen Reader für Konsolen eingaben
	 * 
	 * @param in der InputStream von welchem gelewesen werden soll
	 */
	public void startReaderThread(InputStream in)
	{
		userReaderThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Erstellt den ReaderThread, der die Eingaben des Users liest
				BufferedReader reader = createReader(new InputStreamReader(in));

				// Gibt dem Kunden ein das Willkommen aus
				logger.info("Hallo Lieber Kunde!");
				logger.info("Möchten sie ein Auto anlegen? (ja oder nein)");
				logger.info("Oder bereits hinzugefügte Autos ansehen? (list)");

				// Endlosschleife welche die Eingaben bearbeitet
				while (true)
				{
					handleUserInpunt(reader);
				}
			}
		});
		// Startet den Thread zum Lesen
		userReaderThread.start();
	}

	/**
	 * Startet das Programm und startet den Thread so wie das MainframeWindow
	 * 
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		fileLogger.getName().replaceAll("fileLogger", "Usereingabe");

		// Initialisiere FileManager
		fm = new FileManager();

		// Initialisiere Searcher
		searcher = new Searcher();

		// Initalisiere CarList
		carlist = new CarList();
		carlist.addCarsFromDataBase();

		// Starte Konsolen-Eingabe-Thread
		startReaderThread(System.in);

		// START & INIT WINDOW
		if (primaryStage == null)
		{
			return;
		}
		controller = MainFrameController.createWindow();
		controller.setCarlist(carlist);
		controller.setFileManager(fm);
		controller.init();
		controller.setList(carlist.getList());
	}

	/**
	 * Updatet die im Frame angezeigte Liste im FX-Thread
	 */
	private void updateList()
	{
		// Mit diesem Befehl läuft die Operation über den FX-Thread, so dass Fehler
		// vermieden werden
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				controller.setList(carlist.getList());
			}
		});
	}

}
