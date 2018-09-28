package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;

public class JAXBFileHandler extends FileHandler
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Car> load(File f)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(CarList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			CarList list = (CarList) unmarshaller.unmarshal(f);
			return list.getList();

		} catch (JAXBException e)
		{
			logger.fatal("Fehler beim Einlesen der JAXB-XML-Datei.");
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void safe(CarList cars, File f)
	{

		try
		{
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(CarList.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(cars, writer);
			File chooFile = f;

			if (chooFile == null)
				chooFile = getDefaultFile();

			PrintWriter wr = new PrintWriter(new FileWriter(f));
			BufferedReader r = new BufferedReader(new StringReader(writer.toString()));

			String line = "";

			while ((line = r.readLine()) != null)
			{
				wr.println(line);
			}
			wr.flush();
			wr.close();

		} catch (JAXBException e)
		{
			logger.fatal("Fehler beim Erstellen des JAXB-Contexts");
		} catch (IOException e)
		{
			logger.fatal("Fehler beim Speichern in der Datei schreiben");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{

		return new File(Launcher.HOME_DIR + "JAXB_Cars.xml");
	}

}
