package de.krauss.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.krauss.Car;
import de.krauss.CarList;
import de.krauss.Launcher;
import de.krauss.Reservierung;

public class TxtFileHandler implements FileHandler
{

	private String NAME_PATTERN = "Name:", MARKE_PATTERN = "Marke:", TACHO_PATTERN = "Kilometer:",
			RES_START_PATTERN = "ResStart:", RES_STOP_PATTERN = "ResStop:", CAR_FINISHED_PATTERN = "---",
			OWNER_PATTERN = "owner:";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Car> load(File f)
	{
		if (f.getName().startsWith("ALEC"))
		{
			NAME_PATTERN = "name:";
			MARKE_PATTERN = "mark:";
			TACHO_PATTERN = "milage:";
			OWNER_PATTERN = "user:";
			RES_START_PATTERN = "beginn:";
			RES_STOP_PATTERN = "end:";
			CAR_FINISHED_PATTERN = "";
		} else
		{
			NAME_PATTERN = "Name:";
			MARKE_PATTERN = "Marke:";
			TACHO_PATTERN = "Kilometer:";
			RES_START_PATTERN = "ResStart:";
			RES_STOP_PATTERN = "ResStop:";
			CAR_FINISHED_PATTERN = "---";
			OWNER_PATTERN = "owner:";
		}
		return loadData(f);
	}

	private ArrayList<Car> loadData(File f)
	{
		try
		{
			ArrayList<Car> cars = new ArrayList<>();

			if (f == null)
				return null;

			BufferedReader reader = new BufferedReader(new FileReader(f));

			String st = "";
			Car newCar = new Car();
			Reservierung res = new Reservierung();
			while ((st = reader.readLine()) != null)
			{

				if (st.contains(NAME_PATTERN))
				{
					newCar.setCarName(st.replace(NAME_PATTERN, ""));
				} else if (st.contains(MARKE_PATTERN))
				{
					newCar.setCarMarke(st.replace(MARKE_PATTERN, ""));
				} else if (st.contains(TACHO_PATTERN))
				{
					String g = st.replace(TACHO_PATTERN, "").replaceAll(" +", "");
					newCar.setCarTacho(Integer.parseInt(g));
				} else if (st.contains(OWNER_PATTERN))
				{
					res.setOwner(st.replace(OWNER_PATTERN, ""));
				}

				else if (st.contains(RES_START_PATTERN))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy (HH:MM)");
					sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

					Date start = null;
					Date stop = null;

					String resLine1 = st.replace(RES_START_PATTERN, "");
					String resLine2 = reader.readLine().replace(RES_STOP_PATTERN, "");

					try
					{
						start = sdf.parse(resLine1);
						stop = sdf.parse(resLine2);

					} catch (java.text.ParseException e)
					{
						// TRY LOAD AS LONG
						resLine1 = resLine1.replaceAll(" +", "");
						resLine2 = resLine2.replaceAll(" +", "");
						logger.info("Versuche Laden als TimeStamp-Long");
						start = new Date(Long.parseLong(resLine1));
						stop = new Date(Long.parseLong(resLine2));
					}
					res.setResStart(start);
					res.setResStop(stop);
					newCar.addResv(res);
					res = new Reservierung();

				} else if (st.contains(CAR_FINISHED_PATTERN))
				{
					if (newCar.getCarName() != null)
					{
						cars.add(newCar);
						newCar = new Car();
					}
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
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void safe(CarList cars, File f)
	{
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
				wr.println("Name: " + c.getCarName());
				wr.println("Marke: " + c.getCarMarke());
				wr.println("Kilometer: " + c.getCarTacho());
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
