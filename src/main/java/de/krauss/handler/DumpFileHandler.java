package de.krauss.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public class DumpFileHandler implements FileHandler
{
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Car> load(File dumpfile)
	{
		System.out.println("HALLO");
		try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(dumpfile)))
		{
			Object ob = o.readObject();

			if (ob instanceof java.util.ArrayList)
			{
				return (ArrayList<Car>) ob;
			}
			logger.fatal("Objekt ist keine Arraylist!");
			return null;
		} catch (FileNotFoundException fnf)
		{
			logger.fatal("Datei (" + dumpfile + ") nicht gefunden!");
		} catch (IOException e)
		{
			logger.fatal("Probleme mit dem Einlesen der Datei");
		} catch (ClassNotFoundException e)
		{
			logger.fatal("Das einzulesende Objekt ist fehlerhaft!");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void safe(CarList cars, File f)
	{
		OutputStream fos = null;
		try
		{
			File chooFile = f;

			if (chooFile == null)
				chooFile = getDefaultFile();

			fos = new FileOutputStream(chooFile);

			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(cars.getList());
			o.flush();
			o.close();
			fos.close();
		} catch (FileNotFoundException e)
		{
			logger.fatal("Die gesuchte Datei wurde nicht gefunden");
		} catch (IOException e)
		{
			logger.fatal("Fehler beim Schreiben des Objektes");
			e.printStackTrace();
		}
		try
		{
			if (fos != null)
				fos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{
		return new File(Launcher.HOME_DIR + ".dump");
	}

}
