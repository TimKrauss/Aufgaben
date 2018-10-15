package de.krauss.gfx;

import java.io.File;

import de.krauss.CarList;
import de.krauss.handler.DumpFileHandler;
import de.krauss.handler.JSonFileHandler;
import de.krauss.handler.TxtFileHandler;
import de.krauss.handler.XStreamFileHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ExportFrameController
{
	@FXML
	private CheckBox check_Dump, check_JSon, check_XML, check_Txt;

	@FXML
	private Button btn_Export, btn_fileChooser;

	@FXML
	private TextField txtf_Name;

	private Stage stage;
	private File expoDir;
	private CarList carlist;

	private DumpFileHandler dumpFile;
	private JSonFileHandler jsonFile;
	private TxtFileHandler txtFile;
	private XStreamFileHandler xStreamFile;

	@FXML
	public void exportieren()
	{
//

		if (expoDir == null)
		{
			return;
		}

		String fName = txtf_Name.getText();

		if (check_Dump.isSelected())
		{
			dumpFile.safe(carlist, new File(expoDir.getAbsolutePath() + "/" + fName + ".DUMP"));
		}
		if (check_JSon.isSelected())
		{
			jsonFile.safe(carlist, new File(expoDir.getAbsolutePath() + "/" + fName + ".json"));
		}
		if (check_Txt.isSelected())
		{
			txtFile.safe(carlist, new File(expoDir.getAbsolutePath() + "/" + fName + ".txt"));
		}
		if (check_XML.isSelected())
		{
			xStreamFile.safe(carlist, new File(expoDir.getAbsolutePath() + "/" + fName + ".xml"));
		}

	}

	public void init()
	{
		btn_fileChooser.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("SpeicherOrt auswählen");
				expoDir = chooser.showDialog(stage);
			}
		});
	}

	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

	public void setCarlist(CarList carlist)
	{
		this.carlist = carlist;
	}

}
