/*
 * Created on 04.01.2004
 * 
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003  Wolfgang Keller
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
public class GameTile {

	protected char[] directionParts;
	protected int vis;
	
	GameTile(char t_north, char t_east, char t_south, char t_west, int visible)
	{
		this.vis=visible;
		directionParts=new char[4];
		
		directionParts[0]=t_north;
		directionParts[1]=t_east;
		directionParts[2]=t_south;
		directionParts[3]=t_west;
	}
	
	
	
	public char[] getTiles()
	{
		return directionParts;
	}
	
	public int getVisible()
	{
		return vis;
	}
}