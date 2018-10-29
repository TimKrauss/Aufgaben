package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import de.krauss.gfx.AddResvController;

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
	 * @param c          Das Auto welches zu der Zeit frei seien soll
	 * @param controll   Zum Ausgeben der ErrorMeldung auf dem GUI
	 * @return Ob das Auto zu der Zeit schon reserviert ist
	 */
	public static boolean isCarAvaible(Date start_Date, Date stop_Date, Car c, AddResvController controll)
	{

		for (Reservierung r : c.getReservs())
		{
			Date oldStart = r.getResStart();
			Date oldStop = r.getResStop();

			if (oldStart.equals(start_Date))
			{
				controll.showErrorMessage("Das Startdatum ist identisch mit dem einer anderen Reservierung");
				return false;
			}

			if (start_Date.before(new Date()))
			{
				controll.showErrorMessage("Keine Reservierungen in der Vergangenheit möglich");
				return false;
			}

			if (start_Date.after(stop_Date))
			{
				controll.showErrorMessage("Start Datum nach Stop Datum");
				return false;
			}

			if (start_Date.after(oldStart) && start_Date.before(oldStop))
			{
				controll.showErrorMessage("StartDatum liegt zwischen einer anderen Reservierung");
				return false;
			}

			if (stop_Date.after(oldStart) && stop_Date.before(oldStop))
			{
				controll.showErrorMessage("StopDatum liegt zwischen einer anderen Reservierung");
				return false;
			}

			if (start_Date.before(oldStart) && stop_Date.after(oldStop))
			{
				controll.showErrorMessage("Zeitraum überschneidet sich mit einer anderen Reservierung");
				return false;
			}
		}
		return true;
	}

	/**
	 * Guckt ob das Auto zu der Zeit noch frei ist
	 * 
	 * @param start_Date Anfang der neuen Reservierung
	 * @param stop_Date  Ende der neuen Reservierung
	 * @param c          Das Auto welches zu der Zeit frei seien soll
	 * @return Ob das Auto zu der Zeit schon reserviert ist
	 */
	public static boolean isCarAvaible(Date start_Date, Date stop_Date, Car c)
	{
		if (start_Date == null)
		{
			logger.fatal("Start-Date ist Null");
			return false;
		}

		for (Reservierung r : c.getReservs())
		{
			Date oldStart = r.getResStart();
			Date oldStop = r.getResStop();

			if (start_Date.before(new Date()))
			{
				logger.error("Keine Reservierungen in der Vergangenheit möglich");
				return false;
			}

			if (start_Date.after(stop_Date))
			{
				logger.error("Start Datum nach Stop Datum");
				return false;
			}

			if (start_Date.after(oldStart) && start_Date.before(oldStop))
			{
				logger.error("StartDatum liegt zwischen einer anderen Reservierung");
				return false;
			}

			if (stop_Date.after(oldStart) && stop_Date.before(oldStop))
			{
				logger.error("StopDatum liegt zwischen einer anderen Reservierung");
				return false;
			}

			if (start_Date.before(oldStart) && stop_Date.after(oldStop))
			{
				logger.error("Zeitraum überschneidet sich mit einer anderen Reservierung");
				return false;
			}
		}
		return true;
	}

	/**
	 * Sorgt für eine Fehlerfreie Eingabe des Tachostandes
	 * 
	 * @param r Der Reader mit welchen die Usereingaben gelesen werden können
	 * @return Den Tachostand als Double
	 */
	public static int addTacho(BufferedReader r)
	{
		logger.info("Wie viele Kilometer hat das Auto auf dem Buckel?");

		int kilo = 0;

		while (true)
		{
			try
			{
				String line = r.readLine();
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
				String s = reader.readLine();

				DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				df.setLenient(false);

				df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

				Date f = df.parse(s);

				if (f.before(new Date()))
				{
					logger.warn("Kein Datum in der Vergangenheit bitte!");
				} else
				{
					return f;
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
	 * @param d Das zu formatierende Datum
	 * @return Das formatierte Datum
	 */
	public static String format(Date d)
	{
		return sdf.format(d);
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
