/*
 * Created on 20.06.2004
 *
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003 Wolfgang Keller
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
 * @author Wolfgang
 */
public class CoordVector {
	private int pos_l;
	private int pos_x;
	private int pos_y;
	
	CoordVector(int l, int x, int y)
	{
		this.pos_l=l;
		this.pos_x=x;
		this.pos_y=y;
	}
	
	public void setLayer(int layer) {
		this.pos_l = layer;
	}

	public int getLayer() {
		return pos_l;
	}
	
	public void setPosX(int pos_x) {
		this.pos_x = pos_x;
	}

	public int getPosX() {
		return pos_x;
	}

	public void setPosY(int pos_y) {
		this.pos_y = pos_y;
	}

	public int getPosY() {
		return pos_y;
	}
	
	/**
	 * Bitaufteilung:
	 * Bit 24-31: Bit 0-7 von pos_l
	 * Bit 12-23: Bit 0-12 von pos_x
	 * Bit  0-11: Bit 0-12 von pos_y
	 */
	public int hashCode() {
		return (((getLayer()&((1<<8)-1))<<24) | 
		((getPosX()&((1<<12)-1))<<12) | 
		(getPosY()&((1<<12)-1)));
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof CoordVector)
		{
			CoordVector cv=(CoordVector) arg0;
			return (this.getLayer()==cv.getLayer() && 
			this.getPosX()==cv.getPosX() &&
			this.getPosY()==cv.getPosY());
		}
		else
			return false;
	}

}
