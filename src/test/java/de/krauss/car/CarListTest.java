package de.krauss.car;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.utils.OracleDataBase;

public class CarListTest
{
	private CarList carlist;
	private ArrayList<Car> arrayList;
	private Car car;
	private OracleDataBase orcb = new OracleDataBase();

	@Before
	public void init()
	{
		car = new Car();
		carlist = new CarList();
		carlist.setOrcb(orcb);
		arrayList = new ArrayList<>();
	}

	@Test
	public void test()
	{
		Assert.assertEquals(arrayList, carlist.getList());

		carlist.addCar(car);
		Assert.assertEquals(null, carlist.getCar(0));

		car.setCarMarke("VW");
		car.setCarName("Test");
		car.setCarTacho(123);
		car.setCarID(0);
		carlist.addCar(car);
		Assert.assertEquals(car, carlist.getCar(0));

		orcb.closeConnection();
	}
}
