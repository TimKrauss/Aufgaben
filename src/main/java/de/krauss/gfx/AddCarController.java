package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddCarController
{
	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho;

	private CarList carlist;

	@FXML
	private Button btn_Add;

	@FXML
	private Label lblError;

	/**
	 * 
	 * Listener hinzu
	 * 
	 * @param con Setzt die Instanz des MainframeControllers
	 */
	public void addListenerToButton(MainFrameController con)
	{
		btn_Add.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String name = txtf_Name.getText();

				if (name.equals("") || name.equals(null))
				{
					showErrorMessage("Bitte einen Namen angeben!");
					return;
				}

				String marke = txtf_Marke.getText();

				if (marke.equals("") || marke.equals(null))
				{
					showErrorMessage("Bitte eine Marke angeben!");
					return;
				}

				String tacho = txtf_Tacho.getText();

				if (tacho.equals("") || tacho.equals(null))
				{
					showErrorMessage("Bitte einen Tachostand angeben!");
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

					con.setList(carlist.getList());
					((Node) (event.getSource())).getScene().getWindow().hide();

					// Auto hinzufügen
				} catch (NumberFormatException e)
				{
					showErrorMessage("Bitte einen gültigen Tachostand angeben!");
					return;
				}
			}
		});

	}

	/**
	 * Erzeugt eine Instanz des Fensters
	 * 
	 * @return Gibt den Controller für das Fenster zurück
	 */
	public static AddCarController createWindow()
	{
		try
		{
			// Load FXML File
			FXMLLoader loader = new FXMLLoader();
			FileInputStream fis = new FileInputStream(
					new File((Launcher.class.getResource("/de/krauss/gfx/AddCar.fxml").getFile())));
			Parent root = loader.load(fis);
			AddCarController controll = loader.getController();

			// Create Stage & Show
			Stage stage = new Stage();
			stage.setTitle("Auto hinzufügen");
			stage.setAlwaysOnTop(true);
			stage.getIcons().add(new Image("car.png"));
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();

			fis.close();
			return controll;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gibt auf dem AddCarFrame eine Fehlermeldung aus
	 * 
	 * @param txt Die Fehlermeldung
	 */
	public void showErrorMessage(String txt)
	{
		lblError.setText(txt);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(3000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						lblError.setText("");
					}
				});
			}

		}).start();
	}

	/**
	 * Setzt die neue Carlist
	 * 
	 * @param carlist Setzt die neue Carlist
	 */
	public void setCarlist(CarList carlist)
	{
		this.carlist = carlist;
	}
}
