package de.krauss.gfx;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.OracleDataBase;
import de.krauss.Reservierung;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

public class MainFrameController
{

	@FXML
	private ListView<String> list_Autos;

	@FXML
	private Button btn_Hinzuf�gen, btn_Reservieren, btn_L�schen, btn_Resl�schen, btn_Exportieren;

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
	 * �ffnet das Exportieren-Fenster
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
		initer.setOracleDataBase(orcb);
		initer.init();
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
	 * @param r Reservierung welche hinzugef�gt werden soll
	 */
	public void addReservierungToSelCar(Reservierung r)
	{
		try
		{
			Car c = getSelectedCar();
			r.setCarID(c.getCAR_ID());
			c.addResv(r);
			orcb.uploadRes(r);
		} catch (Exception e)
		{
			logger.info("Kein Auto ausgew�hlt");
		}
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
		controll.setOracleDataBase(orcb);
	}

	/**
	 * L�scht das im GUI ausgew�hlte Auto
	 */
	@FXML
	public void ausgew�hlteL�schen()
	{
		int opt = JOptionPane.showConfirmDialog(null,
				"Wollen sie das Auto '" + getSelectedCar().getF_Name() + "' wirklich endg�ltig l�schen?");
		if (opt == JOptionPane.OK_OPTION)
		{
			carlist.getList().remove(list_Autos.getSelectionModel().getSelectedIndex());
			list_Autos.getSelectionModel().clearSelection();
			orcb.deleteCarFromDatabase(getSelectedCar().getCAR_ID());
			setList(carlist.getList());
		}

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
			logger.warn("Auto hinzugef�gt, dennoch falscher Thread (" + e.getMessage() + ")");
		}
	}

	/**
	 * 
	 * @return Gibt das ausgew�hlte Auto zur�ck
	 */
	public Car getSelectedCar()
	{
		return carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
	}

}
