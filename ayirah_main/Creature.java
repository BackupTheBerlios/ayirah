 /*
 * Created on 11.10.2003
 * 
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003  Wolfgang Keller
 * Contact: http://ayirah.berlios.de | mail.wolfgang.keller@web.de * 
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
 */
public abstract class Creature {
	protected GameMap map;
	protected int pos_x, pos_y, layer, view_direction;	
	public Creature(GameMap map, int l, int x, int y, int direction) {
		super();
		this.map=map;
		this.layer=l;
		this.pos_x=x;
		this.pos_y=y;
		this.view_direction=direction;
	}
	
	public int getPosX()
	{
		return pos_x;
	}
	
	public void setPosX(int x)
	{
		this.pos_x=x;
	}
	
	public int getPosY()
	{
		return pos_y;
	}
	
	public void setPosY(int y)
	{
		this.pos_y=y;
	}
	
	public int getLayer()
	{
		return layer;
	}
	
	public void setLayer(int layer)
	{
		this.layer=layer;
	}
	
	public void rotateLeft()
	{
		this.view_direction=(this.view_direction+7)%8;
		calculateVisible();
	}
	
	public void rotateRight()
	{
		this.view_direction=(this.view_direction+1)%8;
		calculateVisible();
	}
	
	public int getViewDirection()
	{
		return view_direction;
	}
	
	public void setViewDirection(int direction)
	{
		this.view_direction=direction;
	}
	
	/**
	 * Die Methode calculateVisible() muss aus technischen 
	 * Gründen nach der Konstruktion des Objektes manuell 
	 * aufgerufen werden
	 */
	public void calculateVisible()
	{
		
	}
		public int getTurn() {		return AyirahStaticVars.TURN_INVALID_TURN;	}	public GameMap getMap() {		return this.map;	}
}
