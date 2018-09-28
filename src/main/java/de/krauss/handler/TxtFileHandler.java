package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.Reservierung;

public class TxtFileHandler extends FileHandler
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Car> load(File f)
	{
		try
		{
			ArrayList<Car> cars = new ArrayList<>();

			if (f == null)
				return null;

			BufferedReader reader = new BufferedReader(new FileReader(f));

			String st = "";
			Car newCar = new Car();
			while ((st = reader.readLine()) != null)
			{

				if (st.contains("Name:"))
				{
					newCar.setF_Name(st.replace("Name:", ""));
				} else if (st.contains("Marke:"))
				{
					newCar.setF_Marke(st.replace("Marke:", ""));
				} else if (st.contains("Kilometer:"))
				{
					String g = st.replace("Kilometer:", "").replaceAll(" +", "");
					newCar.setF_Tacho(Integer.parseInt(g));
				} else if (st.contains("ResStart:"))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy (HH:MM)");
					sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

					Date start = sdf.parse(st.replace("ResStart: ", ""));
					String dStop = reader.readLine();
					Date stop = sdf.parse(dStop.replace("ResStop: ", ""));

					newCar.addResv(new Reservierung(start, stop));
				} else if (st.contains("---"))
				{
					cars.add(newCar);
					newCar = new Car();
				}

			}
			logger.info("");
			logger.info("");
			reader.close();
			return cars;
		} catch (FileNotFoundException e)
		{
			logger.fatal("Das Textfile wurde nicht gefunden!");
		} catch (NumberFormatException e)
		{
			logger.fatal("Fehler beim NumberFormat");
		} catch (IOException e)
		{
			logger.fatal("Fehler beim Lesen des Files");
		} catch (ParseException e)
		{
			logger.fatal("Fehler beim Datumformat!");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void safe(CarList cars, File f)
	{
//		JFileChooser fc = new JFileChooser();
//		
//		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		
//		int option = fc.showDialog(null, "Wähle diesen Ordner");
//	
//		if (option == JFileChooser.APPROVE_OPTION)
//			
//		File f = fc.getSelectedFile();

		File chooFile = f;
		if (f == null)
		{
			chooFile = getDefaultFile();
		}
		try
		{
			PrintWriter wr = new PrintWriter(chooFile);

			for (Car c : cars.getList())
			{
				wr.println("Name: " + c.getF_Name());
				wr.println("Marke: " + c.getF_Marke());
				wr.println("Kilometer: " + c.getF_Tacho());
				wr.flush();

				// Reservierungen

				if (c.getReservs().size() != 0)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy (HH:MM)");

					for (Reservierung r : c.getReservs())
					{
						wr.println("ResStart: " + sdf.format(r.getResStart()));
						wr.println("ResStop: " + sdf.format(r.getResStop()));
					}
				}

				wr.println("---");
				wr.flush();
			}

			wr.close();
		} catch (Exception e)
		{
			logger.warn("File not Found");
		}

		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDefaultFile()
	{
		return new File(Launcher.HOME_DIR + "Cars.txt");
	}

}
