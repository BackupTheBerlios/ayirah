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
public class GameItem implements Cloneable {
	private String type; // um was für einen Objekttyp handelt es sich
	private String sub_type; // Unter-Typ
	private String state; // Status des Objekts
	private String name; // lange Bezeichnung
	private int vis_type;
	private long weight; // Gewicht in Gramm
	private boolean walk_on_able;
	private boolean takeable; // kann der Character das Objekt aufnehmen (rein theoretisch!)
	
	GameItem(String type, String sub_type, String state, String name, 
	int vis_type, boolean takeable, boolean move_on_able, long weight)
	{
		setType(type);
		setSubType(sub_type);
		setState(state);
		setName(name);
		setVisibilityType(vis_type);
		setWeight(Math.abs(weight));
		setWalkOnAble(move_on_able);
		setTakeable(takeable);
	}
	
	protected void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	protected void setSubType(String sub_type) {
		this.sub_type = sub_type;
	}

	public String getSubType() {
		return sub_type;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setVisibilityType(int vis_type) {
		this.vis_type = vis_type;
	}

	public int getVisibilityType() {
		return vis_type;
	}

	protected void setWeight(long weight) {
		this.weight = weight;
	}

	public long getWeight() {
		return weight;
	}
	
	protected void setWalkOnAble(boolean move_on_able) {
		this.walk_on_able = move_on_able;
	}
	
	public boolean isWalkOnAble() {
		return walk_on_able;
	}

	protected void setTakeable(boolean takeable) {
		this.takeable = takeable;
	}

	public boolean isTakeable() {
		return takeable;
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof GameItem)
		{
			GameItem gi=(GameItem) arg0;
			return (this.getType()==gi.getType() && 
			this.getSubType()==gi.getSubType() &&
			this.getState()==gi.getState() &&
			this.getName()==gi.getName() &&
			this.getVisibilityType()==gi.getVisibilityType() &&
			this.getWeight()==gi.getWeight() &&
			this.isTakeable()==gi.isTakeable());
		}
		else
			return false;
	}
	
	public int hashCode() {
		return
		((getType().hashCode() & ((1<<6)-1))<<26) |
		((getSubType().hashCode() & ((1<<6)-1))<<20 |
		((getState().hashCode() & ((1<<6)-1))<<14) |
		((getName().hashCode() & ((1<<6)-1))<<8) |
		((getVisibilityType() & ((1<<3)-1))<<5) |
		(((int) getWeight() & ((1<<3)-1))<<1) |
		(isTakeable() ? 1 : 0)
		);
	}
	
	public Object clone()
	{
		return new GameItem(getType(), getSubType(), getState(), getName(), getVisibilityType(), isTakeable(), false, getWeight());
	}
}
