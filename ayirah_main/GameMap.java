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
public class GameMap {
	protected String[][] map= // Format: [zeile][spalte]
	{
	   {"............x..#.........",
		".........................",
		"........I................",
		"............1............",
		"...w....s...............#",
		".........................",
		"................3........",
		".......4.................",
		"...................>.....",
		"..........x..............",
		".........................",
		".........X...............",
		".........................",
		".........................",
		"........2................",
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
	
	protected AyirahCharacter ayirah_char;
	protected int mapsize_x=25, mapsize_y=25, layers_count=3;
	
	public GameMap()
	{
		ayirah_char=new AyirahCharacter(this, 0, 1, 1, AyirahStaticVars.DIRECTION_SOUTH);
		ayirah_char.calculateVisible();
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
	
	/**
	 * Sollte es für alle Fälle, außer die diagonalen Wände tun
	 * @param tile_type
	 * @param visible
	 * @param known
	 * @return
	 */
	public GameTile createGameTile(char tile_type, int visible, int known)
	{
		boolean known_north=((known & 1) != 0);
		boolean known_east=((known & 2) != 0);
		boolean known_south=((known & 4) != 0);
		boolean known_west=((known & 8) != 0);
		
		char tile_north= known_north ? tile_type : ' ';
		char tile_east= known_east ? tile_type : ' ';
		char tile_south= known_south ? tile_type : ' ';
		char tile_west= known_west ? tile_type : ' ';
		
		return new GameTile(tile_north, tile_east, tile_south, tile_west, visible, known, "");
	}
	
	public GameTile getCreatureKnownTile(HumanLikeCreature c, int l, int zeile, int spalte)
	{
		if ((zeile >= mapsize_y) || (zeile<0) || 
		(spalte>=mapsize_x) || (spalte<0))
			return createGameTile(' ', 0, 0);
		else if (c.getKnown(l, zeile, spalte)		==AyirahStaticVars.VISIBLE_KNOWN_NONE)
		return createGameTile(' ', 0, 0);
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
					AyirahStaticVars.non_block_tiles_tile_parts[array_index][i] : ' ';
				}
				
				return new GameTile(tile_part[0], tile_part[1], 
				tile_part[2], tile_part[3], c.getVisible(l, zeile, spalte), 
				c.getKnown(l, zeile, spalte),"");
			}
			
			else if (r=='X' || r=='x')
			{
				GameTile gt=createGameTile('.', c.getVisible(l, zeile, spalte), 
				c.getKnown(l, zeile, spalte));
				if (r=='x')
					gt.setItem("box_open");
				else
					gt.setItem("box_closed");
				return gt;
			}
			
			else
			{
				return createGameTile(getTile(l, zeile, spalte), 
				c.getVisible(l, zeile, spalte), c.getKnown(l, zeile, spalte));
			}
		}
	}
	
	public AyirahCharacter getCharacter()
	{
		return ayirah_char;
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
	
	public boolean isWall(int l, int y, int x)
	{
		int act_tile=this.getTile(l,y,x);
		
		return (act_tile=='#' || act_tile=='1' || act_tile=='2' ||
		act_tile=='3' || act_tile=='4' || act_tile=='I' || act_tile=='i' ||
		act_tile=='-' || act_tile=='_');
	}
	
	public int getVisibilityType(int l, int zeile, int spalte)
	{
		if (isValidCoordPair(l, spalte, zeile))
		{
			char t=getTile(l, zeile, spalte);
			
			switch (t)
			{
				case '.':
					return 0;
				case ' ':
					return 0;
				case 'x':
					return 0;
				case 'X':
					return 0;
				case '<':
					return 0;
				case '>':
					return 0;
				case '|':
					return 0;
				case '1':
					return 1;
				case '4':
					return 3;
				case '2':
					return 4;
				case '3':
					return 6;
				case 's':
					return 2;
				case 'w':
					return 5;
				case '#':
					return 7;
				case 'I':
					return 2;
				case 'i':
					return 8;
				case '-':
					return 5;
				case '_':
					return 9;
				default:
					return -1;	
			}
		}
		else
		{
			return -1;
		}
	}
	public void move(int direction, Creature c) throws IllegalTurnException
	{
		if (isMovePossible(direction, c))
		{
			c.setPosX(c.getPosX()+AyirahStaticVars.direction_modifier[direction][0]);
			c.setPosY(c.getPosY()+AyirahStaticVars.direction_modifier[direction][1]);
			c.calculateVisible();
		}
		else 
			throw new IllegalTurnException("Zug unmöglich");
	}
	
	protected boolean isDoor(int layer, int zeile, int spalte)
	{
		char t=getTile(layer, zeile, spalte);
		
		return (t=='I' || t=='i' || t=='-' || t=='_');
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 * true, wenn Erfolg
	 * false, wenn Misserfolg
	 * @throws IllegalTurnException
	 */
	public boolean openThing(Creature c) throws IllegalTurnException
	{
		if (c.getViewDirection()%2==1 && 
		isDoor(c.getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
		c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0]))
			throw new IllegalTurnException("Ungültige Richtung");
			
		char tile=getTile(
		c.getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
		c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0]);
		
		if (!(tile=='-' || tile=='I' || tile=='X'))
			throw new IllegalTurnException("Keine geschlossenes Ding");
		
		if (tile=='-' && !(c.getViewDirection()==AyirahStaticVars.DIRECTION_NORTH || 
		c.getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH))	
			throw new IllegalTurnException("Ungültige Richtung");
		
		if (tile=='I' && !(c.getViewDirection()==AyirahStaticVars.DIRECTION_WEST || 
		c.getViewDirection()==AyirahStaticVars.DIRECTION_EAST))	
			throw new IllegalTurnException("Ungültige Richtung");
		
		if (tile=='-')
		{
			setTile(c.getLayer(), 
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], '_');
			c.calculateVisible();
			return true;
		}
		
		else if (tile=='I')
		{
			setTile(c.getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], 'i');
			c.calculateVisible();
			return true;
		}
		else if (tile=='X' && isReachable(c.getViewDirection(), c))
		{
			setTile(c.getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], 'x');
			return true;
		}
		
		else return false;
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 * true, wenn Erfolg
	 * false, wenn Misserfolg
	 * @throws IllegalTurnException
	 */
	public boolean closeThing(Creature c) throws IllegalTurnException
	{
		if (c.getViewDirection()%2==1 && 
		isDoor(c.getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
		c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0]))
			throw new IllegalTurnException("Ungültige Richtung");
		
		char tile=getTile(c.getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
		c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0]);
	
		if (!(tile=='_' || tile=='i' || tile=='x'))
			throw new IllegalTurnException("Keine offenes Ding");
	
		if (tile=='_' && !(c.getViewDirection()==AyirahStaticVars.DIRECTION_NORTH || 
		c.getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH))	
			throw new IllegalTurnException("Ungültige Richtung");
	
		if (tile=='i' && !(c.getViewDirection()==AyirahStaticVars.DIRECTION_WEST || 
		c.getViewDirection()==AyirahStaticVars.DIRECTION_EAST))	
			throw new IllegalTurnException("Ungültige Richtung");
	
		if (tile=='_')
		{
			setTile(c.getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], '-');
			c.calculateVisible();
			return true;
		}
	
		else if (tile=='i')
		{
			setTile(c.getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], 'I');
			c.calculateVisible();
			return true;
		}
		else if (tile=='x' && isReachable(c.getViewDirection(), c))
		{
			setTile(c.getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[c.getViewDirection()][1], 
			c.getPosX()+AyirahStaticVars.direction_modifier[c.getViewDirection()][0], 'X');
			return true;
		}
		
		else return false;
	}
	
	protected boolean isMovePossible(int direction, Creature c)
	{
		if (direction<0 || direction>=8)
			return false;
		
		char tile_to=this.getTile(getCharacter().getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[direction][1],
		c.getPosX()+AyirahStaticVars.direction_modifier[direction][0]);
		
		if (tile_to=='.' || tile_to=='i' || tile_to=='_' || 
		tile_to=='<' || tile_to=='>' || tile_to=='|')
		{
			if (direction%2==1)
			{
				char tile_before=this.getTile(getCharacter().getLayer(),
				c.getPosY()+AyirahStaticVars.direction_modifier[(direction+7)%8][1],
				c.getPosX()+AyirahStaticVars.direction_modifier[(direction+7)%8][0] );
				
				char tile_after=this.getTile(getCharacter().getLayer(),
				c.getPosY()+AyirahStaticVars.direction_modifier[(direction+1)%8][1],
				c.getPosX()+AyirahStaticVars.direction_modifier[(direction+1)%8][0] );
				
				if (tile_before=='.' || tile_before=='>' || 
				tile_after=='.' || tile_after=='>') 
				// nur an leeren Tiles oder nach unten gehenden Treppen kann sich easy
				// vorbeigeschlichen werden
					return true;
					
				else
				{
					if (direction==AyirahStaticVars.DIRECTION_NORTH_EAST)
					{
						if (
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='4' || 
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='2')
							return true;
						else return false;
					}
					
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_WEST)
					{
						if (
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='4' || 
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='2')
							 return true;
						else return false;
					}
					
					else if (direction==AyirahStaticVars.DIRECTION_NORTH_WEST)
					{
						if (
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='3' || 
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='1')
							return true;
						else return false;
					}
					
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_EAST)
					{
						if (
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='1' || 
						this.getTile(getCharacter().getLayer(),
						c.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						c.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='3')
							return true;
						else return false;
					}
					
					else return false;
				}
			}
			
			else // if (direction%2==0)
			{
				char tile_from=this.getTile(getCharacter().getLayer(),
				c.getPosY(), c.getPosX());
				
				if (tile_from=='_' && (direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
				
				else if (tile_from=='i' && (direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
				
				if (tile_to=='_' && (direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
				
				else if (tile_to=='i' && (direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
					
				else return true;
			}
		}
		else return false;
	}
	
	
	protected boolean isReachable(int direction, Creature c)
	{
		if (direction<0 || direction>=8)
			return false;
		
		char tile_to=this.getTile(getCharacter().getLayer(),
		c.getPosY()+AyirahStaticVars.direction_modifier[direction][1],
		c.getPosX()+AyirahStaticVars.direction_modifier[direction][0]);
		
		if (direction%2==1)
		{
			char tile_before=this.getTile(getCharacter().getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[(direction+7)%8][1],
			c.getPosX()+AyirahStaticVars.direction_modifier[(direction+7)%8][0] );
			
			char tile_after=this.getTile(getCharacter().getLayer(),
			c.getPosY()+AyirahStaticVars.direction_modifier[(direction+1)%8][1],
			c.getPosX()+AyirahStaticVars.direction_modifier[(direction+1)%8][0] );
				
			if (tile_before=='.' || tile_before=='>' || 
			tile_after=='.' || tile_after=='>') 
			// nur an leeren Tiles oder nach unten gehenden Treppen kann sich easy
			// vorbeigeschlichen werden
				return true;
			
			else
			{
				if (direction==AyirahStaticVars.DIRECTION_NORTH_EAST)
				{
					if (
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_NORTH][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_NORTH][0])=='4' || 
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_EAST][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_EAST][0])=='2')
						return true;
					else return false;
				}
				
				else if (direction==AyirahStaticVars.DIRECTION_SOUTH_WEST)
				{
					if (
					this.getTile(getCharacter().getLayer(),					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_WEST][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_WEST][0])=='4' || 
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_SOUTH][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_SOUTH][0])=='2')
						return true;
					else return false;
				}
				
				else if (direction==AyirahStaticVars.DIRECTION_NORTH_WEST)
				{
					if (
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_WEST][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_WEST][0])=='3' || 
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_NORTH][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_NORTH][0])=='1')
						return true;
					else return false;
				}
				
				else if (direction==AyirahStaticVars.DIRECTION_SOUTH_EAST)
				{
					if (
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_EAST][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_EAST][0])=='1' || 
					this.getTile(getCharacter().getLayer(),
					c.getPosY()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_SOUTH][1],
					c.getPosX()+AyirahStaticVars.direction_modifier
					[AyirahStaticVars.DIRECTION_SOUTH][0])=='3')
						return true;
					else return false;
				}
				
				else return false;
			}
		}
		
		else // if (direction%2==0)
		{
			char tile_from=this.getTile(getCharacter().getLayer(),
			c.getPosY(), c.getPosX());
			
			if (tile_from=='_' && (direction==AyirahStaticVars.DIRECTION_EAST ||
			direction==AyirahStaticVars.DIRECTION_WEST))
				return false;
			
			else if (tile_from=='i' && (direction==AyirahStaticVars.DIRECTION_NORTH || 
			direction==AyirahStaticVars.DIRECTION_SOUTH))
				return false;
			
			if (tile_to=='_' && (direction==AyirahStaticVars.DIRECTION_EAST ||
			direction==AyirahStaticVars.DIRECTION_WEST))
				return false;
			
			else if (tile_to=='i' && (direction==AyirahStaticVars.DIRECTION_NORTH || 
			direction==AyirahStaticVars.DIRECTION_SOUTH))
				return false;
			
			else return true;
		}
	 }
}
