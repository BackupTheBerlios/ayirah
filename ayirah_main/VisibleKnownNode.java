/*
 * Created on 08.12.2003
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
 *
 */
public class VisibleKnownNode {
	protected int main_type;
	protected int diagonal_visible;
	
	VisibleKnownNode()
	{
		
	}
	
	VisibleKnownNode(int main_type, int diagonal_visible)
	{
		this();
		setType(main_type, diagonal_visible);
	}
	
	public int getMainType()
	{
		return main_type;
	}
	
	public int getDiagonalType()
	{
		return diagonal_visible;
	}
	
	protected boolean isValidVisibleType(int main_type, int diagonal_visible)
	{
		return (main_type>=0 && main_type<6 && diagonal_visible>=0 && diagonal_visible<3);
	}
	
	public void setType(int main_type, int diagonal_visible)
	{
		if (isValidVisibleType(main_type, diagonal_visible))
		{
			this.main_type=main_type;
			this.diagonal_visible=diagonal_visible;
		}
	}
	
	public void addType(int main_type, int diagonal_visible)
	{
		if (isValidVisibleType(main_type, diagonal_visible))
		{
			if (this.main_type==AyirahStaticVars.VISIBLE_ALL || 
			main_type==AyirahStaticVars.VISIBLE_NONE)
				;
			else if (this.main_type==AyirahStaticVars.VISIBLE_NONE)
				setType(main_type, diagonal_visible);
			else
			{
				if (this.main_type==main_type)
					this.diagonal_visible=Math.max(diagonal_visible, this.diagonal_visible);
				else
					this.main_type=AyirahStaticVars.VISIBLE_ALL;
			}
		}
	}
	
	public void removeType(int main_type, int diagonal_visible)
	{
		if (isValidVisibleType(main_type, diagonal_visible))
		{
			if (this.main_type==AyirahStaticVars.VISIBLE_NONE || 
			main_type==AyirahStaticVars.VISIBLE_NONE)
				;
			else if (this.main_type==AyirahStaticVars.VISIBLE_ALL)
			{
				setType(AyirahStaticVars.invert_visible[main_type],2-diagonal_visible);
			}
			else
			{
				if (this.main_type==AyirahStaticVars.invert_visible[main_type])
					this.diagonal_visible=Math.min(this.main_type, 2-main_type);
				else
					this.main_type=AyirahStaticVars.VISIBLE_NONE;
			}
		}
	}
}
