/*
 * Created on 04.01.2004
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
 * @author Wolfgang
 */
public class GameTile {
	protected char[] directionParts;
	protected int visibility;
	protected int visible_other;
	protected int known;
	
	protected GameItem gi;
	
	GameTile(char t_north, char t_east, char t_south, char t_west, int visible, int known, GameItem item)
	{
		this.visibility=visible;
		this.visible_other=0;
		directionParts=new char[4];
		this.known=known;
		this.gi=item;
		
		directionParts[0]=t_north;
		directionParts[1]=t_east;
		directionParts[2]=t_south;
		directionParts[3]=t_west;
	}
	
	GameTile(char[] direction_parts, int vis, int known, String item)
	{
		this.visibility=vis;
		this.known=known;
		
		directionParts=new char[4];
		
		if (direction_parts.length>=4)
		{
			for (int i=0; i<4; i++)
				directionParts[i]=direction_parts[i];
		}
		else
		{
			int size=direction_parts.length;
		
			for (int i=0; i<size; i++)
				directionParts[i]=direction_parts[i];
		
			for (int i=size; i<4; i++)
			directionParts[i]=' ';
		}
	}
	
	public char[] getTiles()
	{
		return directionParts;
	}
	
	public void setTile(int index, char tile)
	{
		directionParts[index]=tile;
	}
	
	public int getVisible()
	{
		return visibility;
	}
	
	public void setVisible(int visible)
	{
		this.visibility=visible;
	}
	
	public int getVisibleOther()
	{
		return visible_other;
	}
	
	public void setVisibleOther(int vis)
	{
		this.visible_other=vis;
	}
	
	public int getKnown()
	{
		return known;
	}
	
	public void setKnown(int known)
	{
		this.known=known;
	}
	
	public GameItem getItem()
	{
		return gi;
	}
	
	public void setItem(GameItem item)
	{
		this.gi=item;
	}
}