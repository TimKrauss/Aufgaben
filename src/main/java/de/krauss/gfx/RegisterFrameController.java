package de.krauss.gfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.krauss.Launcher;
import de.krauss.user.UserManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
	private Label lbl_ErrorMsg;

	@FXML
	private ProgressBar pb_PasswordSecure;

	private static Logger logger = Logger.getLogger("System");

	public static RegisterFrameController createWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();

			File file = new File(Launcher.class.getResource("/frames/RegisterFrame.fxml").getFile());
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
		double addLevel = 0.25;

		btn_Registrieren.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String username = txtf_Username.getText();

				if (username.equals("") || username.contains(" "))
				{
					showErrorMessage("Der Benutzername ist ung¸ltig");
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
					showErrorMessage("Passwˆrter stimmen nicht ¸ber ein");
					return;
				}

				if (!userManager.existUsername(username))
				{
					userManager.addUser(username, userManager.hashPasswort(password), 0);
					((Node) (event.getSource())).getScene().getWindow().hide();
					toShowAfter.show();
				} else
				{
					showErrorMessage("Der Benutzername existiert schon");
				}

			}
		});

		pw_Password.setTooltip(new Tooltip(
				"Das Passwort muss beinhalten:\n1. Groﬂ- / Kleinbuchstaben\n2. Eine Zahl\n3.  Mehr als 7 Zeichen\n4. Ein Sonderzeichen"));

		pw_Password.textProperty().addListener((observable, oldValue, newValue) ->
		{
			double safety = 0;

			if (!newValue.equals("")) // Sicherheitslevel = 0
			{

				if (newValue.length() > 7)
				{
					safety += addLevel;
					logger.info("L‰nge passt");
				}

				if (!newValue.equals(newValue.toLowerCase()))
				{
					safety += addLevel;
					logger.info("Groﬂ / Klein");
				}

				Pattern digit = Pattern.compile("[0-9]");
				Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
				// Pattern eight = Pattern.compile (".{8}");

				Matcher hasDigit = digit.matcher(newValue);
				Matcher hasSpecial = special.matcher(newValue);

				if (hasDigit.find())
				{
					safety += addLevel;
				}

				if (hasSpecial.find())
				{
					safety += addLevel;
				}

			}

			pb_PasswordSecure.setProgress(safety);
		});

	}

	public void showErrorMessage(String message)
	{
		logger.warn(message);
		lbl_ErrorMsg.setText(message);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						lbl_ErrorMsg.setText("");
					}
				});
			}
		}).start();
	}
}
