/*
 * Created on 10.09.2003
 * Last modified on 13.06.2004
 * 
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003-2004 Wolfgang Keller
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

import java.util.*;

public class GameMap {
	protected String[][] map= // Format: [zeile][spalte]
	{
	   {"............#............",
		".........................",
		"...................I.....",
		"............1............",
		"...w....s.........-.....#",
		".........................",
		"................3........",
		".......4.................",
		"...#...............>.....",
		"..........x..............",
		".........................",
		".........X...............",
		".........................",
		".........................",
		"#.......2................",
		".........................",
		".........................",
		"....................>....",
		".........................",
		".........................",
		".........................",
		".........................",
		".........................",
		".........................",
		"...#....................."},
		
	   {"########        #########",
		"#......#        #.......#",
		"#......#        #...1...#",
		"#...............I..3....#",
		"#..4...#    .   #.......#",
		"#...2..#    .   ###-#####",
		"#......#    .   #.......#",
		"#......#    .   #..##...#",
		"###.#### ###.####..<#...#",
		"  #.#    #4...1#...##...#",
		"  #.#    #.....I........#",
		"  #.#    #.....#........#",
		"  #.#    #3...2#...###-##",
		"  #.13   ###.####-##....#",
		"  13.13    #.#  #.......#",
		"   13.13   #.# ######_###",
		" ...#...13 #.###........#",
		" ...13...13#...I....|...#",
		" ....13...1#.  #........#",
		" .....13...1.  ####.#####",
		" .......3...........     ",
		"#.#######....#.### .#####",
		"#.......#........# .....#",
		"#.......i....#..........#",
		"#########################"},
		
	   {"########        #########",
		"#......#        #.......#",
		"#......#        #...1...#",
		"#...............I..3....#",
		"#..4...#    .   #.......#",
		"#...2..#    .   ###-#####",
		"#......#    .   #.......#",
		"#......#    .   #.......#",
		"###.#### ###.####.......#",
		"  #.#    #4...1#........#",
		"  #.#    #.....I........#",
		"  #.#    #.....#........#",
		"  #.#    #3...2#...###-##",
		"  #.13   ###.####-##....#",
		"  13.13    #.#  #.......#",
		"   13.13   #.# ######_###",
		" ...#...13 #.###........#",
		" ...13...13#...I....<...#",
		" ....13...1#.  #........#",
		" .....13...1.  ####.#####",
		" .......3...........     ",
		"#.#######....#.### .#####",
		"#.......#........# .....#",
		"#.......i....#..........#",
		"#########################"}
	};
	
	protected AyirahCharacter[] ayirah_char;
	protected int mapsize_x=25, mapsize_y=25, layers_count=3;
	protected int characters_count;
	protected int actual_character_index;
	
	public HashMap object_pos;
	
	public GameMap(int characters_count)
	{
		object_pos=new HashMap();
		
		this.characters_count=Math.max(1, Math.abs(characters_count));
		
		ayirah_char=new AyirahCharacter[this.characters_count];
		
		for (int i=0; i<this.characters_count; i++)
		{
			ayirah_char[i]=new AyirahCharacter(this, 0, 1, 1+i, 
			AyirahStaticVars.DIRECTION_SOUTH, 0, 0);
		}
		
		actual_character_index=0;
	}
	public char getTile(int l, int zeile, int spalte)
	{
		if (!(isValidCoordPair(l, spalte, zeile)))
			return ' ';
		else
			return map[l][zeile].charAt(spalte);
	}
	
	protected void setTile(int l, int zeile, int spalte, char tile)
	{
		if ((isValidCoordPair(l, spalte, zeile)))
		{
			if (spalte<(map[l][zeile].length()-1))
			{
				String s1=map[l][zeile].substring(0, spalte);
				String s2=map[l][zeile].substring(spalte+1, map[l][zeile].length());
				map[l][zeile]=new StringBuffer(map[l][zeile].substring(0, spalte)).
				append(tile).append(s2).toString();
			}
			else if (spalte==(map[l][zeile].length()-1))
			{
				map[l][zeile]=new StringBuffer(
				map[l][zeile].substring(0, spalte)).append(tile).toString();
			}
			
		}
	}
	
	public boolean isTileEmpty(int l, int zeile, int spalte)
	{
		if (this.isValidCoordPair(l, spalte, zeile))
		{
			for (int i=0; i<this.getCharactersCount(); i++)
			{
				AyirahCharacter actual_character=this.getCharacter(i);
				
				if ((actual_character.getLayer() == l) &&
				(actual_character.getPosX() == spalte) && 
				(actual_character.getPosY() == zeile))
				return false;
			}
		}
		else
			return false;
		
		return true;
	}
	
	/**
	 * Ganz einfache Methode zum Erzeugen von GameTiles. Nix für
	 * Spezialfälle
	 * 
	 * @param tile_type
	 * @param visible
	 * @param known
	 * @return
	 */
	public GameTile createGameTile(char tile_type, int visible, int known)
	{
		return new GameTile(
		((known & 1) != 0) ? tile_type : '?', 
		((known & 2) != 0) ? tile_type : '?', 
		((known & 4) != 0) ? tile_type : '?', 
		((known & 8) != 0) ? tile_type : '?', 
		visible, known, null);
	}
	
	public GameTile getCreatureKnownTile(HumanLikeCreature c, int l, int zeile, int spalte)
	{
		if ((zeile >= mapsize_y) || (zeile<0) || 
		(spalte>=mapsize_x) || (spalte<0))
			return createGameTile(' ', 0, 0);
		else if (c.getKnown(l, zeile, spalte)		==AyirahStaticVars.VISIBLE_KNOWN_NONE)
			return createGameTile('?', 0, 0);
		else
		{
			char r=map[l][zeile].charAt(spalte);
			
			if (r=='1' || r=='2' || r=='3' || r=='4' || r=='s' || r=='w')
			{
				int known=c.getKnown(l, zeile, spalte);
				boolean[] known_part=new boolean[4];
				
				known_part[0]=((known & 1) != 0);
				known_part[1]=((known & 2) != 0);
				known_part[2]=((known & 4) != 0);
				known_part[3]=((known & 8) != 0);
				
				int array_index;
				
				switch (r)
				{
					case '1':
						array_index=0;
						break;
					case '2':
						array_index=1;
						break;
					case '3':
						array_index=2;
						break;
					case '4':
						array_index=3;
						break;
					case 's':
						array_index=4;
						break;
					case 'w':
						array_index=5;
						break;
					default:
						// darf eigentlich nicht auftreten
						System.out.println("Fehler ungültiges Tile");
						array_index=0;
				}
				
				char[] tile_part=new char[4];
				for (int i=0; i<tile_part.length; i++)
				{
					tile_part[i]=known_part[i] ? 
					AyirahStaticVars.non_block_tiles_tile_parts[array_index][i] : '?';
				}
				
				return new GameTile(tile_part[0], tile_part[1], 
				tile_part[2], tile_part[3], c.getVisible(l, zeile, spalte), 
				c.getKnown(l, zeile, spalte), null);
			}
			
			else if (r=='X' || r=='x')
			{
				GameTile gt=createGameTile('.', c.getVisible(l, zeile, spalte), 
				c.getKnown(l, zeile, spalte));
				if (r=='x')
					gt.setItem(new GameItem("box", "wooden_box", "open", "an open box", 0, 50000l, false));
				else
					gt.setItem(new GameItem("box", "wooden_box", "closed", "a closed box", 0, 50000l, false));
				return gt;
			}
			
			else
			{
				return createGameTile(getTile(l, zeile, spalte), 
				c.getVisible(l, zeile, spalte), c.getKnown(l, zeile, spalte));
			}
		}
	}
	
	public GameTile getAllCharacterKnownTile(int l, int zeile, int spalte)
	{
		// erst mal eine Quick & Dirty-Implementierung
		boolean[] known=new boolean[4];
		boolean[] visible=new boolean[4];
		boolean[] known_neu=new boolean[4];
		boolean[] visible_neu=new boolean[4];
		
		GameTile[] chartiles=new GameTile[characters_count];
		
		for (int i=0; i<characters_count; i++)
		{
			chartiles[i]=getCreatureKnownTile(getCharacter(i), l, zeile, spalte);
		}
		
		GameTile basic=chartiles[getActualCharacterIndex()];
		
		for (int i=0; i<4; i++)
		{
			visible[i]=((basic.getVisible() & (1<<i)) != 0);
			known[i]=((basic.getKnown() & (1<<i)) != 0);
		}
		
		for (int i=0; i<characters_count; i++)
		{
			for (int j=0; j<4; j++)
			{
				visible_neu[j]=((chartiles[i].getVisible() & (1<<j)) != 0);
				known_neu[j]=((chartiles[i].getKnown() & (1<<j)) != 0);
				
				if (known_neu[j] && !((basic.getKnown() & (1<<j)) != 0))
				{
					basic.setKnown(basic.getKnown() | (1<<j));
					basic.setTile(j, chartiles[i].getTiles()[j]);
				}
				
			}
			
			if (getCharacter(i).getLayer() == getCharacter(
			getActualCharacterIndex()).getLayer())
			basic.setVisibleOther(basic.getVisibleOther() | chartiles[i].getVisible());
			
			if ((basic.getItem()==null) && 
			(chartiles[i].getItem()!=null))
			{
				basic.setItem(chartiles[i].getItem());
			}
		}
		
		return basic;
	}
	
	public GameItem getItem(CoordVector3 coords)
	{
		Object x=object_pos.get(coords);
		
		if (x != null)
			return ((GameItem) ((GameItem) x).clone());
		else
			return null;
	}
	
	public void addItem(CoordVector3 coords, GameItem gi)
	{
		if (coords!=null && gi!=null)
			object_pos.put(coords, gi);
	}
	
	public void removeItem(CoordVector3 coords)
	{
		if (coords!=null)
			object_pos.remove(coords);
	}
	
	public AyirahCharacter getCharacter(int i)
	{
		return ayirah_char[i];
	}
	
	public int getActualCharacterIndex()
	{
		return actual_character_index;
	}
	
	public void setActualCharacterIndex(int aci)
	{
		this.actual_character_index=aci;
	}
	
	public int getCharactersCount()
	{
		return characters_count;
	}
	
	public int getWidth()
	{
		return this.mapsize_x;
	}	
	public int getHeight()
	{
		return this.mapsize_y;
	}
	
	public int getLayersCount()
	{
		return this.layers_count;
	}
	public boolean isValidCoordPair(int l, int x, int y)
	{
		return (l>=0 && y>=0 && x>=0 && y<getHeight() && x<getWidth() && l<getLayersCount());
	}
	
	public int getVisibilityType(int l, int zeile, int spalte)
	{
		if (isValidCoordPair(l, spalte, zeile))
		{
			char t=getTile(l, zeile, spalte);
			
			int vis_type_tile;
			
			switch (t)
			{
				case '.':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case ' ':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case 'x':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case 'X':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case '<':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case '>':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case '|':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case '1':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NORTH_EAST;
					break;
				case '4':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NORTH_WEST;
					break;
				case '2':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_EAST_SOUTH;
					break;
				case '3':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_SOUTH_WEST;
					break;
				case 's':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NORTH_SOUTH;
					break;
				case 'w':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_EAST_WEST;
					break;
				case '#':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_ALL;
					break;
				case 'I':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NORTH_SOUTH;
					break;
				case 'i':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				case '-':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_EAST_WEST;
					break;
				case '_':
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
				default:
					vis_type_tile=AyirahStaticVars.VIS_TYPE_INVISIBLE_NONE;
					break;
			}
			
			GameItem gi=getItem(new CoordVector3(l, spalte, zeile));
			
			if (gi != null)
				return vis_type_tile | gi.getVisibilityType();
			else
				return vis_type_tile;
		}
		
		else
		{
			System.out.println("("+l+" "+spalte+" "+zeile+")");
			return 0;
		}
	}
	
	protected boolean isDoor(int layer, int zeile, int spalte)
	{
		char t=getTile(layer, zeile, spalte);
		return (t=='I' || t=='i' || t=='-' || t=='_');
	}
}
