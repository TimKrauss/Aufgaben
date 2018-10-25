package de.krauss.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(dumpfile)))
		{
			Object ob = o.readObject();

			if (ob instanceof java.util.ArrayList)
			{
				ArrayList<Car> cars = (ArrayList<Car>) ob;

				return cars;
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
		if (f == null)
		{
			f = getDefaultFile();
		}
		try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(f)))
		{
			o.writeObject(cars.getList());
			o.flush();
		} catch (FileNotFoundException e)
		{
			logger.fatal("Die gesuchte Datei wurde nicht gefunden");
		} catch (IOException e)
		{
			logger.fatal("Fehler beim Schreiben des Objektes");
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{
		return new File(Launcher.HOME_DIR + "Dump.dump");
	}

}
