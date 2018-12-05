package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import de.krauss.gfx.ALLINONEFrameController;
import de.krauss.gfx.LoginFrameController;
import de.krauss.search.Searcher;
import javafx.application.Application;
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
	private Thread userReaderThread;

	private UserHandler userHandler;
	private OracleDataBase dataBase = new OracleDataBase();
	private ALLINONEFrameController controller;
	private UserManager userManager;
	private User user;

	/**
	 * 
	 * @param inputReader Der InputStream, welchen der Reader lesen soll
	 * @return Den funktionierende Reader
	 */
	public BufferedReader createReader(Reader inputReader)
	{
		BufferedReader reader = new BufferedReader(inputReader);

		// Überschreibe die Readline
		class OverWrittenReader extends BufferedReader
		{
			public OverWrittenReader(Reader inputReader)
			{
				super(inputReader);
			}

			@Override
			public String readLine()
			{
				try
				{
					String zeile = reader.readLine();
					fileLogger.info(zeile);
					return zeile;
				} catch (IOException e)
				{
					logger.warn(e.getLocalizedMessage());
				}
				return null;
			}

		}
		OverWrittenReader test = new OverWrittenReader(inputReader);
		return test;
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
				BufferedReader reader = createReader(new InputStreamReader(inputStream));

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
	 * 
	 * @return Gibt die Instanz dieses Launchers zurück
	 */
	private Launcher getLauncherInstanz()
	{
		return this;
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
		loginFrameController.init(userManager, this);

	}

	public void updateList()
	{
		if (controller == null)
		{
			return;
		}

		controller.updateList();
	}

	public void startAllInOneFrame()
	{
		controller = ALLINONEFrameController.createWindow();
		controller.setOrcb(dataBase);
		controller.setUser(user);
		controller.init(carlist, searcher);
	}

	public void setUser(User user2)
	{
		user = user2;
	}
}
