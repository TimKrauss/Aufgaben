package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.OracleDataBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddCarController
{
	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho;

	private OracleDataBase orcb;
	private CarList carlist;

	@FXML
	Button btn_Add;

	/**
	 * Fügt dem Button zum Hinzufügen des Autos einen Listener hinzu
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
					JOptionPane.showMessageDialog(null, "Bitte einen Namen angeben!", "Kein Name",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				String marke = txtf_Marke.getText();

				if (marke.equals("") || marke.equals(null))
				{
					JOptionPane.showMessageDialog(null, "Bitte eine Marke angeben!", "Keine Marke",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				String tacho = txtf_Tacho.getText();

				if (tacho.equals("") || tacho.equals(null))
				{
					JOptionPane.showMessageDialog(null, "Bitte einen Tachostand angeben!", "Kein Tachostand",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try
				{
					int int_Tacho = Integer.parseInt(tacho);

					Car c = new Car();
					c.setF_Name(name);
					c.setF_Marke(marke);
					c.setF_Tacho(int_Tacho);

					orcb.addCar(c);
					carlist.addCar(c);
					con.setList(carlist.getList());
					((Node) (event.getSource())).getScene().getWindow().hide();

					// Auto hinzufügen
				} catch (NumberFormatException e)
				{
					JOptionPane.showMessageDialog(null, "Bitte einen GÜLTIGEN Tachostand angeben!",
							"Kein gültiger Tachostand", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
	}

	/**
	 * 
	 * @param c Setzt die Instanz der OracleDatabase
	 */
	public void setOracleDataBase(OracleDataBase c)
	{
		orcb = c;
	}

	public static AddCarController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			FileInputStream fis = new FileInputStream(
					new File((Launcher.class.getResource("/de/krauss/gfx/AddCar.fxml").getFile())));
			Parent root = loader.load(fis);
			AddCarController controll = loader.getController();

			Stage stage = new Stage();
			stage.setTitle("Auto hinzufügen");
			stage.setAlwaysOnTop(true);
			stage.getIcons().add(new Image("car.png"));
			stage.centerOnScreen();
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
	 * Setzt die neue Carlist
	 * 
	 * @param carlist Setzt die neue Carlist
	 */
	public void setCarlist(CarList carlist)
	{
		this.carlist = carlist;
	}
}
