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

public class GameMap {
	protected String[][] map= // Format: [zeile][spalte]
	{
	   {".........................",
		".........................",
		".........................",
		".........................",
		"........#................",
		".........................",
		".........................",
		".........................",
		"...................>.....",
		"..........x..............",
		".........................",
		"........1X...............",
		".........................",
		".........................",
		".........................",
		".........................",
		".........................",
		" ...................>....",
		".........................",
		".........................",
		".........................",
		".........................",
		".........................",
		".........................",
		"........................."},
		
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
	//protected Creature[] monsters;
	
	protected int mapsize_x=25, mapsize_y=25, layers_count=3;
	
	public GameMap()
	{
		//monsters=new Creature[0];
		
		ayirah_char=new AyirahCharacter(this, 0, 1, 1, AyirahStaticVars.DIRECTION_SOUTH);
		ayirah_char.calculateVisible();
	}
	
	/*public int getMonstersCount()
	{
		return monsters.length;
	}*/
	
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
	
	public char getCharacterKnownTile(int l, int zeile, int spalte)
	{
		if ((zeile >= mapsize_y) || (zeile<0) || 
		(spalte>=mapsize_x) || (spalte<0))
			return ' ';
		else if (getCharacter().getMainKnown(l, zeile, spalte)==AyirahStaticVars.VISIBLE_NONE)
			return ' ';
		else
		{
			char r=map[l][zeile].charAt(spalte);
			int known=getCharacter().getMainKnown(l, zeile, spalte);
			
			if (r=='1')
			{
				if (known==(AyirahStaticVars.KNOWN_NORTH_EAST))
				return '#';
				else if (known==(AyirahStaticVars.KNOWN_SOUTH_WEST))
				return '.';
				else
				return r;
			}
			
			else if (r=='2')
			{
				if (known==(AyirahStaticVars.KNOWN_SOUTH_EAST))
				return '#';
				else if (known==(AyirahStaticVars.KNOWN_NORTH_WEST))
				return '.';
				else
				return r;
			}
			
			else if (r=='3')
			{
				if (known==(AyirahStaticVars.KNOWN_SOUTH_WEST))
				return '#';
				else if (known==(AyirahStaticVars.KNOWN_NORTH_EAST))
				return '.';
				else
				return r;
			}
			
			else if (r=='4')
			{
				if (known==(AyirahStaticVars.KNOWN_NORTH_WEST))
				return '#';
				else if (known==(AyirahStaticVars.KNOWN_SOUTH_EAST))
				return '.';
				else
				return r;
			}
			
			return r;
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
}
