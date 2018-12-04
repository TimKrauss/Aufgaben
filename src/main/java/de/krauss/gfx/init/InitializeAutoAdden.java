package de.krauss.gfx.init;

import java.io.File;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.gfx.ALLINONEFrameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class InitializeAutoAdden
{
	private Button btn_Hinzufügen, btn_OpenDirectoryChooser, btn_ImportCars;

	private File fileFromChooser;
	private FileManager fileManager = new FileManager();
	private CarList carlist;

	private TextField txtf_Name, txtf_Marke, txtf_Tacho, txtf_PathToImportFile;

	public void init(ALLINONEFrameController controller, CarList carlist)
	{
		this.carlist = carlist;

		initButtonAdd(controller);
		initBtnImportCars(controller);
		initBtnOpenDirectoryChooser(controller);
	}

	/**
	 * Initializiert den Button zum Importieren der Autos
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
	private void initBtnImportCars(ALLINONEFrameController controller)
	{
		btn_ImportCars.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (fileFromChooser == null)
				{
					controller.showErrorMessage("Kein Auto vorhanden");
				}
				carlist.addCars(fileManager.load(FileManager.TXT_FILE, fileFromChooser, carlist));
				controller.updateList();
			}
		});
	}

	/**
	 * Initializiert den Button welcher den DirectoryChooser öffnet
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
	private void initBtnOpenDirectoryChooser(ALLINONEFrameController controller)
	{
		btn_OpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				FileChooser chooser = new FileChooser();
				File file = chooser.showOpenDialog(controller.getStage());

				if (file == null)
					return;

				txtf_PathToImportFile.setText(file.getAbsolutePath());
				fileFromChooser = file;
			}
		});
	}

	/**
	 * Initizalisiert den Button, welcher zum Hinzufügen von Autos betätigt wird
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 */
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

	public void settxtfPathToImportFile(TextField txtf_PathToImportFile)
	{
		this.txtf_PathToImportFile = txtf_PathToImportFile;
	}

	public void setBtnOpenDirectoryChooser(Button btn_OpenDirectoryChooser)
	{
		this.btn_OpenDirectoryChooser = btn_OpenDirectoryChooser;
	}

	public void setbtn_Hinzufügen(Button button)
	{
		btn_Hinzufügen = button;
	}

	public void setCarlist(CarList car)
	{
		carlist = car;
	}

	public void setBtnImportCars(Button btn_ImportCars)
	{
		this.btn_ImportCars = btn_ImportCars;
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

	public void setBtn_Hinzufügen(Button btn_Hinzufügen)
	{
		this.btn_Hinzufügen = btn_Hinzufügen;
	}
}
