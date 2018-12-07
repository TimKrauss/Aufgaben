package de.krauss;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import de.krauss.car.CarList;
import de.krauss.gfx.ALLINONEFrameController;
import de.krauss.gfx.LoginFrameController;
import de.krauss.search.Searcher;
import de.krauss.user.User;
import de.krauss.user.UserHandler;
import de.krauss.user.UserManager;
import de.krauss.utils.OracleDataBase;
import de.krauss.utils.Utilities;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application implements Serializable
{
	private static final long serialVersionUID = 1636324934042718631L;

	// TODO Konfigurations-Seite (Enorm Viel - KonfigKlasse+ObjektSerialisierung)
	// TODO IMPORT / EXPORT überarbeiten

	@XmlElement(name = "carlist")
	private CarList carlist;

	private Logger logger = Logger.getLogger("System");
	private Logger fileLogger = Logger.getLogger("Usereingabe");

	public static final String HOME_DIR = System.getProperty("user.home") + "/Desktop/Cars/";
	private Searcher searcher;
	private Thread userReaderThread;

	private UserHandler userHandler;
	private OracleDataBase dataBase = new OracleDataBase();
	private ALLINONEFrameController controller;
	private UserManager userManager;
	private User user;

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
	 * Startet das Programm und startet den Thread so wie das MainframeWindow
	 * 
	 * @throws Exception Falls beim Starten irgendwelche Fehler gemacht werden
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		// Initialisiere Searcher
		searcher = new Searcher();
		searcher.setOrcb(dataBase);

		// Init UserManager
		userManager = new UserManager();

		// Initalisiere CarList
		carlist = new CarList();
		carlist.setOrcb(dataBase);
		carlist.addCarsFromDataBase();

		// Starte Konsolen-Eingabe-Thread
		startReaderThread(System.in);

		if (primaryStage == null)
			return;

		// START & INIT WINDOW

		LoginFrameController loginFrameController = LoginFrameController.createWindow();
		loginFrameController.init(userManager, this, carlist, searcher);

	}

	/**
	 * Startet einen Reader für Konsolen eingaben
	 * 
	 * @param inputStream der InputStream von welchem gelewesen werden soll
	 */
	public void startReaderThread(InputStream inputStream)
	{
		userHandler = new UserHandler();

		userHandler.setCarlist(carlist);
		userHandler.setSearcher(searcher);

		userReaderThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Erstellt den ReaderThread, der die Eingaben des Users liest
				BufferedReader reader = Utilities.createReader(new InputStreamReader(inputStream), fileLogger);

				// Gibt dem Kunden ein das Willkommen aus
				logger.info("Hallo Lieber Kunde!");
				logger.info("Möchten sie ein Auto anlegen? (ja oder nein)");
				logger.info("Oder bereits hinzugefügte Autos ansehen? (list)");

				// Endlosschleife welche die Eingaben bearbeitet
				while (true)
				{
					userHandler.handleUserInpunt(reader, getLauncherInstanz());
				}
			}
		});
		// Startet den Thread zum Lesen
		userReaderThread.start();
	}

	/**
	 * Updatet die Liste im GUI
	 */
	public void updateList()
	{
		if (controller == null)
		{
			return;
		}

		controller.updateList();
	}

	public void startAllInOneFrame(CarList list, Searcher searcher2)
	{
		controller = ALLINONEFrameController.createWindow();
		controller.setOrcb(dataBase);
		controller.setUser(user);
		controller.init(list, searcher2);
	}

	// SETTER && GETTER

	/**
	 * 
	 * @return Gibt die Instanz dieses Launchers zurück
	 */
	private Launcher getLauncherInstanz()
	{
		return this;
	}

	public void setUser(User user2)
	{
		user = user2;
	}

	public ALLINONEFrameController getAllInOneFrameController()
	{
		return controller;
	}

}
