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
	 * am Ende
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

	public int getCarID()
	{
		return CAR_ID;
	}

	public void setCarID(int iD)
	{
		CAR_ID = iD;
	}

	public int getRES_ID()
	{
		return RES_ID;
	}

	public void setRES_ID(int rES_ID)
	{
		RES_ID = rES_ID;
	}
}
