package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

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
	private FileManager fileManager;
	private UserHandler userHandler;
	private OracleDataBase dataBase = new OracleDataBase();

	/**
	 * 
	 * @param inputReader Der InputStream, welchen der Reader lesen soll
	 * @return Den funktionierende Reader
	 */
	public BufferedReader createReader(Reader inputReader)
	{
		BufferedReader reader = new BufferedReader(inputReader);

		// �berschreibe die Readline
		class Test extends BufferedReader
		{
			public Test(Reader inputReader)
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
		Test test = new Test(inputReader);
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
		logger.info("Ein weiteres Auto hinzuf�gen? (ja oder nein)");
		logger.info("Bereits hinzugef�gte Autos ansehen? (list)");
		logger.info("Ein bereits hinzugef�gtes Auto reservieren? (reservieren)");
		logger.info("Eine bereits hinzugef�gte Reservierung l�schen? (rdel)");
		logger.info("Vorhandene Autos durchsuchen? (search)");
		return true;
	}

	/**
	 * Startet einen Reader f�r Konsolen eingaben
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
				logger.info("M�chten sie ein Auto anlegen? (ja oder nein)");
				logger.info("Oder bereits hinzugef�gte Autos ansehen? (list)");

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
	 * @return Gibt die Instanz dieses Launchers zur�ck
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

		// Initialisiere FileManager
		fileManager = new FileManager();

		// Initialisiere Searcher
		searcher = new Searcher();
		searcher.setOrcb(dataBase);

		// Initalisiere CarList
		carlist = new CarList();
		carlist.setOrcb(dataBase);
		carlist.addCarsFromDataBase();

		// Starte Konsolen-Eingabe-Thread
		startReaderThread(System.in);

		if (primaryStage == null)
			return;

		// START & INIT WINDOW
		controller = MainFrameController.createWindow();
		controller.setCarlist(carlist);
		controller.setFileManager(fileManager);
		controller.init();
	}

	/**
	 * Updatet die im Frame angezeigte Liste im FX-Thread
	 */
	public void updateList()
	{
		// Mit diesem Befehl l�uft die Operation �ber den FX-Thread, so dass Fehler
		// vermieden werden
		try
		{
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					controller.setList(carlist.getList());
				}
			});
		} catch (IllegalStateException e)
		{
			logger.error(e.getMessage());
		}
	}

}
