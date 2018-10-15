package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public class JSonFileHandler implements FileHandler
{

	private Gson gson;

	public JSonFileHandler()
	{
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Car> load(File f)
	{
		StringBuilder builder = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));

			String line = "";

			while ((line = reader.readLine()) != null)
			{
				builder.append(line);
			}
			reader.close();
			return (ArrayList<Car>) gson.fromJson(builder.toString(), ArrayList.class);

		} catch (FileNotFoundException e)
		{
			logger.fatal(e.getMessage());
		} catch (IOException e)
		{
			logger.fatal(e.getMessage());
		} catch (JsonSyntaxException e)
		{
			logger.fatal(e.getMessage());
		}
		return null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void safe(CarList cars, File f)
	{
		File safeFile = f;

		if (safeFile == null)
		{
			safeFile = getDefaultFile();
		}

		try
		{
			PrintWriter wr = new PrintWriter(safeFile);

			String line = "";

			BufferedReader reader = new BufferedReader(new StringReader(gson.toJson(cars)));

			while ((line = reader.readLine()) != null)
			{
				wr.println(line);
			}

			wr.flush();
			wr.close();

		} catch (FileNotFoundException e)
		{
			logger.fatal(e.getMessage());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{
		return new File(Launcher.HOME_DIR + "Cars.json");
	}

}
