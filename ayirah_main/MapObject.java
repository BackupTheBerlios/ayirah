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
 * MapObject repräsentiert alle Objekte, die auf
 * der Karte auftreten (darunter auch Items, sie haben
 * die Eigenschaft takeable=true)
 */
public class MapObject {
	String type; // um was für einen Objekttyp handelt es sich
	String name; // lange Bezeichnung
	long weight; // Gewicht in Gramm
	boolean takeable; // kann der Character das Objekt aufnehmen (rein theoretisch!)
	
	MapObject(String type, String name, long weight, boolean takeable)
	{
		this.type=type;
		this.name=name;
		this.weight=Math.abs(weight);
		this.takeable=takeable;
	}
}
