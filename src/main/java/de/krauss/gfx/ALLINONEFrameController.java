package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import de.krauss.gfx.init.ManagerInitialize;
import de.krauss.search.Searcher;
import de.krauss.user.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class ALLINONEFrameController
{
	private static Stage primaryStage;
	private static Logger logger = Logger.getLogger("System");

	@FXML
	private ListView<Tab> listView_Tabs;

	@FXML
	private Tab tab_Main, tab_Reservieren, tab_Suchen, tab_Hinzufügen;

	@FXML
	private TabPane pane_Tabpane;

	@FXML
	private ListView<Car> list_Autos, list_SearchResults;

	@FXML
	private Button btn_Löschen, btn_Hinzufügen, btn_AddResv, btn_StartSearch, btn_DeleteReservierung,
			btn_OpenDirectoryChooser, btn_ImportCars;

	@FXML
	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop, lbl_SearchName,
			lbl_SearchMarke, lbl_SearchTacho, lbl_ShowErrorMsg, lbl_Benutzername, lbl_RechteLVL;

	@FXML
	private CheckBox rb_Name, rb_Marke, rb_Tacho;

	@FXML
	private DatePicker date_Start, date_Stop;

	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho, txtfs_Name, txtfs_Marke, txtfs_Tacho, textf_StartResv,
			textf_StopResv, txtf_PathToImportFile;

	@FXML
	private ComboBox<String> combo_Res;

	@FXML
	private ComboBox<Car> cb_PickCarForResv;

	private ArrayList<Tab> list_Tabs;
	private CarList carlist;
	private OracleDataBase orcb;
	private User user;

	/**
	 * Initializiert das komplette Frame
	 * 
	 * @param carlist  Die CarList, welche alle Autos beinhaltet
	 * @param searcher Der Sucher welche zum Durchsuchen der Datenbank benutzt
	 *                 werden soll
	 */
	public void init(CarList carlist, Searcher searcher)
	{
		this.carlist = carlist;

		ManagerInitialize initer = new ManagerInitialize();

		// Auto hinzufügen
		initer.getInitializeAutoAdden().setTxtf_Name(txtf_Name);
		initer.getInitializeAutoAdden().setTxtf_Marke(txtf_Marke);
		initer.getInitializeAutoAdden().setTxtf_Tacho(txtf_Tacho);
		initer.getInitializeAutoAdden().settxtfPathToImportFile(txtf_PathToImportFile);
		initer.getInitializeAutoAdden().setBtnOpenDirectoryChooser(btn_OpenDirectoryChooser);
		initer.getInitializeAutoAdden().setBtnImportCars(btn_ImportCars);
		initer.getInitializeAutoAdden().setbtn_Hinzufügen(btn_Hinzufügen);

		// Overview
		initer.getInitializeOverview().setLabel_Marke(label_Marke);
		initer.getInitializeOverview().setLabel_Name(label_Name);
		initer.getInitializeOverview().setLabel_Tachostand(label_Tachostand);
		initer.getInitializeOverview().setLbl_Res_start(lbl_Res_start);
		initer.getInitializeOverview().setLbl_Res_stop(lbl_Res_stop);
		initer.getInitializeOverview().setCombo_Res(combo_Res);
		initer.getInitializeOverview().setList_Autos(list_Autos);
		initer.getInitializeOverview().setListView_Tabs(listView_Tabs);
		initer.getInitializeOverview().setBtn_Löschen(btn_Löschen);
		initer.getInitializeOverview().setPane_Tabpane(pane_Tabpane);
		initer.getInitializeOverview().setBtnDeleteReservierung(btn_DeleteReservierung);
		initer.getInitializeOverview().setTxtf_Marke(txtf_Marke);
		initer.getInitializeOverview().setTxtf_Name(txtf_Name);
		initer.getInitializeOverview().setTxtf_Tacho(txtf_Tacho);

		// Search
		initer.getInitializerSearch().setTxtfs_Name(txtfs_Name);
		initer.getInitializerSearch().setTxtfs_Marke(txtfs_Marke);
		initer.getInitializerSearch().setTxtfs_Tacho(txtfs_Tacho);
		initer.getInitializerSearch().setRb_Marke(rb_Marke);
		initer.getInitializerSearch().setRb_Name(rb_Name);
		initer.getInitializerSearch().setRb_Tacho(rb_Tacho);
		initer.getInitializerSearch().setLbl_SearchName(lbl_SearchName);
		initer.getInitializerSearch().setLbl_SearchMarke(lbl_SearchMarke);
		initer.getInitializerSearch().setLbl_SearchTacho(lbl_SearchTacho);
		initer.getInitializerSearch().setList_SearchResults(list_SearchResults);
		initer.getInitializerSearch().setBtn_StartSearch(btn_StartSearch);
		initer.getInitializerSearch().setSearcher(searcher);

		// Reservieren
		initer.getInitializerReservieren().setCb_PickCarForResv(cb_PickCarForResv);
		initer.getInitializerReservieren().setBtn_AddResv(btn_AddResv);
		initer.getInitializerReservieren().setDate_Start(date_Start);
		initer.getInitializerReservieren().setDate_Stop(date_Stop);
		initer.getInitializerReservieren().setTextf_StartResv(textf_StartResv);
		initer.getInitializerReservieren().setTextf_StopResv(textf_StopResv);

		initer.init(this, orcb, carlist);

		lbl_Benutzername.setText(user.getBenutzerName());
		lbl_RechteLVL.setText(user.getRechteLvl() + "");

		// ArrayList
		list_Tabs = new ArrayList<>();

		// LISTEVIEW
		list_Tabs.add(tab_Main);
		list_Tabs.add(tab_Hinzufügen);
		list_Tabs.add(tab_Reservieren);
		list_Tabs.add(tab_Suchen);
		listView_Tabs.setItems(FXCollections.observableArrayList(list_Tabs));

		listView_Tabs.getSelectionModel().select(tab_Main);

		list_Autos.setItems(FXCollections.observableArrayList(carlist.getList()));

		if (list_Autos.getItems().size() > 0)
		{
			list_Autos.getSelectionModel().select(0);
		}
	}

	/**
	 * Erstellt das Fenster
	 * 
	 * @return Gibt den Controller für das Fenster zurück
	 */
	public static ALLINONEFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();

			File file = new File(Launcher.class.getResource("/frames/ALLINONEFrame.fxml").getFile());
			FileInputStream fis = new FileInputStream(file);
			Pane pane = loader.load(fis);
			primaryStage = new Stage();
			Scene scene = new Scene(pane);
			scene.getStylesheets().add("NoTabs.css");
			primaryStage.setScene(scene);

			ALLINONEFrameController controller = loader.getController();

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
	 * Zeigt eine Fehlermeldung auf dem Frame an
	 * 
	 * @param string Die Fehlermeldung welche angezeigt werden soll
	 */
	public void showErrorMessage(String string)
	{
		logger.warn(string);
		lbl_ShowErrorMsg.setText(string);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						lbl_ShowErrorMsg.setText("");
					}
				});
			}
		}).start();
	}

	/**
	 * Akutalisiert die Liste die im Frame angezeigt wird
	 */
	public void updateList()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				list_Autos.getItems().clear();
				list_Autos.setItems(FXCollections.observableArrayList(carlist.getList()));
			}
		});
	}

	/**
	 * Die Liste wird neu aus der Datenbank ausgelesen
	 */
	public void updateListFomDatabase()
	{
		carlist.getList().clear();
		carlist.getList().addAll(orcb.loadDatabase());
		updateList();
	}

	/**
	 * Setzt den Tab auf eine der
	 * 
	 * @param i
	 */
	public void setTab(int i)
	{
		listView_Tabs.getSelectionModel().select(i);
	}

	/**
	 * 
	 * @return Gibt die Stage zurück, auf welchem das Frame läuft
	 */
	public Window getStage()
	{
		return primaryStage;
	}

	/**
	 * Setzt die Datenbank
	 * 
	 * @param database Die initialisierte Datenbank
	 */
	public void setOrcb(OracleDataBase database)
	{
		orcb = database;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
