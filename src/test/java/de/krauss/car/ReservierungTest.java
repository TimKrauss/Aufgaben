package de.krauss.car;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.krauss.car.Reservierung;

public class ReservierungTest
{
	private Reservierung reservierung;
	private static final Date beforeStartDate = new Date();
	private static final Date startDate = new Date();
	private static final Date stopDate = new Date();
	private static final int reservierungID = 5;

	@Before
	public void init()
	{
		reservierung = new Reservierung();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test()
	{
		reservierung.setResStart(startDate);
		reservierung.setResStop(stopDate);
		reservierung.setResvID(reservierungID);

		Assert.assertEquals(startDate, reservierung.getResStart());
		Assert.assertEquals(stopDate, reservierung.getResStop());
		Assert.assertEquals(reservierungID, reservierung.getResvID());

//		Assert.assertTrue(reservierung.isReserved(startDate));
//
//		beforeStartDate.setHours(beforeStartDate.getHours() - 1);
//		Assert.assertTrue(reservierung.isReserved(beforeStartDate));

		beforeStartDate.setHours(beforeStartDate.getHours() + 1);
		Date later = new Date();
		later.setHours(later.getHours() + 5);
//		Assert.assertFalse(reservierung.isReserved(later));
		reservierung = new Reservierung(startDate, stopDate);
	}

}
