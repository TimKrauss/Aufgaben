package de.krauss.gfx;

import javax.swing.JOptionPane;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddCarController
{
	@FXML
	private TextField txtf_Name, txtf_Marke, txtf_Tacho;

	private FileManager fm;
	private CarList carlist;

	@FXML
	Button btn_Add;

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

					fm.safeCarToDB(c);
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
	 * Setzt den Filemanger
	 * 
	 * @param fm Der neue Filemanager
	 */
	public void setFm(FileManager fm)
	{
		this.fm = fm;
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
