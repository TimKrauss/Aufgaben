package de.krauss.gfx;

import java.io.File;

import de.krauss.CarList;
import de.krauss.FileManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ExportFrameController
{
	@FXML
	private CheckBox check_Dump, check_JSon, check_XML, check_Txt;

	@FXML
	private Button btn_Export, btn_fileChooser;

	@FXML
	private TextField txtf_Name;

	@FXML
	private Label lbl_Pfad;

	private Stage stage;
	private File expoDir = new File(System.getProperty("user.home") + "/Desktop/");
	private CarList carlist;
	private FileManager fm;

	/**
	 * Schließt das Exportieren ab und schließt das kleine Fenster
	 */
	@FXML
	public void exportieren()
	{
//
		String fName = txtf_Name.getText();

		if (expoDir == null || fName.equals(" +"))
		{
			return;
		}

		if (check_Dump.isSelected())
		{
			fm.safe(carlist, FileManager.DUMP_FILE, new File(expoDir.getAbsolutePath() + "/" + fName + ".DUMP"));
		}
		if (check_JSon.isSelected())
		{
			fm.safe(carlist, FileManager.JSON_FILE, new File(expoDir.getAbsolutePath() + "/" + fName + ".json"));
		}
		if (check_Txt.isSelected())
		{
			fm.safe(carlist, FileManager.TXT_FILE, new File(expoDir.getAbsolutePath() + "/" + fName + ".txt"));
		}
		if (check_XML.isSelected())
		{
			fm.safe(carlist, FileManager.XSTREAM_FILE, new File(expoDir.getAbsolutePath() + "/" + fName + ".xml"));
		}

		stage.getScene().getWindow().hide();
	}

	/**
	 * Initalisiert alle von dem Fenster nötigen Elemente
	 * 
	 * @param m Setzt die Instanz des FileManagers
	 */
	public void init(FileManager m)
	{

		fm = m;
		lbl_Pfad.setText(expoDir.getName());

		btn_fileChooser.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("SpeicherOrt auswählen");
				expoDir = chooser.showDialog(stage);
				lbl_Pfad.setText(expoDir.getName());
			}
		});
	}

	/**
	 * 
	 * @param stage Setzt die "Stage" Instanz
	 */
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

	/**
	 * 
	 * @param carlist Setzt die "Carlist" Instanz
	 */
	public void setCarlist(CarList carlist)
	{
		this.carlist = carlist;
	}

}
