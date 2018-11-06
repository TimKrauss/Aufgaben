package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainFrameControllerTest extends ApplicationTest
{
	private MainFrameController controller;
	private OracleDataBase orcb;

	@Override
	public void start(Stage arg0) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		File f = new File(Launcher.class.getResource("/de/krauss/gfx/MainFrame.fxml").getFile());
		System.out.println(f.getAbsolutePath());
		FileInputStream fis = new FileInputStream(f);
		AnchorPane pane = loader.load(fis);
		controller = loader.getController();
		arg0.setScene(new Scene(pane));
		arg0.show();
		arg0.toFront();
		fis.close();
		orcb = new OracleDataBase();
		controller.setFileManager(new FileManager());

		CarList list = new CarList();
		list.setOrcb(orcb);
		controller.setCarlist(list);

		controller.init();
		orcb.delteAllDataFromBase();
		arg0.toFront();
		arg0.requestFocus();
	}

	@Test
	public void reservieren()
	{

		clickOn("#list_Autos");
		type(KeyCode.DOWN);

		clickOn("#btn_Reservieren");
		clickOn("#date_Start");
		write("22.12.2030");

		clickOn("#date_Stop");
		write("24.12.2030");

		clickOn("#btn_Add");

		clickOn("#textf_Start");
		write("11:00");

		clickOn("#textf_Stop");
		write("12:00");

		clickOn("#btn_Add");

		// Exportieren
		clickOn("#btn_Exportieren");
		clickOn("#check_Dump");
		clickOn("#check_JSon");
		clickOn("#check_XML");
		clickOn("#check_Txt");
		clickOn("#txtf_Name");
		write("GhostTry");
		clickOn("#btn_Export");

		// Searcher
		clickOn("#btn_Search");
		clickOn("#rb_Name");
		clickOn("#txtf_Name");
		write("G");
		clickOn("#btn_StartSearch");

		clickOn("#rb_Marke");
		clickOn("#txtf_Marke");
		write("VW");
		clickOn("#btn_StartSearch");

		clickOn("#rb_Tacho");
		clickOn("#txtf_Tacho");
		write("5");
		clickOn("#btn_StartSearch");

		clickOn("#txtf_Tacho");
		write("5");
		clickOn("#btn_StartSearch");
		clickOn("#btn_closeSearcher");

	}

	@After
	public void löschen()
	{
		clickOn("#list_Autos");
		type(KeyCode.DOWN);

		clickOn("#btn_Reslöschen");

		clickOn("#list_Autos");
		type(KeyCode.DOWN);

		clickOn("#btn_Löschen");
		clickOn("#btn_Import");
		orcb.closeConnection();

	}

	@Before
	public void testAutohinzufügen()
	{
		clickOn("#btn_DeleteAll");
		clickOn("#btn_Hinzufügen");

		clickOn("#txtf_Name");
		write("Ghost-Hand");
		clickOn("#txtf_Marke");
		clickOn("#btn_Add");
		clickOn("#txtf_Marke");
		write("VW (Die Marke)");
		clickOn("#txtf_Tacho");
		write("500o");

		clickOn("#btn_Add");
		clickOn("#txtf_Tacho");
		type(KeyCode.BACK_SPACE);

		clickOn("#btn_Add");

	}
}
