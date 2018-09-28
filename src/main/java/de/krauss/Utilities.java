package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class Utilities
{
	public static final String sdf_Pattern = "dd.MM.yyyy (HH:mm)";
	private static SimpleDateFormat sdf = new SimpleDateFormat(sdf_Pattern);
	private static Logger logger = Logger.getLogger("Utilities");

	/**
	 * 
	 * @param start_Date Anfang der neuen Reservierung
	 * @param stop_Date  Ende der neuen Reservierung
	 * @return Ob die Pflanze zu der Zeit schon reserviert ist
	 */
	public static boolean isCarAvaible(Date start_Date, Date stop_Date, Car c)
	{
		for (Reservierung r : c.getReservs())
		{
			if (r.getResStop().before(start_Date) || r.getResStart().after(stop_Date))
			{
				return false;
			}

			if (r.getResStart().before(start_Date) && r.getResStop().after(stop_Date))
				return false;
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

	public static String format(Date d)
	{
		return sdf.format(d);
	}

	public static Car chooseCarFromList(CarList carlist, BufferedReader reader)
	{
		while (true)
		{
			try
			{

				int choo = Integer.parseInt(reader.readLine());
				if (choo > carlist.getList().size())
				{
					new Exception();
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
