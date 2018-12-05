package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.krauss.Launcher;
import de.krauss.UserManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RegisterFrameController
{
	@FXML
	private TextField txtf_Username;

	@FXML
	private PasswordField pw_Password, pw_CheckPassword;

	@FXML
	private Button btn_Registrieren;

	@FXML
	private ProgressBar pb_PasswordSecure;

	private static Logger logger = Logger.getLogger("System");

	public static RegisterFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();

			File file = new File(Launcher.class.getResource("/de/krauss/gfx/RegisterFrame.fxml").getFile());
			FileInputStream fis = new FileInputStream(file);
			Pane pane = loader.load(fis);
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);

			RegisterFrameController controller = loader.getController();

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

	public void init(UserManager userManager, Stage toShowAfter)
	{
		double addLevel = 0.2;

		btn_Registrieren.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String username = txtf_Username.getText();

				if (username.equals("") || username.contains(" "))
				{
					showErrorMessage("Der Benutzername ist ungültig");
					return;
				}

				String password = pw_Password.getText();

				if (password.equals(""))
				{
					showErrorMessage("Bitte ein Passwort angeben");
					return;
				}

				if (!password.equals(pw_CheckPassword.getText()))
				{
					showErrorMessage("Passwörter stimmen nicht über ein");
					return;
				}

				// PASSWORT HASHEN TODO
				userManager.addUser(username, userManager.hashPasswort(password), 0);
				toShowAfter.show();
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});

		pw_Password.textProperty().addListener((observable, oldValue, newValue) ->
		{
			double safety = 0;

			if (newValue.length() > 7)
			{
				safety += addLevel;
				System.out.println("20 PROZENT");
			}

			if (!newValue.equals(newValue.toLowerCase()))
			{
				safety += addLevel;
			}
			pb_PasswordSecure.setProgress(safety);
		});

	}

	public void showErrorMessage(String message)
	{
		logger.info(message);
	}
}
