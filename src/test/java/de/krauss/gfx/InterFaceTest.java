package de.krauss.gfx;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import de.krauss.Launcher;
import de.krauss.car.CarList;
import de.krauss.search.Searcher;
import de.krauss.user.UserManager;
import de.krauss.utils.OracleDataBase;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class InterFaceTest extends ApplicationTest
{
	private ALLINONEFrameController controller;
	private OracleDataBase orcb;
	private LoginFrameController loginFrameController;
	private Launcher launcher = new Launcher();
	private UserManager manager = new UserManager();
	private String passwortForLogin = "Das5!tEinT";

//	@Override
//	public void start(Stage arg0) throws Exception
//	{
//		controller = ALLINONEFrameController.createWindow();
//		orcb = new OracleDataBase();
//
//		CarList list = new CarList();
//		list.setOrcb(orcb);
//
//		Searcher searcher = new Searcher();
//		searcher.setOrcb(orcb);
//
//		controller.setOrcb(orcb);
//		controller.init(list, searcher);
//
//		orcb.delteAllDataFromBase();
//		arg0.toFront();
//		arg0.requestFocus();
//	}

	@Override
	public void start(Stage arg0) throws Exception
	{
		loginFrameController = LoginFrameController.createWindow();
		orcb = new OracleDataBase();

		CarList list = new CarList();
		list.setOrcb(orcb);

		Searcher searcher = new Searcher();
		searcher.setOrcb(orcb);

		loginFrameController.init(manager, launcher, list, searcher);

		manager.deleteUser("JUnitTester");

		orcb.delteAllDataFromBase();
		arg0.toFront();
		arg0.requestFocus();
	}

	private void selectTabDown(int pressDown)
	{

		clickOn("#listView_Tabs");
		for (int i = 0; i < pressDown; i++)
		{
			type(KeyCode.DOWN);
		}
	}

	private void selectTabUp(int pressDown)
	{

		clickOn("#listView_Tabs");
		for (int i = 0; i < pressDown; i++)
		{
			type(KeyCode.UP);
			System.out.println("DOWN");
		}
	}

	private void registerAndLogin()
	{
		// Registrieren
		clickOn("#lbl_Registrieren");

		clickOn("#txtf_Username");
		write("JUnitTester");

		clickOn("#pw_Password");
		write(passwortForLogin);

		clickOn("#pw_CheckPassword");
		write(passwortForLogin + "s");

		clickOn("#btn_Registrieren");

		clickOn("#pw_CheckPassword");
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.BACK_SPACE);

		clickOn("#btn_Registrieren");

		// Einloggen
		clickOn("#txtf_Username");
		write("JUnitTester");

		clickOn("#pw_Password");
		write("123");

		clickOn("#btn_Login"); // False Login

		clickOn("#pw_Password");
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.RIGHT);
		type(KeyCode.BACK_SPACE);
		type(KeyCode.BACK_SPACE);
		type(KeyCode.BACK_SPACE);
		write(passwortForLogin);

		clickOn("#btn_Login");
	}

	@Before
	public void addCar()
	{
		registerAndLogin();

		selectTabDown(1);

		// Namen eingeben
		clickOn("#txtf_Name");
		write("Ghost-Hand");

		// Fehler testen
		clickOn("#txtf_Marke");
		clickOn("#btn_Hinzufügen");

		// Marke eingeben
		clickOn("#txtf_Marke");
		write("VW (Die Marke)");

		// Fehler eingeben
		clickOn("#txtf_Tacho");
		write("o5");

		// Tacho eingeben
		clickOn("#btn_Hinzufügen");
		clickOn("#txtf_Tacho");
		type(KeyCode.BACK_SPACE);

		clickOn("#btn_Hinzufügen");
	}

	@Test
	public void testInterface()
	{

		/*
		 * Reservieren
		 */
		// Tab auswählen
		selectTabDown(2);

		// Schreibe Start Datum
		clickOn("#date_Start");
		write("22.12.2030");

		// Schreibe Stop Datum DU MUSST DEN TAB ÜBER LISTE MACHEN + STYLESHEETS
		clickOn("#date_Stop");
		write("24.12.2030");

		// Teste auf Fehler
		clickOn("#btn_AddResv");

		// Schreibe Start Uhrzeit
		clickOn("#textf_StartResv");
		write("11:00");

		// Schreibe Stop Uhrzeit
		clickOn("#textf_StopResv");
		write("12:00");

		// Füge Reservierung hinzu
		clickOn("#btn_AddResv");

		// Für mehr CodeAbdeckung
		controller = launcher.getAllInOneFrameController();
		controller.updateListFomDatabase();
		/*
		 * Suchen
		 */
		// Tab auswählen
		selectTabDown(3);

		// Nach Name suchen
		clickOn("#rb_Name");
		clickOn("#txtfs_Name");
		write("G");
		clickOn("#btn_StartSearch");

		// Marke mit hinzufügen
		clickOn("#rb_Marke");
		clickOn("#txtfs_Marke");
		write("VW");
		clickOn("#btn_StartSearch");

		// Tacho hinzufügen
		clickOn("#rb_Tacho");
		clickOn("#txtfs_Tacho");
		write("5");
		clickOn("#btn_StartSearch");

		// Falsche Suche
		clickOn("#txtfs_Tacho");
		write("5");
		clickOn("#btn_StartSearch");

		/*
		 * 
		 * Auto wieder löschen
		 * 
		 */
		// Tab auswählen
		selectTabUp(3);

		// Obersters Auto auswählen
		clickOn("#list_Autos");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);

		// RESERVIERUNG LÖSCHEN
		clickOn("#btn_DeleteReservierung");

		clickOn("#list_Autos");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#btn_Löschen");
	}

}
