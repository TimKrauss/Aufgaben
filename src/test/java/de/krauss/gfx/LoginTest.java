package de.krauss.gfx;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import de.krauss.search.Searcher;
import de.krauss.user.UserManager;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginTest extends ApplicationTest
{

	private LoginFrameController loginFrameController;
	private OracleDataBase orcb = new OracleDataBase();
	private Launcher launcher = new Launcher();
	private UserManager manager = new UserManager();

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

		orcb.delteAllDataFromBase();
		arg0.toFront();
		arg0.requestFocus();
	}

	@Test
	public void test()
	{
		// Registrieren
		clickOn("#lbl_Registrieren");

		clickOn("#txtf_Username");
		write("JUnitTester");

		clickOn("#pw_Password");
		write("Das5!tKeinT()sT");

		clickOn("#pw_CheckPassword");
		write("Das5!tKeinT()sTt");

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
		type(KeyCode.BACK_SPACE);
		type(KeyCode.BACK_SPACE);
		type(KeyCode.BACK_SPACE);
		write("Das5!tKeinT()sTt");

		clickOn("#btn_Login");

	}
}
