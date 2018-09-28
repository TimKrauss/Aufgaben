package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import de.krauss.Reservierung;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");

	/**
	 * 
	 * @param c Setzt die Carlist
	 */
	public void setCarlist(CarList c)
	{
		carlist = c;
	}

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

	public void setOracleDataBase(OracleDataBase c)
	{
		orcb = c;
	}

	/**
	 * F�gt den Elementen im GUI die Listener hinzu
	 */
	public void init()
	{
		list_Autos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				// change the label text value to the newly selected
				// item.

				int opt = list_Autos.getSelectionModel().getSelectedIndex();
				if (opt == -1)
				{
					combo_Res.setItems(FXCollections.observableArrayList());
					combo_Res.setDisable(true);
					btn_L�schen.setDisable(true);
					btn_Reservieren.setDisable(true);
					btn_Resl�schen.setDisable(true);
					return;
				}
				Car c = carlist.getCar(opt);

				ArrayList<String> res_Name = new ArrayList<>();

				combo_Res.getItems().clear();
				combo_Res.getSelectionModel().select(-1);

				lbl_Res_start.setText("");
				lbl_Res_stop.setText("");

				if (c.getReservs().size() == 0)
				{
					combo_Res.setDisable(true);
					btn_Resl�schen.setDisable(true);
				} else
				{
					combo_Res.setDisable(false);
					btn_Resl�schen.setDisable(false);
				}

				for (int count = 0; count < c.getReservs().size(); count++)
				{
					res_Name.add((count + 1) + "# Reservierung");
				}

				combo_Res.setItems(FXCollections.observableArrayList(res_Name));
				combo_Res.getSelectionModel().select(0);
				label_Name.setText(c.getF_Name());
				label_Marke.setText(c.getF_Marke());
				label_Tachostand.setText(c.getF_Tacho() + "");
				btn_L�schen.setDisable(false);
				btn_Reservieren.setDisable(false);
			}
		});

		combo_Res.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				int opt = combo_Res.getSelectionModel().getSelectedIndex();
				if (opt == -1)
				{
					lbl_Res_start.setText("");
					lbl_Res_stop.setText("");
					return;
				}

				Car c = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
				Reservierung r = c.getReservs().get(opt);
				lbl_Res_start.setText(format.format(r.getResStart()));
				lbl_Res_stop.setText(format.format(r.getResStop()));
			}
		});

		btn_Resl�schen.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Car selCar = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
				Reservierung r = selCar.getReservs().get(combo_Res.getSelectionModel().getSelectedIndex());

				orcb.deleteReservierung(r);
				selCar.getReservs().remove(r);

				carlist.setCars(orcb.loadDatabase());
				setList(carlist.getList());
			}
		});
	}

	/**
	 * Wird durch einen Button von dem GUI angesprochen und startet ein kleineres
	 * Fenster in welchem man eine neue Reserveriung hinzuf�gen kann
	 */
	@FXML
	public void reservieren()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			FileInputStream fis = new FileInputStream(
					new File((Launcher.class.getResource("/de/krauss/gfx/AddResv.fxml").getFile())));
			Parent root = loader.load(fis);
			fis.close();
			AddResvController controll = loader.getController();

			controll.addButtonListener(this);

			Stage stage = new Stage();
			stage.setTitle("Auto hinzuf�gen");
			stage.setAlwaysOnTop(true);
			stage.centerOnScreen();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
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
			Car c = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
			r.setCarID(c.getCAR_ID());
			c.addResv(r);
			orcb.uploadRes(r);
		} catch (Exception e)
		{
			System.out.println("Kein Auto ausgew�hlt");
		}
	}

	/**
	 * Startet ein neues Fenster in welchem man ein neues Auto hinzuf�gen kann
	 */
	@FXML
	public void hinzuf�gen()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			FileInputStream fis = new FileInputStream(
					new File((Launcher.class.getResource("/de/krauss/gfx/AddCar.fxml").getFile())));
			Parent root = loader.load(fis);
			AddCarController controll = loader.getController();

			controll.setCarlist(carlist);
			controll.addListenerToButton(this);
			controll.setOracleDataBase(orcb);
			Stage stage = new Stage();
			stage.setTitle("Auto hinzuf�gen");
			stage.setAlwaysOnTop(true);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent arg0)
				{
//
				}
			});
			stage.centerOnScreen();
			stage.setScene(new Scene(root));
			stage.show();
			fis.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * L�scht das im GUI ausgew�hlte Auto
	 */
	@FXML
	public void ausgew�hlteL�schen()
	{
		Car c = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());

		int opt = JOptionPane.showConfirmDialog(null,
				"Wollen sie das Auto '" + c.getF_Name() + "' wirklich endg�ltig l�schen?");
		if (opt == JOptionPane.OK_OPTION)
		{
			carlist.getList().remove(list_Autos.getSelectionModel().getSelectedIndex());
			list_Autos.getSelectionModel().clearSelection();
			orcb.deleteCarFromDatabase(c.getCAR_ID());
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

	public void setModeJUnit()
	{
		btn_Hinzuf�gen = new Button();
		btn_L�schen = new Button();
		btn_Reservieren = new Button();
		btn_Resl�schen = new Button();

		label_Marke = new Label();
		label_Name = new Label();
		label_Tachostand = new Label();
		lbl_Res_start = new Label();
		lbl_Res_stop = new Label();

		list_Autos = new ListView<String>();

	}

	public Car getSelectedCar()
	{
		return carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
	}

}
