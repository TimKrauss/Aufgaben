package de.krauss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.krauss.handler.DumpFileHandlerTest;
import de.krauss.handler.JAXBFileHandlerTest;
import de.krauss.handler.JSonFileHandlerTest;
import de.krauss.handler.TxtFileHandlerTest;
import de.krauss.handler.XStreamFileHandlerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LauncherTest.class, CarListTest.class, CarTest.class, FileManagerTest.class,
		ReservierungTest.class, SearcherTest.class, OracleDataBaseTest.class, DumpFileHandlerTest.class,
		JAXBFileHandlerTest.class, JSonFileHandlerTest.class, TxtFileHandlerTest.class, XStreamFileHandlerTest.class })
public class Tester
{
//
}
