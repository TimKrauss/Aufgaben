package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.FileManager;
import de.krauss.Launcher;
import de.krauss.Reservierung;

public class XStreamFileHandler extends FileHandler
{
	private XStream xs;

	public XStreamFileHandler()
	{
		xs = new XStream(new DomDriver());

		xs.alias("Car", Car.class);
		xs.alias("Reservierung", Reservierung.class);
		xs.alias("Launcher", Launcher.class);
		xs.alias("FileManager", getClass());

		Class<?>[] classes = new Class[] { Reservierung.class, Car.class, FileManager.class, Launcher.class };
		XStream.setupDefaultSecurity(xs);
		xs.allowTypes(classes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Car> load(File f)
	{
		try
		{
			@SuppressWarnings("unchecked")
			ArrayList<Car> c = (ArrayList<Car>) xs.fromXML(f);
			return c;
		} catch (StreamException e)
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
		String xml = xs.toXML(cars.getList());
		File chooFile = f;
		if (f == null)
		{
			chooFile = getDefaultFile();
		}

		try
		{
			PrintWriter wr = new PrintWriter(new FileWriter(chooFile));
			BufferedReader reader = new BufferedReader(new StringReader(xml));

			String line = "";

			while ((line = reader.readLine()) != null)
			{
				wr.println(line);
			}

			wr.flush();
			wr.close();

		} catch (IOException e)
		{
			logger.fatal("Fehler beim Schreiben der XML-Datei");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{
		return new File(Launcher.HOME_DIR + "Cars.xml");
	}

}
