package de.krauss;

import java.io.Serializable;
import java.util.Date;

public class Reservierung implements Serializable
{

	/**
	 * Log4j logger
	 */
	private static final long serialVersionUID = 13273646095955764L;
	private Date resStart, resStop;
	private int ID;

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
	 * Erzeugt eine neue Reservierung
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
	 * @param resStop Setzt das Datum an welchem die Reservierung stopt
	 */
	public void setResStop(Date resStop)
	{
		this.resStop = resStop;
	}

	/**
	 * 
	 * @param f Das Datum welches überprüft werden soll
	 * @return True wenn das Fahrzeug zu der Zeit reserviert ist
	 */
	public boolean isReserved(Date f)
	{
		// TODO FIX NEEDED
		return true;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int iD)
	{
		ID = iD;
	}
}
