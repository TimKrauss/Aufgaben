package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.krauss.Launcher;
import de.krauss.User;
import de.krauss.UserManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginFrameController
{
	@FXML
	private TextField txtf_Username;

	@FXML
	private PasswordField pw_Password; // HASH - SPEICHERN NIE ENTSCHLÜSSELN

	@FXML
	private Button btn_Login;

	@FXML
	private Label lbl_Registrieren;

	private static Stage primaryStage;
	private static Logger logger = Logger.getLogger("System");

	public static LoginFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			primaryStage = new Stage();

			File file = new File(Launcher.class.getResource("/de/krauss/gfx/LoginFrame.fxml").getFile());
			FileInputStream fis = new FileInputStream(file);
			Pane pane = loader.load(fis);
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);

			LoginFrameController controller = loader.getController();

			primaryStage.setTitle("Login");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent arg0)
				{
					System.exit(1);
				}
			});
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

	public void init(UserManager manager, Launcher launcher)
	{
		btn_Login.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String username = txtf_Username.getText();

				if (username.equals("") || username.contains(" "))
				{
					showErrorMessage("Username ungültig");
					return;
				}

				String passwordText = pw_Password.getText();

				if (passwordText.equals(""))
				{
					showErrorMessage("Bitte ein Passwort eingeben");
					return;
				}

				// TODO Do Login
				User user = manager.login(username, manager.hashPasswort(passwordText));

				if (user == null)
				{
					showErrorMessage("Ungültige Angabe der LoginDaten");
					return;
				}

				launcher.setUser(user);
				launcher.startAllInOneFrame();
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});

		lbl_Registrieren.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				RegisterFrameController controller = RegisterFrameController.createWindow();
				controller.init(manager, primaryStage);
				((Node) (e.getSource())).getScene().getWindow().hide();
			}
		});
	}

	public void showErrorMessage(String errorMsg)
	{
		logger.info(errorMsg);
	}
}
