package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.Launcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
	private FileManager fileManager;

	/**
	 * Schließt das Exportieren ab und schließt das kleine Fenster
	 */
	@FXML
	public void exportieren()
	{
		String fName = txtf_Name.getText();

		if (expoDir == null || fName.equals(" +"))
		{
			return;
		}

		if (check_Dump.isSelected())
		{
			fileManager.safe(carlist, FileManager.DUMP_FILE,
					new File(expoDir.getAbsolutePath() + "/" + fName + ".dump"));
		}
		if (check_JSon.isSelected())
		{
			fileManager.safe(carlist, FileManager.JSON_FILE,
					new File(expoDir.getAbsolutePath() + "/" + fName + ".json"));
		}
		if (check_Txt.isSelected())
		{
			fileManager.safe(carlist, FileManager.TXT_FILE, new File(expoDir.getAbsolutePath() + "/" + fName + ".txt"));
		}
		if (check_XML.isSelected())
		{
			fileManager.safe(carlist, FileManager.XSTREAM_FILE,
					new File(expoDir.getAbsolutePath() + "/" + fName + ".xml"));
		}

		stage.getScene().getWindow().hide();
	}

	/**
	 * Initalisiert alle von dem Fenster nötigen Elemente
	 * 
	 * @param filmanager Setzt die Instanz des FileManagers
	 */
	public void init(FileManager filmanager)
	{

		fileManager = filmanager;
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
	 * Erzeugt das Fenster
	 * 
	 * @return Gibt den Controller für das Fenster zurück
	 */
	public static ExportFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			FileInputStream fis = new FileInputStream(
					new File((Launcher.class.getResource("/de/krauss/gfx/ExportFrame.fxml").getFile())));
			Parent root = loader.load(fis);
			fis.close();

			ExportFrameController controll = loader.getController();
			Stage stage = new Stage();

			stage.setTitle("Autos exportieren");
			stage.setAlwaysOnTop(true);
			stage.getIcons().add(new Image("export.png"));
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			controll.setStage(stage);
			stage.show();

			return controll;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
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
