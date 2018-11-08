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
import de.krauss.search.Searcher;
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
	private Button btn_Löschen, btn_Hinzufügen, btn_AddResv, btn_StartSearch;

	@FXML
	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop, lbl_SearchName,
			lbl_SearchMarke, lbl_SearchTacho;

	@FXML
	private CheckBox rb_Name, rb_Marke, rb_Tacho;

	@FXML
	private DatePicker date_Start, date_Stop;

	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho, txtfs_Name, txtfs_Marke, txtfs_Tacho, textf_StartResv,
			textf_StopResv;

	@FXML
	private ComboBox<String> combo_Res;

	@FXML
	private ComboBox<Car> cb_PickCarForResv;

	private ArrayList<Tab> list_Tabs;
	private CarList carlist;
	private OracleDataBase orcb;

	public void init(CarList carlist)
	{
		this.carlist = carlist;

		InitializerALL initer = new InitializerALL();
		initer.setCarlist(carlist);
		initer.setLabel_Marke(label_Marke);
		initer.setLabel_Name(label_Name);
		initer.setLabel_Tachostand(label_Tachostand);
		initer.setLbl_Res_start(lbl_Res_start);
		initer.setLbl_Res_stop(lbl_Res_stop);
		initer.setCombo_Res(combo_Res);
		initer.setList_Autos(list_Autos);
		initer.setListView_Tabs(listView_Tabs);
		initer.setBtn_Löschen(btn_Löschen);
		initer.setPane_Tabpane(pane_Tabpane);
		initer.setTxtf_Name(txtf_Name);
		initer.setTxtf_Marke(txtf_Marke);
		initer.setTxtf_Tacho(txtf_Tacho);
		initer.setTxtfs_Name(txtfs_Name);
		initer.setTxtfs_Marke(txtfs_Marke);
		initer.setTxtfs_Tacho(txtfs_Tacho);
		initer.setbtn_Hinzufügen(btn_Hinzufügen);
		initer.setRb_Marke(rb_Marke);
		initer.setRb_Name(rb_Name);
		initer.setRb_Tacho(rb_Tacho);
		initer.setLbl_SearchName(lbl_SearchName);
		initer.setLbl_SearchMarke(lbl_SearchMarke);
		initer.setLbl_SearchTacho(lbl_SearchTacho);
		initer.setList_SearchResults(list_SearchResults);
		initer.setCb_PickCarForResv(cb_PickCarForResv);
		initer.setBtn_AddResv(btn_AddResv);
		initer.setBtn_StartSearch(btn_StartSearch);
		initer.setDate_Start(date_Start);
		initer.setDate_Stop(date_Stop);
		initer.setTextf_StartResv(textf_StartResv);
		initer.setTextf_StopResv(textf_StopResv);
		Searcher searcher = new Searcher();
		searcher.setOrcb(orcb);
		initer.setSearcher(searcher);

		initer.init(this, orcb);

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

	public void setOrcb(OracleDataBase database)
	{
		orcb = database;
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

			File file = new File(Launcher.class.getResource("/de/krauss/gfx/ALLINONEFrame.fxml").getFile());
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
	}

	/**
	 * Akutalisiert die Liste die im Frame angezeigt wird
	 */
	public void updateList()
	{
		list_Autos.setItems(FXCollections.observableArrayList(carlist.getList()));
	}

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
}
