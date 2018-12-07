package de.krauss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.krauss.car.CarListTest;
import de.krauss.car.CarTest;
import de.krauss.car.ReservierungTest;
import de.krauss.gfx.InterFaceTest;
import de.krauss.handler.DumpFileHandlerTest;
import de.krauss.handler.JAXBFileHandlerTest;
import de.krauss.handler.JSonFileHandlerTest;
import de.krauss.handler.TxtFileHandlerTest;
import de.krauss.handler.XStreamFileHandlerTest;
import de.krauss.search.SearcherTest;
import de.krauss.utils.FileManagerTest;
import de.krauss.utils.OracleDataBaseTest;
import de.krauss.utils.UtilitiesTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LauncherTest.class, CarListTest.class, CarTest.class, FileManagerTest.class,
		ReservierungTest.class, SearcherTest.class, OracleDataBaseTest.class, DumpFileHandlerTest.class,
		JAXBFileHandlerTest.class, JSonFileHandlerTest.class, TxtFileHandlerTest.class, XStreamFileHandlerTest.class,
		InterFaceTest.class, UtilitiesTest.class })
public class Tester
{
//
	/*
	 * 
	 */

}
