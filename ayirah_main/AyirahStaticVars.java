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
	
	public static final int VISIBLE_LEFT=4;
	public static final int VISIBLE_CENTER=2;
	public static final int VISIBLE_RIGHT=1;
	
	public static final int FRONT_FULL_VISIBLE=0;
	public static final int FRONT_TILE_INVISIBLE=1;
	public static final int FRONT_TILE_MOST_INVISIBLE=2;
	public static final int FRONT_TILE_MOST_VISIBLE=3;
	public static final int FRONT_TILE_VISIBLE=4;
	
	public static final int SIDE_FULL_VISIBLE=0;
	public static final int SIDE_TILE_INVISIBLE=1;
	public static final int SIDE_TILE_HALFVISIBLE=2;
	public static final int SIDE_TILE_VISIBLE=3;
	
	public static char[][] non_block_tiles_tile_parts=
	{
		{'#', '#', '.', '.'},
		{'.', '#', '#', '.'},
		{'.', '.', '#', '#'},
		{'#', '.', '.', '#'},
		{'#', '.', '#', '.'},
		{'.', '#', '.', '#'}
	};
	
	public static final String avtr_prefix="avatars/avtr";
	public static final String tile_prefix="tiles/";
	public static final String item_prefix="items/";
	public static final String cursor_name="cursor/hand_sm.gif";
	
	public static final String[] tile_names={"boden1", "wand1", "lava", 
		"tuer_waagrecht_offen", "tuer_waagrecht_geschlossen", 
		"tuer_senkrecht_offen", "tuer_senkrecht_geschlossen", 
		"treppe_hoch", "treppe_hoch_runter", "treppe_runter"};
	public static final String[] item_names={"box_open", "box_closed"};
	public static final String[] map_object_states={"_invisible", "_visible", "_other"};
	public static final String map_object_ending=".gif";

	public static final char[] sprite_name={'a', 'b', 'c', 'd'};
}