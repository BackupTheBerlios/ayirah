 /*
 * Created on 10.09.2003
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
 * @author Wolfgang Keller
 */
import java.awt.*;

public class AyirahStaticVars {
	/** Die Richtungen: */
	public static final int DIRECTION_NONE=-1;
	public static final int DIRECTION_NORTH=0;
	public static final int DIRECTION_NORTH_EAST=1;
	public static final int DIRECTION_EAST=2;
	public static final int DIRECTION_SOUTH_EAST=3;
	public static final int DIRECTION_SOUTH=4;
	public static final int DIRECTION_SOUTH_WEST=5;
	public static final int DIRECTION_WEST=6;
	public static final int DIRECTION_NORTH_WEST=7;
	
	public static final int TURN_INVALID_TURN=-1;
	
	/**
	[n][0]:x
	[n][1]:y
	*/
	public static final int[][] direction_modifier=
	{{0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1},{-1,0}, {-1,-1}};
	
	
	
	public static final int VISIBLE_NONE=0;
	public static final int VISIBLE_NORTH_EAST=1;
	public static final int VISIBLE_SOUTH_EAST=2;
	public static final int VISIBLE_SOUTH_WEST=3;
	public static final int VISIBLE_NORTH_WEST=4;
	public static final int VISIBLE_ALL=5;
	
	public static final int KNOWN_NONE=0;
	public static final int KNOWN_NORTH_EAST=1;
	public static final int KNOWN_SOUTH_EAST=2;
	public static final int KNOWN_SOUTH_WEST=3;
	public static final int KNOWN_NORTH_WEST=4;
	public static final int KNOWN_ALL=5;
	
	public static final int[] dir_left_visible_modifier=
	{
		VISIBLE_NORTH_EAST, VISIBLE_SOUTH_EAST, VISIBLE_SOUTH_WEST,
		VISIBLE_NORTH_WEST
	};
	
	public static final int[] dir_right_visible_modifier=
	{
		VISIBLE_NORTH_WEST, VISIBLE_NORTH_EAST, VISIBLE_SOUTH_EAST,
		VISIBLE_SOUTH_WEST
	};
	
	public static final int[] invert_visible=
	{
		VISIBLE_ALL, VISIBLE_SOUTH_WEST, VISIBLE_NORTH_WEST,
		VISIBLE_NORTH_EAST, VISIBLE_SOUTH_EAST, VISIBLE_NONE
	};
	
	public static final int DIAGONAL_INVISIBLE=0;
	public static final int DIAGONAL_HALFVISIBLE=1;
	public static final int DIAGONAL_VISIBLE=2;
	
	
	/**
	[n][0]:x
	[n][1]:y
	*/
	public static final int[][] side_view_modifiers=
	{{1,0}, {1,1}, {0,1}, {1,-1}, {-1,0}, {-1,-1}, {0,-1}, {-1,1}};
	
	public static final int[] diagonal_view_visible=
	{
		AyirahStaticVars.VISIBLE_NORTH_EAST,
		AyirahStaticVars.VISIBLE_SOUTH_EAST,
		AyirahStaticVars.VISIBLE_SOUTH_WEST,
		AyirahStaticVars.VISIBLE_NORTH_WEST
	};
	
	public static final String avtr_prefix="avatars/avtr2";
	public static final String tile_prefix="tiles/";
	
	public static final Color color_invisible=new Color(0,0,0,127);
	public static final Color color_unknown=new Color(0,0,0,255);
}