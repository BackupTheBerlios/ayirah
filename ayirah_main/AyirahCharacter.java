 /*
 * Created on 11.10.2003
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
 */
public class AyirahCharacter extends HumanLikeCreature {
	protected int scroll_top_x, scroll_top_y;
	
	public AyirahCharacter(GameMap map, int layer, int x, int y, int direction,
	int scroll_top_x, int scroll_top_y) {
		super(map, layer, x, y, direction);
		
		this.scroll_top_x=scroll_top_x;
		this.scroll_top_y=scroll_top_y;
	}
	
	public void setScrollCoords(int stx, int sty)
	{
		this.scroll_top_x=stx;
		this.scroll_top_y=sty;
	}
	
	public void setScrollTopX(int stx)
	{
		this.scroll_top_x=stx;
	}
	
	public void setScrollTopY(int sty)
	{
		this.scroll_top_y=sty;
	}
	
	public int getScrollTopX()
	{
		return scroll_top_x;
	}
	
	public int getScrollTopY()
	{
		return scroll_top_y;
	}
}
