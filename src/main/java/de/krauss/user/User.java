package de.krauss.user;

public class User
{
	private int RechteLvl;
	private String BenutzerName;
	private String PasswordHash;
	private int BenutzerID;

	public int getRechteLvl()
	{
		return RechteLvl;
	}

	public void setRechteLvl(int rechteLvl)
	{
		RechteLvl = rechteLvl;
	}

	public String getBenutzerName()
	{
		return BenutzerName;
	}

	public void setBenutzerName(String benutzerName)
	{
		BenutzerName = benutzerName;
	}

	public String getPasswordHash()
	{
		return PasswordHash;
	}

	public void setPasswordHash(String passwordHash)
	{
		PasswordHash = passwordHash;
	}

	public void setBenutzerID(int id)
	{
		BenutzerID = id;
	}

	public int getBenutzerID()
	{
		return BenutzerID;
	}
}
