package de.krauss;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class LauncherTest
{
	private Launcher launcher;
	private BufferedReader reader;
	private OracleDataBase orc;
	private static final String NAME = "TEST", MARKE = "Marke";
	private static final String START_DATUM = "12.12.2019 15:00", STOP_DATUM = "13.12.2019 16:00";
	private static final int TACHO = 2;

	@Before
	public void init() throws Exception
	{
		launcher = new Launcher();
		launcher.init();
		orc = new OracleDataBase();
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
		launcher.handleUserInpunt(reader);

//		Auto hinzufügen (handleMethode vorschalten)
		st = new StringBuilder();
		st.append("ja\n");
		st.append(NAME + "\n");
		st.append(MARKE + "\n");
		st.append(TACHO);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Reservierung hinzufügen
		st = new StringBuilder();
		st.append("reservieren\n");
		st.append("1\n");
		st.append(START_DATUM + "\n");
		st.append(STOP_DATUM);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Auflisten
		st = new StringBuilder();
		st.append("list\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		StandardCall
		launcher.standardCall();

//		Reservierung löschen
		st = new StringBuilder();
		st.append("rdel\n");
		st.append("1\n");
		st.append("1\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		getReader

//		Suchen
		st = new StringBuilder();
		st.append("search\n");
		st.append("1\n");
		st.append(NAME);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

		st = new StringBuilder();
		st.append("search\n");
		st.append("2\n");
		st.append(MARKE);

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

		st = new StringBuilder();
		st.append("search\n");
		st.append("3\n");
		st.append(TACHO + "");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Fehlerhafte Suche
		st = new StringBuilder();
		st.append("search\n");
		st.append("1\n");
		st.append(NAME + "BLAH");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

		st = new StringBuilder();
		st.append("search\n");
		st.append("2\n");
		st.append(MARKE + "BLAH");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

		st = new StringBuilder();
		st.append("search\n");
		st.append("3\n");
		st.append(TACHO + 5 + "");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Auto löschen
		st = new StringBuilder();
		st.append("del\n");
		st.append("1\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Nein
		st = new StringBuilder();
		st.append("nein\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
		launcher.handleUserInpunt(reader);

//		Alles Explodieren lassen
		st = new StringBuilder();
		st.append("nein\n");

		stri = new StringReader(st.toString());
		reader = launcher.createReader(stri);
	}

}
