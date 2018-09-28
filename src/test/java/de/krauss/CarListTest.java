package de.krauss;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarListTest
{
	private CarList carlist;
	private ArrayList<Car> arrayList;
	private Car car;

	@Before
	public void init()
	{
		car = new Car();
		carlist = new CarList();
		arrayList = new ArrayList<>();
	}

	@Test
	public void test()
	{
		carlist.setCars(arrayList);
		Assert.assertEquals(arrayList, carlist.getList());

		carlist.addCar(car);
		Assert.assertEquals(car, carlist.getCar(0));

	}
}
