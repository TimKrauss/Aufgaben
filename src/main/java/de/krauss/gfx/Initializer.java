package de.krauss.gfx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Reservierung;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class Initializer
{

	private ListView<String> list_Autos;

	private Button btn_Reservieren, btn_Löschen, btn_Reslöschen, btn_DeleteAll, btn_Update;

	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop;

	private ComboBox<String> combo_Res;

	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");

	private CarList carlist;

	private MainFrameController controller;

	/**
	 * Startet die Initaliasierung von Liste,ComboBox, den Button reslöschen
	 */
	public void init()
	{
		initList();
		initCombo();
		initReslöschen();
		initDeleteAll();
		initUpdate();
	}

	private void initUpdate()
	{
		btn_Update.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				carlist.getList().clear();
				carlist.addCarsFromDataBase();
				controller.setList(carlist.getList());
			}
		});
	}

	private void initDeleteAll()
	{
		btn_DeleteAll.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				carlist.deleteEverything();
				controller.setList(carlist.getList());
			}
		});
	}

	/**
	 * Initizalisiert die Liste
	 */
	private void initList()
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
					btn_Löschen.setDisable(true);
					btn_Reservieren.setDisable(true);
					btn_Reslöschen.setDisable(true);
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
					btn_Reslöschen.setDisable(true);
				} else
				{
					combo_Res.setDisable(false);
					btn_Reslöschen.setDisable(false);
				}

				for (int count = 0; count < c.getReservs().size(); count++)
				{
					res_Name.add((count + 1) + "# Reservierung");
				}

				combo_Res.setItems(FXCollections.observableArrayList(res_Name));
				combo_Res.getSelectionModel().select(0);
				label_Name.setText(c.getCarName());
				label_Marke.setText(c.getCarMarke());
				label_Tachostand.setText(c.getCarTacho() + "");
				btn_Löschen.setDisable(false);
				btn_Reservieren.setDisable(false);
			}
		});
	}

	/**
	 * Fügt dem Reservierung-Löschen-Button eine Aktion hinzu
	 */
	private void initReslöschen()
	{
		btn_Reslöschen.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Car selCar = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
				Reservierung r = selCar.getReservs().get(combo_Res.getSelectionModel().getSelectedIndex());

				// DATENBANK + LOKAL
				carlist.deleteReservierungFromCar(selCar, r);

				controller.setList(carlist.getList());
			}
		});
	}

	/**
	 * Initalisiert die Combobox
	 */
	private void initCombo()
	{
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
	}

	public void setMainFrameController(MainFrameController m)
	{
		controller = m;
	}

	public void setCarlist(CarList car)
	{
		carlist = car;
	}

	public void setList_Autos(ListView<String> list_Autos)
	{
		this.list_Autos = list_Autos;
	}

	public void setBtn_Reservieren(Button btn_Reservieren)
	{
		this.btn_Reservieren = btn_Reservieren;
	}

	public void setBtn_Löschen(Button btn_Löschen)
	{
		this.btn_Löschen = btn_Löschen;
	}

	public void setBtn_Reslöschen(Button btn_Reslöschen)
	{
		this.btn_Reslöschen = btn_Reslöschen;
	}

	public void setLabel_Name(Label label_Name)
	{
		this.label_Name = label_Name;
	}

	public void setLabel_Marke(Label label_Marke)
	{
		this.label_Marke = label_Marke;
	}

	public void setLabel_Tachostand(Label label_Tachostand)
	{
		this.label_Tachostand = label_Tachostand;
	}

	public void setLbl_Res_start(Label lbl_Res_start)
	{
		this.lbl_Res_start = lbl_Res_start;
	}

	public void setLbl_Res_stop(Label lbl_Res_stop)
	{
		this.lbl_Res_stop = lbl_Res_stop;
	}

	public void setCombo_Res(ComboBox<String> combo_Res)
	{
		this.combo_Res = combo_Res;
	}

	public void setFormat(SimpleDateFormat format)
	{
		this.format = format;
	}

	public void setBtn_DeleteAll(Button btn_DeleteAll)
	{
		this.btn_DeleteAll = btn_DeleteAll;
	}

	public void setBtn_Update(Button btn_Update)
	{
		this.btn_Update = btn_Update;
	}

}
