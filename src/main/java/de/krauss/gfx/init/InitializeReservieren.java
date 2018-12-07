package de.krauss.gfx.init;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import de.krauss.car.Car;
import de.krauss.car.CarList;
import de.krauss.car.Reservierung;
import de.krauss.gfx.ALLINONEFrameController;
import de.krauss.utils.OracleDataBase;
import de.krauss.utils.Utilities;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class InitializeReservieren
{
	private TextField textf_StartResv, textf_StopResv;
	private DatePicker date_Start, date_Stop;
	private Button btn_AddResv;
	private CarList carlist;
	private ComboBox<Car> cb_PickCarForResv;

	public void init(ALLINONEFrameController controller, OracleDataBase orcb, CarList list)
	{
		carlist = list;
		initCbPickCarForResv();
		initBtnAddResv(controller, orcb);
	}

	/**
	 * Initialisiert die ComboBox welche die Autos beinhaltet die man Reservieren
	 * kann/möchte
	 */
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

	/**
	 * Initialisiert den Button, welcher zum Hinzufügen einer Reservierung betätigt
	 * wird
	 * 
	 * @param controller Der ALLINONEFrameController welcher das Window kontrolliert
	 * @param orcb       Die Datenbank in welche die Reservierung hochgeladen werden
	 *                   soll
	 */
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

				Car car = cb_PickCarForResv.getSelectionModel().getSelectedItem();

				if (car == null)
				{
					controller.showErrorMessage("Kein Auto wurde ausgewählt");
					return;
				}

				// OWNER SETZEN
				resv.setOwner("Tim");

				if (Utilities.isCarAvaible(start_Date, stop_Date, car, controller))
				{
					carlist.addReservierung(resv, car);

					textf_StartResv.setText("");
					textf_StopResv.setText("");
					date_Start.setValue(null);
					date_Stop.setValue(null);
					controller.updateList();
					controller.setTab(0);
				}
				return;
			}
		});
	}

	//
	// SETTER
	//

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

	public void setBtn_AddResv(Button btn_AddResv)
	{
		this.btn_AddResv = btn_AddResv;
	}

	public void setCb_PickCarForResv(ComboBox<Car> cb_PickCarForResv)
	{
		this.cb_PickCarForResv = cb_PickCarForResv;
	}

	public ComboBox<Car> getCb_PickCarForResv()
	{
		return cb_PickCarForResv;
	}

}
