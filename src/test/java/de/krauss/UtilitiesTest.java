package de.krauss;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class UtilitiesTest
{

	@Test
	public void test()
	{
		Car resCar = new Car();

		Assert.assertNotNull(Utilities.format(new Date()));

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("dwads\n");
		strBuilder.append("14.12.2012 12:30\n");
		strBuilder.append("14.12.2021 12:30\n");
		Date date_1 = Utilities.enterDate(new BufferedReader(new StringReader(strBuilder.toString())));
		Assert.assertNotNull(date_1);

		Date date_2 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 12:00"))); // 14.12.21 12:30 -
		// 12:00
		Utilities.isCarAvaible(date_1, date_2, resCar, null);

		resCar.addResv(new Reservierung(date_1, date_2));

		Date date_3 = Utilities.enterDate(new BufferedReader(new StringReader("12.12.2021 11:00")));
		Date date_4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 14:00")));
		Assert.assertFalse(Utilities.isCarAvaible(date_3, date_4, resCar, null));

		date_3 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 11:00")));
		date_4 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 14:00")));
		Assert.assertFalse(Utilities.isCarAvaible(date_3, date_4, resCar, null));

		date_3 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 16:00")));
		date_4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 18:00")));
		Assert.assertFalse(Utilities.isCarAvaible(date_3, date_4, resCar, null));

		date_3 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 16:00")));
		date_4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 11:00")));
		Assert.assertFalse(Utilities.isCarAvaible(date_3, date_4, resCar, null));

		CarList carList = new CarList();
		carList.getList().add(new Car());
		strBuilder = new StringBuilder();
		strBuilder.append("w\n");
		strBuilder.append("1\n");
		Assert.assertNotNull(
				Utilities.chooseCarFromList(carList, new BufferedReader(new StringReader(strBuilder.toString()))));

		strBuilder = new StringBuilder();
		strBuilder.append("w\n");
		strBuilder.append("5\n");
		Assert.assertEquals(5, Utilities.addTacho(new BufferedReader(new StringReader(strBuilder.toString()))));

		// MIT CONTROLLER

//		StringBuilder str = new StringBuilder();
//		str.append("dwads\n");
//		str.append("14.12.2012 12:30\n");
//		str.append("14.12.2021 12:30\n");
//		Date dr1 = Utilities.enterDate(new BufferedReader(new StringReader(str.toString())));
//		Assert.assertNotNull(dr1);
//
//		Date dr2 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 12:00"))); // 14.12.21 12:30 -
//																									// 15.12.21 12:00
//
//		Utilities.isCarAvaible(dr1, dr2, resCar);
//
//		resCar.addResv(new Reservierung(dr1, dr2));
//
//		Date dr3 = Utilities.enterDate(new BufferedReader(new StringReader("12.12.2021 11:00")));
//		Date dr4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 14:00")));
//		Assert.assertFalse(Utilities.isCarAvaible(dr3, dr4, resCar));
//
//		dr3 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 11:00")));
//		dr4 = Utilities.enterDate(new BufferedReader(new StringReader("15.12.2021 14:00")));
//		Assert.assertFalse(Utilities.isCarAvaible(dr3, dr4, resCar));
//
//		dr3 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 16:00")));
//		dr4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 18:00")));
//		Assert.assertFalse(Utilities.isCarAvaible(dr3, dr4, resCar));
//
//		dr3 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 16:00")));
//		dr4 = Utilities.enterDate(new BufferedReader(new StringReader("14.12.2021 11:00")));
//		Assert.assertFalse(Utilities.isCarAvaible(dr3, dr4, resCar));
//
//		CarList c = new CarList();
//		c.addCar(new Car());
//		str = new StringBuilder();
//		str.append("w\n");
//		str.append("1\n");
//		Assert.assertNotNull(Utilities.chooseCarFromList(c, new BufferedReader(new StringReader(str.toString()))));
//
//		str = new StringBuilder();
//		str.append("w\n");
//		str.append("5\n");
//		Assert.assertEquals(5, Utilities.addTacho(new BufferedReader(new StringReader(str.toString()))));

	}
}
