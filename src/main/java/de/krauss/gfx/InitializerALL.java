package de.krauss.gfx;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.OracleDataBase;
import de.krauss.Reservierung;
import de.krauss.Utilities;
import de.krauss.search.Searcher;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class InitializerALL
{

	private ListView<Car> list_Autos, list_SearchResults;

	private ListView<Tab> listView_Tabs;

	private Button btn_Löschen, btn_Hinzufügen, btn_AddResv, btn_StartSearch;

	private Label label_Name, label_Marke, label_Tachostand, lbl_Res_start, lbl_Res_stop, lbl_SearchName,
			lbl_SearchMarke, lbl_SearchTacho;

	private ComboBox<String> combo_Res;
	private ComboBox<Car> cb_PickCarForResv; // VOR RESV BUTTON

	private DatePicker date_Start, date_Stop;

	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");

	private CarList carlist;
	private Searcher searcher;

	private TabPane pane_Tabpane;

	private TextField txtf_Name, txtf_Marke, txtf_Tacho, txtfs_Name, txtfs_Marke, txtfs_Tacho, textf_StartResv,
			textf_StopResv;

	private CheckBox rb_Name, rb_Marke, rb_Tacho;

	private Logger logger = Logger.getLogger("System");

	/**
	 * Startet die Initaliasierung von Liste,ComboBox, den Button reslöschen
	 */
	public void init(ALLINONEFrameController controller, OracleDataBase orcb)
	{
		initListAutos();
		initComboResv();
		initButtonAdd(controller);
		initButtonLöschen(controller);
		initCheckbox();
		initListTabs();
		initListSearchResults();
		initCbPickCarForResv();
		initBtnAddResv(controller, orcb);
		initBtnStartSearch(controller);
	}

	private void initBtnStartSearch(ALLINONEFrameController controller)
	{
		btn_StartSearch.setOnAction(new EventHandler<ActionEvent>()
		{
			String tachoStand = "";
			String autoName = "";
			String autoMarke = "";

			@Override
			public void handle(ActionEvent arg0)
			{
				if (!rb_Name.isSelected() && !rb_Marke.isSelected() && !rb_Tacho.isSelected())
				{
					controller.showErrorMessage("Kein Suchkriterium vorhanden!");
					return;
				}

				// TACHO
				if (rb_Tacho.isSelected())
				{
					try
					{
						String shouldBeNummer = txtfs_Tacho.getText();

						if (shouldBeNummer.equals(""))
						{
							controller.showErrorMessage("Bitte eine Nummer eingeben!");
							return;
						}

						Integer.parseInt(shouldBeNummer);
						tachoStand = shouldBeNummer;

					} catch (NumberFormatException e)
					{
						controller.showErrorMessage("Bitte den Tachostand gültig eingeben");
						return;
					}
				}

				// NAME
				if (rb_Name.isSelected())
				{
					String shouldBeName = txtfs_Name.getText();

					if (shouldBeName.equals(""))
					{
						controller.showErrorMessage("Bitte einen Namen eingeben");
						return;
					}
					autoName = shouldBeName;
				}

				// Marke
				if (rb_Marke.isSelected())
				{
					String shouldBeMarke = txtfs_Marke.getText();

					if (shouldBeMarke.equals(""))
					{
						controller.showErrorMessage("Bitte eine Marke eingeben");
						return;
					}
					autoMarke = shouldBeMarke;
				}
				ArrayList<Car> foundCars = searcher.searchAll(autoName, autoMarke, tachoStand);
				autoName = "";
				autoMarke = "";
				tachoStand = "";
				list_SearchResults.setItems(FXCollections.observableArrayList(foundCars));
			}
		});
	}

	private void initCbPickCarForResv()
	{
		cb_PickCarForResv.setConverter(new StringConverter<Car>()
		{
			@Override
			public String toString(Car object)
			{
				return object.getCarName();
			}

			@Override
			public Car fromString(String string)
			{
				return null;
			}
		});
	}

	private void initBtnAddResv(ALLINONEFrameController controller, OracleDataBase orcb)
	{
		btn_AddResv.setOnAction(new EventHandler<ActionEvent>()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event)
			{
				LocalDate local_Start = date_Start.getValue();
				LocalDate local_Stop = date_Stop.getValue();

				String txtFromStart = textf_StartResv.getText();
				String txtFromStop = textf_StopResv.getText();

				int start_Hours = 0;
				int start_Minutes = 0;

				int stop_Hours = 0;
				int stop_Minutes = 0;

				if (txtFromStart.equals("") || !txtFromStart.contains(":") || txtFromStop.equals("")
						|| !txtFromStop.contains(":") || local_Start == null || local_Stop == null)
				{
					controller.showErrorMessage("Überprüfe bitte nochmal alle Eingaben");
					return;
				}

				String[] txtSplitedStart = txtFromStart.split(":");

				String[] txtSplitedStop = txtFromStop.split(":");

				try
				{
					start_Hours = Integer.parseInt(txtSplitedStart[0]);
					if (start_Hours < 0 || start_Hours > 23)
					{
						controller.showErrorMessage("Startdatum: Bitte die Stunden gültig eingeben");
						return;
					}

					start_Minutes = Integer.parseInt(txtSplitedStart[1]);
					if (start_Minutes < 0 || start_Minutes > 59)
					{
						controller.showErrorMessage("Startdatum: Bitte die Minuten gültig eingeben");
						return;
					}

					stop_Hours = Integer.parseInt(txtSplitedStop[0]);
					if (stop_Hours < 0 || stop_Hours > 23)
					{
						controller.showErrorMessage("Stopdatum: Bitte die Stunden gültig eingeben");
						return;
					}

					stop_Minutes = Integer.parseInt(txtSplitedStop[1]);
					if (stop_Minutes < 0 || stop_Minutes > 59)
					{
						controller.showErrorMessage("Stopdatum: Bitte die Minuten gültig eingeben");
						return;
					}

				} catch (NumberFormatException e)
				{
					controller.showErrorMessage("Bitte die Uhrzeiten gültig eingeben");
					return;
				}

				Instant insta_Start = Instant.from(local_Start.atStartOfDay(ZoneId.systemDefault()));
				Date start_Date = Date.from(insta_Start);
				start_Date.setHours(start_Hours);
				start_Date.setMinutes(start_Minutes);

				Instant insta_Stop = Instant.from(local_Stop.atStartOfDay(ZoneId.systemDefault()));
				Date stop_Date = Date.from(insta_Stop);
				stop_Date.setHours(stop_Hours);
				stop_Date.setMinutes(stop_Minutes);

				Reservierung resv = new Reservierung(start_Date, stop_Date);

				// OWNER SETZEN
				resv.setOwner("Tim");

				if (Utilities.isCarAvaible(start_Date, stop_Date,
						cb_PickCarForResv.getSelectionModel().getSelectedItem(), controller))
				{
					carlist.addReservierung(resv, cb_PickCarForResv.getSelectionModel().getSelectedItem());

					textf_StartResv.setText("");
					textf_StopResv.setText("");
					date_Start.setValue(null);
					date_Stop.setValue(null);
					controller.setTab(0);
				}

				return;
			}
		});
	}

	private void initListSearchResults()
	{
		list_SearchResults.setCellFactory(param -> new ListCell<Car>()
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

		list_SearchResults.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Car>()
		{
			public void changed(ObservableValue<? extends Car> observable, Car oldValue, Car newValue)
			{
				if (list_SearchResults.getSelectionModel().getSelectedIndex() == -1)
				{
					lbl_SearchName.setText("");
					lbl_SearchMarke.setText("");
					lbl_SearchTacho.setText("");
				} else
				{
					lbl_SearchName.setText(newValue.getCarName());
					lbl_SearchMarke.setText(newValue.getCarMarke());
					lbl_SearchTacho.setText(newValue.getCarTacho() + "");
				}
			}
		});

	}

	private void initCheckbox()
	{
		rb_Name.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtfs_Name.setDisable(!newValue);
			}
		});

		rb_Marke.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtfs_Marke.setDisable(!newValue);
			}
		});

		rb_Tacho.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtfs_Tacho.setDisable(!newValue);
			}
		});
	}

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
				controller.updateList();
			}
		});
	}

	private void initButtonAdd(ALLINONEFrameController controller)
	{
		btn_Hinzufügen.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String name = txtf_Name.getText();

				if (name.equals("") || name.equals(null))
				{
					controller.showErrorMessage("Bitte einen Namen angeben!");
					return;
				}

				String marke = txtf_Marke.getText();

				if (marke.equals("") || marke.equals(null))
				{
					controller.showErrorMessage("Bitte eine Marke angeben!");
					return;
				}

				String tacho = txtf_Tacho.getText();

				if (tacho.equals("") || tacho.equals(null))
				{
					controller.showErrorMessage("Bitte einen Tachostand angeben!");
					return;
				}

				try
				{
					int int_Tacho = Integer.parseInt(tacho);

					Car c = new Car();
					c.setCarName(name);
					c.setCarMarke(marke);
					c.setCarTacho(int_Tacho);
					c.setCarID(0);

					// DATENANK + LOKAL
					carlist.addCar(c);

					controller.updateList();
					controller.setTab(0);
					txtf_Marke.setText("");
					txtf_Name.setText("");
					txtf_Tacho.setText("");
					// Auto hinzufügen
				} catch (NumberFormatException e)
				{
					controller.showErrorMessage("Bitte einen gültigen Tachostand angeben!");
					return;
				}
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
					cb_PickCarForResv.setItems(FXCollections.observableArrayList(carlist.getList()));
					cb_PickCarForResv.getSelectionModel().select(0);
				}

				pane_Tabpane.getSelectionModel().select(newValue);
			}
		});
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
					return;
				}

				Car c = carlist.getCar(list_Autos.getSelectionModel().getSelectedIndex());
				Reservierung r = c.getReservs().get(opt);
				lbl_Res_start.setText(format.format(r.getResStart()));
				lbl_Res_stop.setText(format.format(r.getResStop()));
			}
		});
	}

	/*
	 * 
	 * 
	 * SETTER UND GETTER
	 * 
	 * 
	 */

	public void setRb_Name(CheckBox rb_Name)
	{
		this.rb_Name = rb_Name;
	}

	public void setRb_Marke(CheckBox rb_Marke)
	{
		this.rb_Marke = rb_Marke;
	}

	public void setRb_Tacho(CheckBox rb_Tacho)
	{
		this.rb_Tacho = rb_Tacho;
	}

	public void setTxtfs_Name(TextField txtfs_Name)
	{
		this.txtfs_Name = txtfs_Name;
	}

	public void setTxtfs_Marke(TextField txtfs_Marke)
	{
		this.txtfs_Marke = txtfs_Marke;
	}

	public void setTxtfs_Tacho(TextField txtfs_Tacho)
	{
		this.txtfs_Tacho = txtfs_Tacho;
	}

	public void setbtn_Hinzufügen(Button button)
	{
		btn_Hinzufügen = button;
	}

	public void setCarlist(CarList car)
	{
		carlist = car;
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

	public void setFormat(SimpleDateFormat format)
	{
		this.format = format;
	}

	public void setListView_Tabs(ListView<Tab> listView_Tabs)
	{
		this.listView_Tabs = listView_Tabs;
	}

	public void setTxtf_Name(TextField txtf_Name)
	{
		this.txtf_Name = txtf_Name;
	}

	public void setTxtf_Marke(TextField txtf_Marke2)
	{
		this.txtf_Marke = txtf_Marke2;
	}

	public void setTxtf_Tacho(TextField txtf_Tacho2)
	{
		this.txtf_Tacho = txtf_Tacho2;
	}

	public void setList_SearchResults(ListView<Car> list_SearchResults)
	{
		this.list_SearchResults = list_SearchResults;
	}

	public void setBtn_Hinzufügen(Button btn_Hinzufügen)
	{
		this.btn_Hinzufügen = btn_Hinzufügen;
	}

	public void setBtn_AddResv(Button btn_AddResv)
	{
		this.btn_AddResv = btn_AddResv;
	}

	public void setBtn_StartSearch(Button btn_StartSearch)
	{
		this.btn_StartSearch = btn_StartSearch;
	}

	public void setLbl_SearchName(Label lbl_SearchName)
	{
		this.lbl_SearchName = lbl_SearchName;
	}

	public void setLbl_SearchMarke(Label lbl_SearchMarke)
	{
		this.lbl_SearchMarke = lbl_SearchMarke;
	}

	public void setLbl_SearchTacho(Label lbl_SearchTacho)
	{
		this.lbl_SearchTacho = lbl_SearchTacho;
	}

	public void setCb_PickCarForResv(ComboBox<Car> cb_PickCarForResv)
	{
		this.cb_PickCarForResv = cb_PickCarForResv;
	}

	public void setDate_Start(DatePicker date_Start)
	{
		this.date_Start = date_Start;
	}

	public void setDate_Stop(DatePicker date_Stop)
	{
		this.date_Stop = date_Stop;
	}

	public void setTextf_StartResv(TextField textf_StartResv)
	{
		this.textf_StartResv = textf_StartResv;
	}

	public void setTextf_StopResv(TextField textf_StopResv)
	{
		this.textf_StopResv = textf_StopResv;
	}

	public void setSearcher(Searcher searcher)
	{
		this.searcher = searcher;
	}
}
