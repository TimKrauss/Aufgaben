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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class SearcherFrameController
{
	@FXML
	private Label lbl_Marke, lbl_Tacho, lbl_Error;

	@FXML
	private ComboBox<Car> cb_Select;

	@FXML
	private Button btn_StartSearch, btn_SelectCarInList;

	@FXML
	private CheckBox rb_Name, rb_Marke, rb_Tacho;

	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho;

	private static Stage primaryStage;
	private static Logger logger = Logger.getLogger("System");
	private Searcher searcher = new Searcher();

	/**
	 * Erstellt das Such-Fenster
	 * 
	 * @return den Controller für das erzeugte Fenster
	 */
	public static SearcherFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();

			File file = new File(Launcher.class.getResource("/de/krauss/gfx/SearcherFrame.fxml").getFile());
			FileInputStream fis = new FileInputStream(file);
			AnchorPane pane = loader.load(fis);
			primaryStage = new Stage();
			primaryStage.setScene(new Scene(pane));
			SearcherFrameController controller = loader.getController();

			primaryStage.setTitle("Searcher");
			primaryStage.setResizable(false);
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
	 * @param orcb Setzt die Datenbank
	 */
	public void setDatabase(OracleDataBase orcb)
	{
		searcher.setOrcb(orcb);
	}

	/**
	 * Initaliziert die Elemente auf dem Frame
	 */
	public void init(ListView<Car> list, CarList l)
	{
		btn_StartSearch.setDisable(false);

		btn_SelectCarInList.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				Car selectedCar = cb_Select.getSelectionModel().getSelectedItem();
				list.getSelectionModel().clearSelection();
				list.getFocusModel().focusNext();

				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(250);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}

						list.getSelectionModel().select(selectedCar);
						list.getFocusModel().focus(list.getSelectionModel().getSelectedIndex());
						list.scrollTo(selectedCar);
					}
				});
			}
		});

		rb_Name.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtf_Name.setDisable(!newValue);
			}
		});

		rb_Marke.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtf_Marke.setDisable(!newValue);
			}
		});

		rb_Tacho.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				txtf_Tacho.setDisable(!newValue);
			}
		});

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
					showErrorMessage("Kein Suchkriterium vorhanden!");
					return;
				}

				// TACHO
				if (rb_Tacho.isSelected())
				{
					try
					{
						String shouldBeNummer = txtf_Tacho.getText();

						if (shouldBeNummer.equals(""))
						{
							showErrorMessage("Bitte eine Nummer eingeben!");
							return;
						}

						Integer.parseInt(shouldBeNummer);
						tachoStand = shouldBeNummer;

					} catch (NumberFormatException e)
					{
						showErrorMessage("Bitte den Tachostand gültig eingeben");
						return;
					}
				}

				// NAME
				if (rb_Name.isSelected())
				{
					String shouldBeName = txtf_Name.getText();

					if (shouldBeName.equals(""))
					{
						showErrorMessage("Bitte einen Namen eingeben");
						return;
					}
					autoName = shouldBeName;
				}

				// Marke
				if (rb_Marke.isSelected())
				{
					String shouldBeMarke = txtf_Marke.getText();

					if (shouldBeMarke.equals(""))
					{
						showErrorMessage("Bitte eine Marke eingeben");
						return;
					}
					autoMarke = shouldBeMarke;
				}

				ArrayList<Car> foundCars = searcher.searchAll(autoName, autoMarke, tachoStand);
				autoName = "";
				autoMarke = "";
				tachoStand = "";
				setComboBoxInhalt(foundCars);
			}
		});

		cb_Select.setConverter(new StringConverter<Car>()
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

		cb_Select.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
		{
			if (newValue == null)
			{
				lbl_Marke.setText("");
				lbl_Tacho.setText("");
				return;
			}

			lbl_Marke.setText(newValue.getCarMarke());
			lbl_Tacho.setText(newValue.getCarTacho() + "");
		});
	}

	/**
	 * Schließt das Fenster
	 */
	@FXML
	public void closeWindow()
	{
		primaryStage.hide();
	}

	/**
	 * Füllt die ComboBox mit den Autos aus der mitgegebenen Liste
	 * 
	 * @param cars Die Liste mit den Autos
	 */
	public void setComboBoxInhalt(ArrayList<Car> cars)
	{
		if (cars.size() == 0)
		{
			showErrorMessage("Kein Auto mit diesen Suchkriterien gefunden");
			cb_Select.setItems(FXCollections.observableArrayList());
			return;
		}

		cb_Select.setDisable(false);
		cb_Select.setItems(FXCollections.observableArrayList(cars));
		cb_Select.getSelectionModel().select(0);
	}

	/**
	 * Zeigt eine Fehlermeldung auf den Frame ein
	 * 
	 * @param txt Die anzuzeigende Fehlermeldung
	 */
	private void showErrorMessage(String txt)
	{
		lbl_Error.setText(txt);
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
					logger.warn(e.getLocalizedMessage());
				}
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						lbl_Error.setText("");
					}
				});
			}
		}).start();
	}
}
