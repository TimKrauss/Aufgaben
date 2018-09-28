package de.krauss;

import java.util.Date;

public class Utilities
{
	/**
	 * 
	 * @param start_Date Anfang der neuen Reservierung
	 * @param stop_Date  Ende der neuen Reservierung
	 * @return Ob die Pflanze zu der Zeit schon reserviert ist
	 */
	public static boolean isSelectCarAvaible(Date start_Date, Date stop_Date, Car c)
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
}
