/*
 * Created on 06.03.2004
 * Last modified on 14.07.2004
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
 * GameItem repr�sentiert alle Objekte, die auf
 * der Karte auftreten (Items haben
 * die Eigenschaft takeable=true)
 */
public class GameItem implements Cloneable {
	private String type; // um was f�r einen Objekttyp handelt es sich
	private String sub_type; // Unter-Typ
	private String state; // Status des Objekts
	private String description; // lange Bezeichnung
	private int vis_type;
	private int using_directions;
	private long weight; // Gewicht in Gramm
	private boolean walk_on_able;
	private boolean takeable; // kann der Character das Objekt aufnehmen (rein theoretisch!)
	
	GameItem(String type, String sub_type, String state, String description, 
	int vis_type, int using_directions,
	boolean takeable, boolean walk_on_able, long weight)
	{
		setType(type);
		setSubType(sub_type);
		setState(state);
		setDescription(description);
		setVisibilityType(vis_type);
		setUsingDirections(using_directions);
		setWeight(Math.abs(weight));
		setWalkOnAble(walk_on_able);
		setTakeable(takeable);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setSubType(String sub_type) {
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setVisibilityType(int vis_type) {
		this.vis_type = vis_type;
	}

	public int getVisibilityType() {
		return vis_type;
	}
	
	public void setUsingDirections(int using_directions) {
		this.using_directions = using_directions;
	}
	
	public int getUsingDirections() {
		return using_directions;
	}
	
	public void setWeight(long weight) {
		this.weight = weight;
	}

	public long getWeight() {
		return weight;
	}
	
	public void setWalkOnAble(boolean move_on_able) {
		this.walk_on_able = move_on_able;
	}
	
	public boolean isWalkOnAble() {
		return walk_on_able;
	}

	public void setTakeable(boolean takeable) {
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
			this.getDescription()==gi.getDescription() &&
			this.getVisibilityType()==gi.getVisibilityType() &&
			this.getUsingDirections()==gi.getUsingDirections() &&
			this.getWeight()==gi.getWeight() &&
			this.isWalkOnAble()==gi.isWalkOnAble() &&
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
		((getDescription().hashCode() & ((1<<6)-1))<<8) |
		((getVisibilityType() & ((1<<3)-1))<<5) |
		(((int) getWeight() & ((1<<3)-1))<<1) |
		(isTakeable() ? 1 : 0)
		);
	}
	
	public Object clone()
	{
		return new GameItem(getType(), getSubType(), getState(), getDescription(), 
		getVisibilityType(), getUsingDirections(), isTakeable(), false, getWeight());
	}
}
