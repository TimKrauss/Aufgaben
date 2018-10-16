package de.krauss;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

public class Reservierung implements Serializable
{

	/**
	 * Log4j logger
	 */
	private static Logger logger = Logger.getLogger("Reservierung");
	private static final long serialVersionUID = 13273646095955764L;
	private Date resStart, resStop;
	private int CAR_ID;
	private int RES_ID;

	/**
	 * Erzeugt eine neue Reservierug
	 * 
	 * @param start Setzt das Anfangsdatum
	 * @param stop  Setzt das Stopdatum
	 */
	public Reservierung(Date start, Date stop)
	{
		resStart = start;
		resStop = stop;
	}

	/**
	 * Erzeugt eine neue Reservierung und man setzt das Start, sowie StopDatum erst
	 * später
	 */
	public Reservierung()
	{

	}

	/**
	 * 
	 * @return Gibt das Datum an welchem die Reservierung startet zurück
	 */
	public Date getResStart()
	{
		return resStart;
	}

	/**
	 * 
	 * @param resStart Setzt das Datum an welchem die Reservierung startet
	 */
	public void setResStart(Date resStart)
	{
		this.resStart = resStart;
	}

	/**
	 * 
	 * @return Gibt das Datum an welchem die Reservierung endet zurück
	 */
	public Date getResStop()
	{
		return resStop;
	}

	/**
	 * 
	 * @param reader Der Reader zum Lesen der Usereingabe
	 * @param resCar Das Auto welches Resererviert werden soll
	 * @return Eine Reservierung für das Auto
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
			Reservierung r = new Reservierung(start, stop);
			r.setCarID(resCar.getCAR_ID());
			r.setRES_ID(-1);
			return r;
		}

	}

	/**
	 * 
	 * @param resStop Setzt das Datum an welchem die Reservierung stopt
	 */
	public void setResStop(Date resStop)
	{
		this.resStop = resStop;
	}

	/**
	 * 
	 * @return Gibt die CAR_ID zurück
	 */
	public int getCarID()
	{
		return CAR_ID;
	}

	/**
	 * 
	 * @param iD Setzt die Car_ID
	 */
	public void setCarID(int iD)
	{
		CAR_ID = iD;
	}

	/**
	 * 
	 * @return Gibt die ReservierungsID zurück
	 */
	public int getRES_ID()
	{
		return RES_ID;
	}

	/**
	 * 
	 * @param rES_ID Setzt die ReservierungsID
	 */
	public void setRES_ID(int rES_ID)
	{
		RES_ID = rES_ID;
	}
}
