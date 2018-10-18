package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import de.krauss.Reservierung;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainFrameController
{

	@FXML
	private ListView<String> list_Autos;

	@FXML
	private Button btn_Hinzufügen, btn_Reservieren, btn_Löschen, btn_Reslöschen, btn_Exportieren;

	@FXML
	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop, label_Status, label_User;

	@FXML
	private ComboBox<String> combo_Res;

	private CarList carlist;
	private OracleDataBase orcb;
	private Logger logger = Logger.getLogger("MainFrameController");

	private FileManager fm;
	private Initializer initer;

	/**
	 * 
	 * @param c Setzt die Carlist
	 */
	public void setCarlist(CarList c)
	{
		carlist = c;
	}

	/**
	 * Zeigt im GUI an ob verbunden oder nicht
	 * 
	 * @param verbunden Verbunden oder nicht
	 * @param user      Der User der in die DB eingeloggt ist
	 */
	public void setDatenbankStatus(boolean verbunden, String user)
	{
		if (verbunden)
		{
			label_Status.setTextFill(Color.LIGHTGREEN);
			label_Status.setText("ONLINE");

			label_User.setTextFill(Color.LIGHTGREEN);
			label_User.setText(user);

		} else
		{
			label_Status.setTextFill(Color.RED);
			label_Status.setText("OFFLINE");

			label_User.setTextFill(Color.RED);
			label_User.setText("Kein User");
		}
	}

	/**
	 * Öffnet das Exportieren-Fenster
	 */
	@FXML
	public void exportieren()
	{
		ExportFrameController controll = ExportFrameController.createWindow();
		controll.init(fm);
		controll.setCarlist(carlist);
	}

	/**
	 * 
	 * @param m Setzt die Instanz des Filemanager
	 */
	public void setFileManager(FileManager m)
	{
		fm = m;
	}

	/**
	 * 
	 * @param c Sezt die Instanz der OrcaleDataBase
	 */
	public void setOracleDataBase(OracleDataBase c)
	{
		orcb = c;
	}

	/**
	 * Fügt den Elementen im GUI die Listener hinzu
	 */
	public void init()
	{
		initer = new Initializer();
		initer.setList_Autos(list_Autos);
		initer.setBtn_Löschen(btn_Löschen);
		initer.setBtn_Reservieren(btn_Reservieren);
		initer.setBtn_Reslöschen(btn_Reslöschen);
		initer.setCarlist(carlist);
		initer.setCombo_Res(combo_Res);
		initer.setLabel_Marke(label_Marke);
		initer.setLabel_Name(label_Name);
		initer.setLabel_Tachostand(label_Tachostand);
		initer.setLbl_Res_start(lbl_Res_start);
		initer.setLbl_Res_stop(lbl_Res_stop);
		initer.setMainFrameController(this);
		initer.setOracleDataBase(orcb);
		initer.init();
	}

	/**
	 * Wird durch einen Button von dem GUI angesprochen und startet ein kleineres
	 * Fenster in welchem man eine neue Reserveriung hinzufügen kann
	 */
	@FXML
	public void reservieren()
	{
		AddResvController controll = AddResvController.createWindow();
		controll.addButtonListener(this);
	}

	/**
	 * Fügt dem ausgewählten Auto eine Reservierung hinzu
	 * 
	 * @param r Reservierung welche hinzugefügt werden soll
	 */
	public void addReservierungToSelCar(Reservierung r)
	{
		try
		{
			Car c = getSelectedCar();
			r.setCarID(c.getCAR_ID());
			orcb.uploadRes(r);
			carlist.loadCarsFromDataBase(orcb);
			setList(carlist.getList());

		} catch (Exception e)
		{
			logger.info("Kein Auto ausgewählt");
		}
	}

	/**
	 * Startet ein neues Fenster in welchem man ein neues Auto hinzufügen kann
	 */
	@FXML
	public void hinzufügen()
	{
		AddCarController controll = AddCarController.createWindow();
		controll.setCarlist(carlist);
		controll.addListenerToButton(this);
		controll.setOracleDataBase(orcb);
	}

	/**
	 * Löscht das im GUI ausgewählte Auto
	 */
	@FXML
	public void ausgewählteLöschen()
	{
		Car delCar = getSelectedCar();
		label_Marke.setText("");
		label_Name.setText("");
		label_Tachostand.setText("");
		carlist.getList().remove(delCar);
		list_Autos.getSelectionModel().clearSelection();
		orcb.deleteCarFromDatabase(delCar.getCAR_ID());
		setList(carlist.getList());
	}

	/**
	 * Ersetzt die Liste der Fahrzeuge
	 * 
	 * @param cars Die neue Liste der Autos
	 */
	public void setList(ArrayList<Car> cars)
	{
		ArrayList<String> names = new ArrayList<>();

		for (Car c : cars)
		{
			names.add(c.getF_Name());
		}
		try
		{
			list_Autos.setItems(FXCollections.observableArrayList(names));
		} catch (IllegalStateException e)
		{
			logger.warn("Auto hinzugefügt, dennoch falscher Thread (" + e.getMessage() + ")");
		}
	}

	public static MainFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();

			File f = new File(Launcher.class.getResource("/de/krauss/gfx/MainFrame.fxml").getFile());
			System.out.println(f.getAbsolutePath());
			FileInputStream fis = new FileInputStream(f);
			AnchorPane pane = loader.load(fis);
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(pane));
			MainFrameController controller = loader.getController();

			primaryStage.setTitle("Fuhrpark");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent arg0)
				{
					System.exit(1);
				}
			});
			primaryStage.getIcons().add(new Image("icon.png"));

			primaryStage.show();
			fis.close();
			return controller;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return Gibt das ausgewählte Auto zurück
	 */
	public Car getSelectedCar()
	{
		return carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
	}

}
