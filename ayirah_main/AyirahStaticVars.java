 /*
 * Created on 10.09.2003
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
		
	public static final int VISIBLE_KNOWN_NONE=0;
	public static final int VISIBLE_KNOWN_NORTH=1;
	public static final int VISIBLE_KNOWN_NORTH_EAST=3;
	public static final int VISIBLE_KNOWN_EAST=2;
	public static final int VISIBLE_KNOWN_SOUTH_EAST=6;
	public static final int VISIBLE_KNOWN_SOUTH=4;
	public static final int VISIBLE_KNOWN_SOUTH_WEST=12;
	public static final int VISIBLE_KNOWN_WEST=8;
	public static final int VISIBLE_KNOWN_NORTH_WEST=9;
	public static final int VISIBLE_KNOWN_ALL=15;
	
	public static final int[] dir_left_visible_modifier=
	{
		VISIBLE_KNOWN_NORTH_EAST, VISIBLE_KNOWN_SOUTH_EAST, VISIBLE_KNOWN_SOUTH_WEST,
		VISIBLE_KNOWN_NORTH_WEST
	};
	
	public static final int[] dir_right_visible_modifier=
	{
		VISIBLE_KNOWN_NORTH_WEST, VISIBLE_KNOWN_NORTH_EAST, VISIBLE_KNOWN_SOUTH_EAST,
		VISIBLE_KNOWN_SOUTH_WEST
	};
	
	public static final int[] invert_visible=
	{
		VISIBLE_KNOWN_ALL, VISIBLE_KNOWN_SOUTH_WEST, VISIBLE_KNOWN_NORTH_WEST,
		VISIBLE_KNOWN_NORTH_EAST, VISIBLE_KNOWN_SOUTH_EAST, VISIBLE_KNOWN_NONE
	};
	
	public static final int[] diagonal_view_visible=
	{
		AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST,
		AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST,
		AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST,
		AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST
	};
	
	/*
	 * Index 0: Tile '1' usw.
	 */
	public static char[][] diagonal_tiles_tile_parts=
	{
		{'#', '#', '.', '.'},
		{'.', '#', '#', '.'},
		{'.', '.', '#', '#'},
		{'#', '.', '.', '#'}
	};
	
	public static final String avtr_prefix="avatars/avtr2";
	public static final String tile_prefix="tiles/";
	
	public static final Color color_invisible=new Color(0,0,0,127);
	public static final Color color_unknown=new Color(0,0,0,255);
}