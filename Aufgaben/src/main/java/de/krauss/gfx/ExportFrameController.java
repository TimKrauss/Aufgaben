package de.krauss.gfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ExportFrameController
{
	@FXML
	private CheckBox check_Dump, check_JSon, check_XML, check_Txt;

	@FXML
	private Button btn_Export, btn_fileChooser;

	@FXML
	private Label lbl_Name;
}
