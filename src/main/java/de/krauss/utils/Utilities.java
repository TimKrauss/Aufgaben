package de.krauss.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.car.Reservierung;
import de.krauss.gfx.ALLINONEFrameController;

public class Utilities
{
	public static final String sdf_Pattern = "dd.MM.yyyy (HH:mm)";
	private static SimpleDateFormat sdf = new SimpleDateFormat(sdf_Pattern);
	private static Logger logger = Logger.getLogger("System");

	/**
	 * Guckt ob das Auto zu der Zeit noch frei ist und gibt, falls nicht, eine
	 * Fehlermeldung auf dem Frame aus
	 * 
	 * @param start_Date Anfang der neuen Reservierung
	 * @param stop_Date  Ende der neuen Reservierung
	 * @param car        Das Auto welches zu der Zeit frei seien soll
	 * @param controll   Zum Ausgeben der ErrorMeldung auf dem GUI
	 * @return Ob das Auto zu der Zeit schon reserviert ist
	 */
	public static boolean isCarAvaible(Date start_Date, Date stop_Date, Car car, ALLINONEFrameController controll)
	{
		boolean onAddResvController = true;
		boolean existsError = false;
		String messageToDisplay = "";

		if (controll == null)
		{
			onAddResvController = false;
		}

		for (Reservierung resv : car.getReservs())
		{
			Date oldStart = resv.getResStart();
			Date oldStop = resv.getResStop();

			if (oldStart.equals(start_Date))
			{
				messageToDisplay = "Das Startdatum ist identisch mit dem einer anderen Reservierung";
				existsError = true;
			}

			if (start_Date.before(new Date()))
			{
				messageToDisplay = "Keine Reservierungen in der Vergangenheit möglich";
				existsError = true;
			}

			if (start_Date.after(stop_Date))
			{
				messageToDisplay = "Start Datum nach Stop Datum";
				existsError = true;
			}

			if (start_Date.after(oldStart) && start_Date.before(oldStop))
			{
				messageToDisplay = "StartDatum liegt zwischen einer anderen Reservierung";
				existsError = true;
			}

			if (stop_Date.after(oldStart) && stop_Date.before(oldStop))
			{
				messageToDisplay = "StopDatum liegt zwischen einer anderen Reservierung";
				existsError = true;
			}

			if (start_Date.before(oldStart) && stop_Date.after(oldStop))
			{
				messageToDisplay = "Zeitraum überschneidet sich mit einer anderen Reservierung";
				existsError = true;
			}
		}

		if (existsError)
		{
			if (onAddResvController)
			{
				if (controll != null)
					controll.showErrorMessage(messageToDisplay);
			} else
			{
				logger.warn(messageToDisplay);
			}
			return false;
		}
		return true;
	}

	/**
	 * Sorgt für eine Fehlerfreie Eingabe des Tachostandes
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Den Tachostand als Double
	 */
	public static int addTacho(BufferedReader reader)
	{
		int kilo = 0;

		while (true)
		{
			try
			{
				String line = reader.readLine();
				if (line == null)
				{
					logger.fatal("Keine Zeile mehr zu lesen");
					return 0;
				}

				kilo = Integer.parseInt(line);
				return kilo;
			} catch (NumberFormatException e)
			{
				logger.warn("Bitte gib eine gültige Kilometerzahl ein!");
			} catch (IOException e)
			{
				logger.fatal("Fehler beim Lesen der User-Eigabe");
			}
		}

	}

	/**
	 * 
	 * @param inputReader Der InputStream, welchen der Reader lesen soll
	 * @return Den funktionierende Reader
	 */
	public static BufferedReader createReader(Reader inputReader, Logger fileLogger)
	{

		// Überschreibe die Readline
		class OverWrittenReader extends BufferedReader
		{
			BufferedReader reader = new BufferedReader(inputReader);

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
	 * Sorgt für eine fehlerfreie Eingabe eines Datums
	 * 
	 * @param reader Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Das Datum welches der User eingegeben hat
	 */
	public static Date enterDate(BufferedReader reader)
	{
		while (true)
		{
			try
			{
				String string = reader.readLine();

				DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				df.setLenient(false);

				df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

				Date date = df.parse(string);

				if (date.before(new Date()))
				{
					logger.warn("Kein Datum in der Vergangenheit bitte!");
				} else
				{
					return date;
				}

			} catch (ParseException e)
			{
				logger.warn("Bitte geben sie ein gültiges Datum ein! (dd.MM.yyyy HH:mm)");
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
	 * 
	 * @param date Das zu formatierende Datum
	 * @return Das formatierte Datum
	 */
	public static String format(Date date)
	{
		return sdf.format(date);
	}

	/**
	 * Sucht aus der Carliste ein Auto aus
	 * 
	 * @param carlist Liste von der das Autos ausgesucht werden kann
	 * @param reader  Reader zum Lesen der Usereingabe
	 * @return Das ausgewählte Auto
	 */
	public static Car chooseCarFromList(CarList carlist, BufferedReader reader)
	{
		while (true)
		{
			try
			{

				int choo = Integer.parseInt(reader.readLine());
				if (choo > carlist.getList().size())
				{
					throw new NumberFormatException();
				}
				return carlist.getList().get(choo - 1);

			} catch (NumberFormatException e)
			{
				logger.warn("Bitte eine gültige Zahl eingeben");
			} catch (IOException e)
			{
				logger.fatal(e.getMessage());
			}
		}
	}

}
