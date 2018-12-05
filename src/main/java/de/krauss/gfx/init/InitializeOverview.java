package de.krauss.gfx.init;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Reservierung;
import de.krauss.gfx.ALLINONEFrameController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class InitializeOverview
{
	private ListView<Tab> listView_Tabs;
	private TabPane pane_Tabpane;

	private ListView<Car> list_Autos;

	private Button btn_Löschen, btn_DeleteReservierung;

	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop;

	private ComboBox<String> combo_Res;

	private CarList carlist;

	private TextField txtf_Name, txtf_Marke, txtf_Tacho;

	private Logger logger = Logger.getLogger("System");

	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");

	private ManagerInitialize manager;

	public void init(ALLINONEFrameController controller, ManagerInitialize manager, CarList carlist)
	{
		this.manager = manager;
		this.carlist = carlist;
		initListTabs();
		initListAutos();
		initBtnDeleteResv(controller);
		initButtonLöschen(controller);
		initComboResv();
	}

	/**
	 * Initalisiert die Combobox
	 */
	private void initComboResv()
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
					btn_DeleteReservierung.setDisable(true);
					return;
				}
				Car car = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
				Reservierung resv = car.getReservs().get(opt);
				lbl_Res_start.setText(format.format(resv.getResStart()));
				lbl_Res_stop.setText(format.format(resv.getResStop()));
				btn_DeleteReservierung.setDisable(false);
			}
		});
	}

	/**
	 * Initializiert den Button welcher Reservierung löscht
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
	private void initBtnDeleteResv(ALLINONEFrameController controller)
	{
		btn_DeleteReservierung.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				int reservierungToDeleteIndex = combo_Res.getSelectionModel().getSelectedIndex();
				Reservierung resvToDelete = list_Autos.getSelectionModel().getSelectedItem().getReservs()
						.get(reservierungToDeleteIndex);

				Car preSelectedCar = list_Autos.getSelectionModel().getSelectedItem();

				carlist.deleteReservierungFromCar(preSelectedCar, resvToDelete);

				controller.updateList();

				list_Autos.getSelectionModel().select(preSelectedCar);
			}
		});
	}

	/**
	 * Initizalisiert die Liste mit der man zwischen den Tabs springen kann (zb
	 * Übersicht, Suchen,...s
	 */
	private void initListTabs()
	{
		// LIST VIEW TAP
		listView_Tabs.setCellFactory(param -> new ListCell<Tab>()
		{
			@Override
			protected void updateItem(Tab item, boolean empty)
			{
				super.updateItem(item, empty);

				if (empty || item == null || item.getText() == null)
				{
					setText("");
				} else
				{
					setText(item.getText());
				}
			}
		});

		listView_Tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
		{
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue)
			{
				if (newValue.getText().equals("Reservieren"))
				{
					manager.getInitializerReservieren().getCb_PickCarForResv()
							.setItems(FXCollections.observableArrayList(carlist.getList()));
					manager.getInitializerReservieren().getCb_PickCarForResv().getSelectionModel().select(0);
				}

				pane_Tabpane.getSelectionModel().select(newValue);
			}
		});
	}

	/**
	 * Initizalisiert die Liste
	 */
	private void initListAutos()
	{

		list_Autos.setCellFactory(param -> new ListCell<Car>()
		{
			@Override
			protected void updateItem(Car item, boolean empty)
			{
				super.updateItem(item, empty);

				if (empty || item == null || item.getCarName() == null)
				{
					setText("");
				} else
				{
					setText(item.getCarName());
				}
			}
		});

		list_Autos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Car>()
		{
			public void changed(ObservableValue<? extends Car> observable, Car oldValue, Car newValue)
			{
				int option = list_Autos.getSelectionModel().getSelectedIndex();
				if (option == -1)
				{
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							if (!list_Autos.getItems().isEmpty())
							{
								list_Autos.getSelectionModel().select(0);
								return;
							}

							logger.info(option);
							combo_Res.setItems(FXCollections.observableArrayList());
							combo_Res.setDisable(true);
							btn_Löschen.setDisable(true);
							txtf_Marke.setText("");
							txtf_Name.setText("");
							txtf_Tacho.setText("");
							combo_Res.setItems(FXCollections.observableArrayList());
						}
					});
					return;
				}
				btn_Löschen.setDisable(false);
				Car car = newValue;

				ArrayList<String> res_Name = new ArrayList<>();

				combo_Res.getItems().clear();
				combo_Res.getSelectionModel().select(-1);

				lbl_Res_start.setText("");
				lbl_Res_stop.setText("");

				if (car.getReservs().size() == 0)
				{
					combo_Res.setDisable(true);
					// btn_Reslöschen.setDisable(true);
				} else
				{
					combo_Res.setDisable(false);
					// btn_Reslöschen.setDisable(false);
				}

				for (int count = 0; count < car.getReservs().size(); count++)
				{
					res_Name.add((count + 1) + "# Reservierung");
				}

				combo_Res.setItems(FXCollections.observableArrayList(res_Name));
				combo_Res.getSelectionModel().select(0);
				label_Name.setText(car.getCarName());
				label_Marke.setText(car.getCarMarke());
				label_Tachostand.setText(car.getCarTacho() + "");
				// btn_Löschen.setDisable(false);
				// btn_Reservieren.setDisable(false);

			}
		});

	}

	/**
	 * Initialisiert den Button welcher zum Löschen von Autos betätigt wird
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
	private void initButtonLöschen(ALLINONEFrameController controller)
	{
		btn_Löschen.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				//
				if (list_Autos.getSelectionModel().getSelectedIndex() == -1)
				{
					logger.warn("Kein Auto zum löschen ausgewählt");
					return;
				}
				carlist.deleteCar(list_Autos.getSelectionModel().getSelectedItem());
				label_Name.setText("");
				label_Marke.setText("");
				label_Tachostand.setText("");
				combo_Res.setItems(FXCollections.observableArrayList());
				controller.updateList();
			}
		});
	}

	//
	// SETTER
	//

	public void setListView_Tabs(ListView<Tab> listView_Tabs)
	{
		this.listView_Tabs = listView_Tabs;
	}

	public void setPane_Tabpane(TabPane tabPane)
	{
		this.pane_Tabpane = tabPane;
	}

	public void setList_Autos(ListView<Car> list_Autos)
	{
		this.list_Autos = list_Autos;
	}

	public void setBtn_Löschen(Button btn_Löschen)
	{
		this.btn_Löschen = btn_Löschen;
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

	public void setTxtf_Marke(TextField txtf_Marke)
	{
		this.txtf_Marke = txtf_Marke;
	}

	public void setTxtf_Name(TextField txtf_Name)
	{
		this.txtf_Name = txtf_Name;
	}

	public void setTxtf_Tacho(TextField txtf_Tacho)
	{
		this.txtf_Tacho = txtf_Tacho;
	}

	public void setBtnDeleteReservierung(Button btn_DeleteReservierung)
	{
		this.btn_DeleteReservierung = btn_DeleteReservierung;
	}
}
