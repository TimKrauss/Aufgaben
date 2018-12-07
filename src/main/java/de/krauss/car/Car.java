package de.krauss.car;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class Car implements Serializable
{

	private static final long serialVersionUID = 1257287804168043133L;
	private String carName;
	private String carMarke;
	private int carID = 0;
	private int carTacho;
	@XmlElement(name = "Reservierungen")
	private ArrayList<Reservierung> resv;

	/**
	 * Konstruktor zur Initalisierung der Arraylist für Reservierungen
	 */
	public Car()
	{
		resv = new ArrayList<Reservierung>();
	}

	/**
	 * 
	 * @return Gibt den Namen des Fahrzeuges zurück
	 */
	public String getCarName()
	{
		return carName;
	}

	/**
	 * 
	 * @param f_Name Setzt den Namen des Fahrzeuges
	 */
	public void setCarName(String f_Name)
	{
		this.carName = f_Name;
	}

	/**
	 * 
	 * @return Gibt die Marke des Fahrzeuges zurück
	 */
	public String getCarMarke()
	{
		return carMarke;
	}

	/**
	 * 
	 * @param f_Marke Setzt die Marke des Fahrzeuges
	 */
	public void setCarMarke(String f_Marke)
	{
		this.carMarke = f_Marke;
	}

	/**
	 * 
	 * @return Gibt den Tachostand des Fahrzeuges zurück
	 */
	public int getCarTacho()
	{
		return carTacho;
	}

	/**
	 * 
	 * @param f_Tacho Setzt den Tachostamd des Fahrzeuges
	 */
	public void setCarTacho(int f_Tacho)
	{
		this.carTacho = f_Tacho;
	}

	/**
	 * 
	 * @param r Fügt der Reservierungs-Arraylist eine Reservierung hinzu
	 */
	public void addResv(Reservierung r)
	{
		resv.add(r);
	}

	/**
	 * Gibt die Liste mit Reservierungen für das Auto zurück
	 * 
	 * @return Liste mit Reservierungen
	 */
	public ArrayList<Reservierung> getReservs()
	{
		return resv;
	}

	/**
	 * 
	 * @return ID des Autos
	 */
	public int getCarID()
	{
		return carID;
	}

	/**
	 * 
	 * @param cAR_ID Setzt ID des Atuos
	 */
	public void setCarID(int cAR_ID)
	{
		carID = cAR_ID;
	}

}
