package de.krauss.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public class DumpFileHandler extends FileHandler
{
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Car> load(File dumpfile)
	{
		InputStream fis = null;

		try
		{
			fis = new FileInputStream(dumpfile);
			ObjectInputStream o = new ObjectInputStream(fis);
			Object ob = o.readObject();
			o.close();

			if (ob instanceof java.util.ArrayList)
			{
				fis.close();
				return (ArrayList<Car>) ob;
			}
			fis.close();
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
		try
		{
			if (fis != null)
				fis.close();
		} catch (IOException e)
		{
			e.printStackTrace();
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
		return new File(Launcher.HOME_DIR + "dumpfile");
	}

}
