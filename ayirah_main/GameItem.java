/*
 * Created on 06.03.2004
 * 
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003  Wolfgang Keller
 * Contact: http://ayirah.berlios.de | mail.wolfgang.keller@web.de
 * 
 * This program is free software; you can 
 * redistribute it and/or modify it under 
 * the terms of the GNU General Public License 
 * as published by the Free Software Foundation; 
 * either version 2 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License 
 * for more details. 
 * You should have received a copy of the 
 * GNU General Public License along with 
 * this program; if not, write to the 
 * Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA. 
 */

/**
 * @author Wolfgang Keller
 * 
 * GameItem repräsentiert alle Objekte, die auf
 * der Karte auftreten (Items haben
 * die Eigenschaft takeable=true)
 */
public class GameItem {
	protected String type; // um was für einen Objekttyp handelt es sich
	protected String sub_type; // Unter-Typ
	protected String state; // Status des Objekts
	protected String name; // lange Bezeichnung
	protected int visibility_type;
	protected long weight; // Gewicht in Gramm
	protected boolean takeable; // kann der Character das Objekt aufnehmen (rein theoretisch!)
	
	GameItem(String type, String sub_type, String state, String name, 
	int vis_type, long weight, boolean takeable)
	{
		this.type=type;
		this.sub_type=sub_type;
		this.state=state;
		this.name=name;
		this.visibility_type=vis_type;
		this.weight=Math.abs(weight);
		this.takeable=takeable;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public String getSubType()
	{
		return this.sub_type;
	}
	
	public String getState()
	{
		return this.state;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getVisibilityType()
	{
		return this.visibility_type;
	}
	
	public long getWeight()
	{
		return this.weight;
	}
	
	public boolean isTakeAble()
	{
		return takeable;
	}
}
