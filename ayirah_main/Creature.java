 /*
 * Created on 11.10.2003
 * Last modified on 13.06.2004
 * 
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003-2004 Wolfgang Keller
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
	protected int view_direction;	protected CoordVector3 coords;
	
	public Creature(GameMap map, int l, int x, int y, int direction) {
		super();
		this.map=map;
		this.coords=new CoordVector3(l, x, y);
		this.view_direction=direction;
	}
	
	public int getPosX()
	{
		return coords.getPosX();
	}
	
	public void setPosX(int x)
	{
		coords.setPosX(x);
	}
	
	public int getPosY()
	{
		return coords.getPosY();
	}
	
	public void setPosY(int y)
	{
		coords.setPosY(y);
	}
	
	public int getLayer()
	{
		return coords.getLayer();
	}
	
	public void setLayer(int l)
	{
		coords.setLayer(l);
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
	public abstract void calculateVisible();
		public int getTurn() {		return AyirahStaticVars.TURN_INVALID_TURN;	}	public GameMap getMap() {		return this.map;	}
}
