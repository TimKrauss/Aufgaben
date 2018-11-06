package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.Launcher;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainFrameController
{
	@FXML
	private ListView<Car> list_Autos;

	@FXML
	private Button btn_Hinzuf�gen, btn_Reservieren, btn_L�schen, btn_Resl�schen, btn_Exportieren, btn_Search,
			btn_DeleteAll;

	@FXML
	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop;

	@FXML
	private ComboBox<String> combo_Res;

	private CarList carlist;
	private static Logger logger = Logger.getLogger("System");
	private static Stage primaryStage;
	private FileManager fileManager;
	private Initializer initer;

	/**
	 * 
	 * @param carlist Setzt die Carlist
	 */
	public void setCarlist(CarList carlist)
	{
		this.carlist = carlist;
	}

	/**
	 * �ffnet ein FileChooser und importiert die Autos aus der Datei
	 */
	@FXML
	public void importieren()
	{
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Supported Files", "*.dump", "*.json", "*.txt", "*.xml"));
		chooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));

		File importFile = chooser.showOpenDialog(primaryStage);

		if (importFile == null)
			return;

		ArrayList<Car> newCars = null;
		newCars = fileManager.load(fileManager.detectOption(importFile), importFile, carlist);

		if (newCars == null)
		{
			System.out.println("NewCars == NULL");
			return;
		}

		carlist.addCars(newCars);

		setList(carlist.getList());
	}

	/**
	 * �ffnet das Exportieren-Fenster
	 */
	@FXML
	public void exportieren()
	{
		ExportFrameController controll = ExportFrameController.createWindow();
		controll.init(fileManager);
		controll.setCarlist(carlist);
	}

	/**
	 * 
	 * @param manager Setzt die Instanz des Filemanager
	 */
	public void setFileManager(FileManager manager)
	{
		fileManager = manager;
	}

	/**
	 * F�gt den Elementen im GUI die Listener hinzu
	 */
	public void init()
	{
		initer = new Initializer();
		initer.setList_Autos(list_Autos);
		initer.setBtn_L�schen(btn_L�schen);
		initer.setBtn_Reservieren(btn_Reservieren);
		initer.setBtn_Resl�schen(btn_Resl�schen);
		initer.setCarlist(carlist);
		initer.setCombo_Res(combo_Res);
		initer.setLabel_Marke(label_Marke);
		initer.setLabel_Name(label_Name);
		initer.setLabel_Tachostand(label_Tachostand);
		initer.setLbl_Res_start(lbl_Res_start);
		initer.setLbl_Res_stop(lbl_Res_stop);
		initer.setMainFrameController(this);
		initer.setBtn_DeleteAll(btn_DeleteAll);
		initer.setBtn_Update(btn_Search);
		initer.init();

		// Liste aktualiesieren
		setList(carlist.getList());
	}

	/**
	 * Wird durch einen Button von dem GUI angesprochen und startet ein kleineres
	 * Fenster in welchem man eine neue Reserveriung hinzuf�gen kann
	 */
	@FXML
	public void reservieren()
	{
		AddResvController controll = AddResvController.createWindow();
		controll.addButtonListener(this);
	}

	/**
	 * F�gt dem ausgew�hlten Auto eine Reservierung hinzu
	 * 
	 * @param resv Reservierung welche hinzugef�gt werden soll
	 */
	public void addReservierungToSelCar(Reservierung resv)
	{
		Car car = getSelectedCar();

		if (car == null)
		{
			return;
		}

		resv.setCarID(car.getCarID());

		// DATENBANK + LOKAL
		carlist.addReservierung(resv, car);

		setList(carlist.getList());
	}

	/**
	 * Startet ein neues Fenster in welchem man ein neues Auto hinzuf�gen kann
	 */
	@FXML
	public void hinzuf�gen()
	{
		AddCarController controll = AddCarController.createWindow();
		controll.setCarlist(carlist);
		controll.addListenerToButton(this);
	}

	/**
	 * L�scht das im GUI ausgew�hlte Auto
	 */
	@FXML
	public void ausgew�hlteL�schen()
	{
		Car delCar = getSelectedCar();

		if (delCar == null)
			return;

		label_Marke.setText("");
		label_Name.setText("");
		label_Tachostand.setText("");
		list_Autos.getSelectionModel().clearSelection();

		// DATENBANK + LOKAL
		carlist.deleteCar(delCar);

		setList(carlist.getList());
	}

	/**
	 * Ersetzt die Liste der Fahrzeuge
	 * 
	 * @param cars Die neue Liste der Autos
	 */
	public void setList(ArrayList<Car> cars)
	{
		list_Autos.getItems().clear();
		list_Autos.refresh();
		list_Autos.setItems(FXCollections.observableArrayList(cars));
		list_Autos.refresh();
	}

	/**
	 * Erzeugt eine Instanz des Mainframes
	 * 
	 * @return Gibt den Controller f�r das Mainframe zur�ck
	 */
	public static MainFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();

			File file = new File(Launcher.class.getResource("/de/krauss/gfx/MainFrame.fxml").getFile());
			FileInputStream fis = new FileInputStream(file);
			AnchorPane pane = loader.load(fis);
			primaryStage = new Stage();
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
		} catch (FileNotFoundException e)
		{
			logger.warn(e.getLocalizedMessage());
		} catch (IOException e)
		{
			logger.warn(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 
	 * @return Gibt das in der Liste ausgew�hlte Auto zur�ck
	 */
	public Car getSelectedCar()
	{
		Car car = null;
		try
		{
			car = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
		} catch (Exception e) // All Exceptions da ich nicht wei�, welche man genau fangen sollte
		{
			logger.warn(e.getMessage());
			logger.warn("Wahrscheinlich kein Auto ausgew�hlt");
		}
		return car;
	}

}
