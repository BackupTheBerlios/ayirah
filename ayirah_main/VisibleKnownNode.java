/*
 * Created on 08.12.2003
 *  * Ayirah - a Java (tm)-based Roleplaying Game  * Copyright (C) 2003  Wolfgang Keller * Contact: http://ayirah.berlios.de | mail.wolfgang.keller@web.de
 *  * This program is free software; you can  * redistribute it and/or modify it under  * the terms of the GNU General Public License  * as published by the Free Software Foundation;  * either version 2 of the License, or  * (at your option) any later version.  *  * This program is distributed in the hope  * that it will be useful, but WITHOUT ANY WARRANTY;  * without even the implied warranty of  * MERCHANTABILITY or FITNESS FOR A PARTICULAR  * PURPOSE. See the GNU General Public License  * for more details.  * You should have received a copy of the  * GNU General Public License along with  * this program; if not, write to the  * Free Software Foundation, Inc.,  * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.  */

/** * @author Wolfgang */
public class VisibleKnownNode implements Cloneable {
	protected int main_type;
	
	VisibleKnownNode()
	{
		
	}
	
	VisibleKnownNode(int main_type)
	{
		this();
		setType(main_type);
	}
	
	public int getType()
	{
		return main_type;
	}
	
	protected boolean isValidVisibleType(int main_type)
	{
		return (main_type>=0 && main_type<=15);
	}
	
	public void setType(int main_type)
	{
		if (isValidVisibleType(main_type))
		{
			this.main_type=main_type;
		}
	}
	
	public void addType(int main_type)
	{
		if (isValidVisibleType(main_type))
		{
			this.main_type=this.main_type | main_type;
		}
	}
		public void removeType(int main_type)	{		if (isValidVisibleType(main_type))			this.main_type=this.main_type & (AyirahStaticVars.VISIBLE_KNOWN_ALL-main_type);
		else
			System.out.println(main_type+" is not a valid VisibilityType");
	}
}
