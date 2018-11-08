package de.krauss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

import de.krauss.gfx.AddResvController;

public class Reservierung implements Serializable
{

	/**
	 * Log4j logger
	 */
	private static Logger logger = Logger.getLogger("System");
	private static final long serialVersionUID = 13273646095955764L;
	private Date resvStart, resvStop;
	private int carId = 0;
	private int resvID = 0;
	private String owner;

	/**
	 * Erzeugt eine neue Reservierug
	 * 
	 * @param start Setzt das Anfangsdatum
	 * @param stop  Setzt das Stopdatum
	 */
	public Reservierung(Date start, Date stop)
	{
		resvStart = start;
		resvStop = stop;
	}

	/**
	 * Erzeugt eine neue Reservierung und man setzt das Start, sowie StopDatum erst
	 * sp�ter
	 */
	public Reservierung()
	{

	}

	/**
	 * 
	 * @return Gibt das Datum an welchem die Reservierung startet zur�ck
	 */
	public Date getResStart()
	{
		return resvStart;
	}

	/**
	 * 
	 * @param resStart Setzt das Datum an welchem die Reservierung startet
	 */
	public void setResStart(Date resStart)
	{
		this.resvStart = resStart;
	}

	/**
	 * 
	 * @return Gibt das Datum an welchem die Reservierung endet zur�ck
	 */
	public Date getResStop()
	{
		return resvStop;
	}

	/**
	 * Erstellt eine Reservierung
	 * 
	 * @param reader Der Reader zum Lesen der Usereingabe
	 * @param resCar Das Auto welches Resererviert werden soll
	 * @return Eine Reservierung f�r das Auto
	 */
	public static Reservierung createReservierung(BufferedReader reader, Car resCar)
	{
		Date start = null;
		Date stop = null;

		while (true)
		{

			logger.info("Wann soll die Reservierung starten? (tt.MM.yyyy HH:mm)");

			start = Utilities.enterDate(reader);

			logger.info("Wann soll die Reservierung enden? (tt.MM.yyyy HH:mm)");

			stop = Utilities.enterDate(reader);
			Reservierung reservierung = new Reservierung(start, stop);

			if (Utilities.isCarAvaible(start, stop, resCar, new AddResvController()))
			{
				try
				{
					logger.info("Auf welchen Namen soll diese Reservierung gespeichert werden?");
					reservierung.setOwner(reader.readLine());
				} catch (IOException e)
				{
					logger.warn(e.getMessage());
					logger.info("Reservierung  wurde auf Unbekannt gesetzt");
					reservierung.setOwner("Unbekannt");
				}

				reservierung.setCarID(resCar.getCarID());
				reservierung.setResvID(-1);
				return reservierung;
			}

			while (true)
			{
				try
				{
					logger.info("M�chten sie das Reservieren abbrechen? (ja oder nein)");
					String antwort = reader.readLine();
					boolean reStart = false;

					switch (antwort)
					{
					case "ja":
						return null;
					case "nein":
						reStart = true;
						break;
					default:
						logger.info("Bitte eine g�ltige Antwort eingeben");
						break;
					}

					if (reStart)
						break;

				} catch (IOException e)
				{
					logger.fatal(e.getMessage());
				}
			}

		}

	}

	/**
	 * 
	 * @param resStop Setzt das Datum an welchem die Reservierung stopt
	 */
	public void setResStop(Date resStop)
	{
		this.resvStop = resStop;
	}

	/**
	 * 
	 * @return Gibt die CAR_ID zur�ck
	 */
	public int getCarID()
	{
		return carId;
	}

	/**
	 * 
	 * @param carID Setzt die Car_ID
	 */
	public void setCarID(int carID)
	{
		this.carId = carID;
	}

	/**
	 * 
	 * @return Gibt die ReservierungsID zur�ck
	 */
	public int getResvID()
	{
		return resvID;
	}

	/**
	 * 
	 * @param resvID Setzt die ReservierungsID
	 */
	public void setResvID(int resvID)
	{
		this.resvID = resvID;
	}

	/**
	 * Setzt den Owner der Reservierung
	 * 
	 * @param owner Setzt den Owner
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * 
	 * @return Gibt den Owner zur�ck
	 */
	public String getOwner()
	{
		return owner;
	}

}
