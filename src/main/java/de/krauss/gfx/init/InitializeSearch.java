package de.krauss.gfx.init;

import java.util.ArrayList;

import de.krauss.car.Car;
import de.krauss.gfx.ALLINONEFrameController;
import de.krauss.search.Searcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class InitializeSearch
{
	private ListView<Car> list_SearchResults;

	private Label lbl_SearchName, lbl_SearchMarke, lbl_SearchTacho;

	private CheckBox rb_Name, rb_Marke, rb_Tacho;

	private TextField txtfs_Name, txtfs_Marke, txtfs_Tacho;

	private Searcher searcher;

	private Button btn_StartSearch;

	public void init(ALLINONEFrameController controller)
	{
		initListSearchResults();
		initCheckbox();
		initBtnStartSearch(controller);
	}

	/**
	 * Initializiert den Button welcher die Suche startet
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
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

	/**
	 * Initialisiert die Liste mit den Suchergebnissen
	 */
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

	/**
	 * Initialisiert die Checkboxen NAME, MARKE, TACHO
	 */
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

	//
	//
	// SETTER
	//
	//
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

	public void setList_SearchResults(ListView<Car> list_SearchResults)
	{
		this.list_SearchResults = list_SearchResults;
	}

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

	public void setBtn_StartSearch(Button btn_StartSearch)
	{
		this.btn_StartSearch = btn_StartSearch;
	}

	public void setSearcher(Searcher searcher)
	{
		this.searcher = searcher;
	}

}
