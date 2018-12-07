package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import de.krauss.Launcher;
import de.krauss.car.Car;
import de.krauss.car.CarList;

public class JSonFileHandler implements FileHandler
{

	private Gson gson;
	private Type fooType = new TypeToken<ArrayList<Car>>()
	{
		//
	}.getType();

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
			return (ArrayList<Car>) gson.fromJson(builder.toString(), fooType);
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

			BufferedReader reader = new BufferedReader(new StringReader(gson.toJson(cars.getList(), fooType)));

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
			logger.warn(e.getMessage());
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
