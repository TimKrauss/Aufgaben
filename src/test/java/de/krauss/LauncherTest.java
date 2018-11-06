package de.krauss;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import de.krauss.search.Searcher;

public class LauncherTest
{
	private Launcher launcher;
	private BufferedReader reader;
	private OracleDataBase orc;
	private UserHandler handler;
	private static final String NAME = "TEST", MARKE = "Marke";
	private static final String START_DATUM = "12.12.2019 15:00", STOP_DATUM = "13.12.2019 16:00";
	private static final int TACHO = 2;

	@Before
	public void init() throws Exception
	{
		launcher = new Launcher();
		launcher.init();
		orc = new OracleDataBase();
		handler = new UserHandler();

		CarList list = new CarList();
		list.setOrcb(orc);

		Searcher searcher = new Searcher();
		searcher.setOrcb(orc);

		handler.setSearcher(searcher);
		handler.setCarlist(list);
	}

	@Test
	public void test() throws Exception
	{
		orc.delteAllDataFromBase();

		StringBuilder st = new StringBuilder();
		StringReader stri = null;
		launcher.start(null);
//		Auflisten
		st = new StringBuilder();
		st.append("list\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Auto hinzufügen (handleMethode vorschalten)
		st = new StringBuilder();
		st.append("ja\n");
		st.append(NAME + "\n");
		st.append(MARKE + "\n");
		st.append(TACHO);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Reservierung hinzufügen
		st = new StringBuilder();
		st.append("reservieren\n");
		st.append("1\n");
		st.append(START_DATUM + "\n");
		st.append(STOP_DATUM + "\n");
		st.append("Junit");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

		// Reservierung hinzufügen
		st = new StringBuilder();
		st.append("reservieren\n");
		st.append("1\n");
		st.append("13.12.2019 16:00" + "\n");
		st.append("12.12.2019 15:00" + "\n");
		st.append("awd" + "\n");
		st.append("nein" + "\n");
		st.append("12.12.2019 15:00" + "\n");
		st.append("13.12.2099 16:00" + "\n");
		st.append("ja");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Auflisten
		st = new StringBuilder();
		st.append("list\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		StandardCall
		launcher.standardCall();

//		Reservierung löschen
		st = new StringBuilder();
		st.append("rdel\n");
		st.append("1\n");
		st.append("1\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		getReader

//		Suchen
		st = new StringBuilder();
		st.append("search\n");
		st.append("1\n");
		st.append(NAME);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

		st = new StringBuilder();
		st.append("search\n");
		st.append("2\n");
		st.append(MARKE);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

		st = new StringBuilder();
		st.append("search\n");
		st.append("3\n");
		st.append(TACHO + "");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Fehlerhafte Suche
		st = new StringBuilder();
		st.append("search\n");
		st.append("1\n");
		st.append(NAME + "BLAH");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

		st = new StringBuilder();
		st.append("search\n");
		st.append("2\n");
		st.append(MARKE + "BLAH");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

		st = new StringBuilder();
		st.append("search\n");
		st.append("3\n");
		st.append(TACHO + 5 + "");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Auto löschen
		st = new StringBuilder();
		st.append("del\n");
		st.append("1\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Nein
		st = new StringBuilder();
		st.append("nein\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		handler.handleUserInpunt(reader, launcher);

//		Alles Explodieren lassen
		st = new StringBuilder();
		st.append("nein\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
	}

}
