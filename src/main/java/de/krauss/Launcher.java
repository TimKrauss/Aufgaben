package de.krauss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.activity.InvalidActivityException;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import de.krauss.gfx.MainFrameController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Launcher extends Application implements Serializable
{

	private static final long serialVersionUID = 1636324934042718631L;

	@XmlElement(name = "carlist")
	private CarList carlist;
	static Logger logger = Logger.getLogger(Launcher.class);
	private FileManager fm;
	public static final String HOME_DIR = System.getProperty("user.home") + "/Desktop/Cars/";
	private Searcher searcher;
	private static BufferedReader userReader;
	private MainFrameController controller;
	private Thread userReaderThread;

	/**
	 * Kümmert sich um die Eingabe des User in der Konsole
	 * 
	 * @param reader ließt die Usereingabe
	 */
	public void handleUserInpunt(BufferedReader reader)
	{
		String txt = "";
		try
		{
			txt = reader.readLine();
		} catch (IOException e1)
		{
			e1.printStackTrace();
			System.exit(1);
		}

		switch (txt)
		{
		case "ja":
			Autohinzufügen(reader);
			standardCall();
			break;
		case "nein":
			logger.info("Dann eben nicht :(");
			break;
		case "list":
			listCars();
			standardCall();
			break;
		case "reservieren":
			reservieren(reader);
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
				logger.info("[" + counter + "] " + c.getF_Name());
				counter++;
			}

			logger.info("Welches Auto soll gelöscht werden?");
			int id = 0;
			while (true)
			{
				try
				{

					int choo = Integer.parseInt(reader.readLine());
					if (choo > carlist.getList().size())
					{
						new Exception();
					}

					id = carlist.getList().get(choo - 1).getCAR_ID();
					break;

				} catch (NumberFormatException e)
				{
					logger.warn("Bitte eine gültige Zahl eingeben");
				} catch (IOException e)
				{
					logger.fatal(e.getMessage());
				}
			}
			fm.delDatabase(id);
			syncDatabase();
			standardCall();
			break;
		case "rdel":
			rLöschen(reader);
			standardCall();
			break;

		case "search":
			logger.info("Nach welchem Merkmal möchten sie suchen?");
			logger.info("[1] Name");
			logger.info("[2] Marke");
			logger.info("[3] Tachostand");

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
							searcher.search(carlist, choose, reader.readLine());
							break;
						case Searcher.MARKE:
							logger.info("Bitte geben sie die Marke des Fahrzeuges an:");
							searcher.search(carlist, choose, reader.readLine());
							break;
						case Searcher.Tacho:
							logger.info("Bitte geben sie den Kilometerstand an:");
							searcher.search(carlist, choose, reader.readLine());
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

	BufferedReader createReader(Reader in)
	{
		BufferedReader r = new BufferedReader(in);
		userReader = r;
		return r;
	}

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
//		logger.info("Die vorhandenen Autos speichern? (safe)");
//		logger.info("Vorhandene Autos aus einlesen? (load)");
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
		Car dCar = null;
		int rnum = 1;
		int resvNummerChoosen = 0;

		for (Car c : carlist.getList())
		{
			if (c.getReservs().size() != 0)
			{
				logger.info("( " + resnum + " ) " + c.getF_Name());
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
			dCar = resCars.get(Integer.parseInt(reader.readLine()) - 1);

		} catch (Exception e)
		{
			logger.fatal(e.getMessage());
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

		logger.info("Name: " + dCar.getF_Name());

		for (Reservierung r : dCar.getReservs())
		{
			logger.info("[" + rnum + "] " + sdf.format(r.getResStart()) + " -------> " + sdf.format(r.getResStop()));
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

		fm.deleteReservierung(dCar.getReservs().get(resvNummerChoosen));

		dCar.getReservs().remove(resvNummerChoosen);
		logger.info("Die Reservierung wurde gelöscht");
		return true;
	}

	/**
	 * Reserviert ein Fahrzeug
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Ob das reservieren erfolgreich war
	 */
	public boolean reservieren(BufferedReader reader)
	{
		Reservierung r = new Reservierung();
		Car resCar = new Car();

		listCars();

		while (true)
		{

			try
			{
				logger.info("Welches Auto wollen sie Reservieren? (1 - " + carlist.getList().size() + ")");
				while (true)
				{
					try
					{
						resCar = carlist.getList().get((Integer.parseInt(reader.readLine()) - 1));
						break;
					} catch (Exception e)
					{
						logger.warn("Bitte eine vorhandene Zahl angeben!");
						return false;
					}

				}

				logger.info("Wann soll die Reservierung starten? (tt.MM.yyyy HH:mm)");

				while (true)
				{
					Date start = enterDate(reader);
					if (resCar.isReservedOn(start))
					{
						logger.error("Auto zu der Zeit leider reserviert....");
						logger.info("Bitte wählen sie ein anderes Datum");
					} else
					{
						r.setResStart(start);
						break;
					}
				}

				logger.info("Wann soll die Reservierung enden? (tt.MM.yyyy HH:mm)");

				while (true)
				{
					Date stop = enterDate(reader);
					if (resCar.isReservedOn(stop))
					{
						logger.error("Auto zu der Zeit leider reserviert....");
						logger.info("Bitte wählen sie ein anderes Datum");
					} else if (r.getResStart().after(stop))
					{
						logger.info("Ungültiger Zeitraum! (Startdatum ist nach Stopdatum)");
					} else
					{
						r.setResStop(stop);
						break;
					}
				}
				r.setID(resCar.getCAR_ID());
				resCar.addResv(r);
				fm.uploadRes(r);
				logger.info("Reserviert!");

				// logger.info("Vorhandene Autos aus einer TXT-Datei einlesen? (load)");
				return true;
			} catch (NumberFormatException e)
			{
				logger.error("Bitte geben sie eine vorhandene Zahl ein! " + e.getMessage());
			}
		}
	}

	/**
	 * Sorgt für eine fehlerfreie Eingabe eines Datums
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Das Datum welches der User eingegeben hat
	 */
	public Date enterDate(BufferedReader reader)
	{
		while (true)
		{
			try
			{
				String s = reader.readLine();

				DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				df.setLenient(false);

				df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

				Date f = df.parse(s);

				if (f.before(new Date()))
				{
					logger.info("Kein Datum in der Vergangenheit bitte!");
				} else
				{
					return f;
				}

			} catch (ParseException e)
			{
				logger.info("Bitte geben sie ein gültiges Datum ein! (dd.MM.yyyy HH:mm)");
			} catch (IOException e)
			{
				logger.fatal("Fehler beim Lesen der User-Eingabe");
			} catch (NullPointerException e)
			{
				logger.fatal(e.getMessage());
				return null;
			}

		}

	}

	/**
	 * Listet alle Fahrzeuge nacheinander in der Konsole auf
	 */
	public void listCars()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");
		int num = 1;
		int rnum = 1;

		logger.info("Anzahl an Autos: " + carlist.getList().size());
		logger.info("--------------------------------------");

		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

		for (Car c : carlist.getList())
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
					logger.info("[" + rnum + "] " + sdf.format(r.getResStart()) + " -------> "
							+ sdf.format(r.getResStop()));
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
	 * @param r Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Ob das Auto hinzufügen erfolgreich war
	 */
	public boolean Autohinzufügen(BufferedReader r)
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

			newCar.setF_Tacho(addTacho(r));
			logger.info("Kilometer: " + newCar.getF_Tacho());

			carlist.getList().add(newCar);
			fm.safeCarToDB(newCar);
			syncDatabase();
			controller.setList(carlist.getList());
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
	 * Sorgt für eine Fehlerfreie Eingabe des Tachostandes
	 * 
	 * @param r Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Den Tachostand als Double
	 */
	private int addTacho(BufferedReader r)
	{
		logger.info("Wie viele Kilometer hat das Auto auf dem Buckel?");

		int kilo;

		while (true)
		{
			try
			{

				String line = r.readLine();
				if (line == null)
				{
					logger.fatal("Keine Zeile mehr zu lesen");
					System.exit(1);
				}

				kilo = Integer.parseInt(line);
				return kilo;
			} catch (NumberFormatException e)
			{
				logger.info("Bitte gib eine gültige Kilometerzahl ein!");
			} catch (IOException e)
			{
				logger.fatal("Fehler beim Lesen der User-Eigabe");
			}
		}

	}

	/**
	 * Ersetzt die lokale Liste durch eine neu eingelesene aus der Datenbank
	 */
	private boolean syncDatabase()
	{
		carlist.setCars(fm.loadDatabase());
		return true;
	}

	/**
	 * 
	 * @return Den Leser, zum lesen der Usereingabe
	 */
	public BufferedReader getUserReader()
	{
		return userReader;
	}

	public void startReaderThread()
	{
		userReaderThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				userReader = createReader(new InputStreamReader(System.in));
				logger.info("Hallo Lieber Kunde!");
				logger.info("Möchten sie ein Auto anlegen? (ja oder nein");
				logger.info("Oder bereits hinzugefügte Autos ansehen? (list)");

				while (true)
				{
					handleUserInpunt(getUserReader());
				}
			}
		});
		userReaderThread.start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{

		fm = new FileManager(logger);
		searcher = new Searcher(logger);
		carlist = new CarList();

		carlist.setCars(fm.loadDatabase());

		// START WINDOW

		if (primaryStage == null)
		{
			startReaderThread();
			return;
		}

		FXMLLoader loader = new FXMLLoader();

		File f = new File(Launcher.class.getResource("/de/krauss/gfx/MainFrame.fxml").getFile());
		System.out.println(f.getAbsolutePath());
		FileInputStream fis = new FileInputStream(f);
		AnchorPane pane = loader.load(fis);
		primaryStage.setScene(new Scene(pane));
		controller = loader.getController();
		controller.setDatenbankStatus(true, fm.getDataBaseUser());
		controller.setCarlist(carlist);
		controller.setFileManager(fm);
		primaryStage.setTitle("Fuhrpark");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				System.exit(1);
			}
		});
		primaryStage.show();

		controller.init();
		controller.setList(carlist.getList());

		// STOP WINDOW
		fis.close();
		startReaderThread();
	}

	public void setController(MainFrameController mainFrameController)
	{
		controller = mainFrameController;
	}

}
