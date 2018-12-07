package de.krauss.gfx.init;

import de.krauss.car.CarList;
import de.krauss.gfx.ALLINONEFrameController;
import de.krauss.user.User;
import de.krauss.utils.OracleDataBase;

public class ManagerInitialize
{
	private InitializeReservieren initializeReservieren = new InitializeReservieren();
	private InitializeSearch initializeSearch = new InitializeSearch();
	private InitializeOverview initializeOverview = new InitializeOverview();
	private InitializeAutoAdden initializeAutoAdden = new InitializeAutoAdden();

	private User user;

	/**
	 * Startet die Initaliasierung von Liste,ComboBox, den Button reslöschen
	 */
	public void init(ALLINONEFrameController controller, OracleDataBase orcb, CarList carlist)
	{
		initializeReservieren.init(controller, orcb, carlist);
		initializeSearch.init(controller);
		initializeOverview.init(controller, this, carlist);
		initializeAutoAdden.init(controller, carlist);
	}

	/*
	 * 
	 * 
	 * SETTER UND GETTER
	 * 
	 * 
	 */

	public InitializeReservieren getInitializerReservieren()
	{
		return initializeReservieren;
	}

	public InitializeSearch getInitializerSearch()
	{
		return initializeSearch;
	}

	public InitializeOverview getInitializeOverview()
	{
		return initializeOverview;
	}

	public InitializeAutoAdden getInitializeAutoAdden()
	{
		return initializeAutoAdden;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public User getUser()
	{
		return user;
	}

}
