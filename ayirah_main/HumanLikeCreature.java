 /*
 * Created on 11.10.2003
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
public abstract class HumanLikeCreature extends Creature {
	public VisibleKnownNode[][][] isVisible;
	public VisibleKnownNode[][][] isKnown;
	protected boolean[][][] visibleWalls;	
	public HumanLikeCreature(GameMap map, int l, int x, int y, int direction) {
		super(map, l, x, y, direction);		
		isVisible=new VisibleKnownNode[map.getLayersCount()][map.getHeight()][map.getWidth()];
		isKnown=new VisibleKnownNode[map.getLayersCount()][map.getHeight()][map.getWidth()];
		visibleWalls=new boolean[map.getLayersCount()][map.getHeight()][map.getWidth()];
		
		for (int i=0; i<map.getLayersCount(); i++)
			for (int j=0; j<map.getHeight(); j++)
				for (int k=0; k<map.getWidth(); k++)
				{
					isVisible[i][j][k]=new VisibleKnownNode();
					isKnown[i][j][k]=new VisibleKnownNode();
				}
	}
	
	public int getVisible(int l, int zeile, int spalte)
	{
		//in return 15; zu Testzwecken ersetzen
		if ((zeile >= map.getHeight()) || (zeile<0) || 
		(spalte>=map.getWidth()) || (spalte<0))
			return AyirahStaticVars.VISIBLE_KNOWN_NONE;
		else return isVisible[l][zeile][spalte].getMainType(); 		// wenn durch return (VisibleKnownNode) [...] .clone() ersetzt wird, kommt es zu Grafikfehlern
	}
	
	protected void addVisible(int l, int x, int y, int main_type)
	{
		if (map.isValidCoordPair(l, x,y))
		{
			isVisible[l][y][x].addType(main_type);				
			int act_tile=map.getTile(l, y,x);			
			if (map.isWall(l, y,x))
			{
				visibleWalls[l][y][x]=true;
			}
		}		
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	public int getKnown(int l, int zeile, int spalte)
	{
		if ((spalte >= map.getWidth()) || (spalte<0) || 
		(zeile>=map.getHeight()) || (zeile<0))
				return AyirahStaticVars.VISIBLE_KNOWN_NONE;
		else return isKnown[l][zeile][spalte].getMainType();		// (vermutlich) wenn durch return (VisibleKnownNode) [...] .clone() ersetzt wird, kommt es zu Grafikfehlern
	}
	
	protected void removeVisible(int l, int x, int y, int main_type)
	{
		if (map.isValidCoordPair(l, x,y))
		{
			isVisible[l][y][x].removeType(main_type);			
			if (map.isWall(l, y,x) && getVisible(l, y, x)==0)
			{
				visibleWalls[l][y][x]=false;
			}
		}
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	protected void setVisible(int l, int x, int y, int main_type, int diagonal_visible)
	{
		if (map.isValidCoordPair(l,x,y))
		{
			isVisible[l][y][x].setType(main_type);			
			if (map.isWall(l, y,x) && getVisible(l,y,x)==0)
			{
				visibleWalls[l][y][x]=false;
			}
		}
		
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	protected void addKnown(int l, int x, int y, int main_type)
	{
		if (map.isValidCoordPair(l,x,y))
		{
			isKnown[l][y][x].addType(main_type);
		}
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	public void goUp() throws IllegalTurnException
	{
		char actTile=map.getTile(getLayer(), getPosY(), getPosX());
		
		if (!(actTile=='<' || actTile=='|'))
			throw new IllegalTurnException("GoUp(): Keine gültige Treppe");
			
		else if (getLayer()<=0) // <=: nur zur Sicherheit; eigentlich reichte ==
			throw new IllegalTurnException("GoUp(): Oberste Ebene");
		
		setLayer(getLayer()-1);
		calculateVisible();
	}
	
	public void goDown() throws IllegalTurnException
	{
		char actTile=map.getTile(getLayer(), getPosY(), getPosX());
		
		if (!(actTile=='>' || actTile=='|'))
			throw new IllegalTurnException("GoDown(): Keine gültige Treppe");
			
		else if (getLayer()>=(map.getLayersCount()-1)) // >=: nur zur Sicherheit; eigentlich reichte ==
			throw new IllegalTurnException("GoDown(): Unterste Ebene");
		
		setLayer(getLayer()+1);
		calculateVisible();
	}
	
	protected void makeVisibleKnown()
	{
		// alles Sichtbare dem Bekannten zufügen
		for (int zeile=0; zeile<map.getHeight(); zeile++)
			for (int spalte=0; spalte<map.getWidth(); spalte++)
			{
				int visible=getVisible(getLayer(), zeile, spalte);
				
				isKnown[getLayer()][zeile][spalte].addType
				(isVisible[getLayer()][zeile][spalte].getMainType()
				);
			}
	}
	
	protected void removeWallHiddenTiles()
	{
		for (int zeile=0; zeile<map.getHeight();zeile++)
			for (int spalte=0; spalte<map.getWidth(); spalte++)
			{
				if (visibleWalls[getLayer()][zeile][spalte] &&  !(zeile==map.getCharacter().getPosY() &&
				spalte==map.getCharacter().getPosX()))
				{
					char wall_type=map.getTile(getLayer(), zeile, spalte);					
					if (spalte==this.getPosX())
					{						// Fall: Wand nördlich vom Character
						if (this.getPosY()-zeile>0)
						{
							if (this.getPosY()-zeile==1)
							{
								if (!(wall_type=='1' || wall_type=='4' || wall_type=='_'))
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_NORTH, false, false,0,0);
									
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
								
								if (wall_type=='1')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_NORTH, true, false, 0,0);
								
								if (wall_type=='4')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_NORTH, false, true, 0,0);
							}
							
							else
							{
								if (!(wall_type=='_'))
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_NORTH, true, true,0,0);
								
								if (!(wall_type=='1' || wall_type=='4' || wall_type=='_'))
								{
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
							}
						}
												// Fall: Wand südlich vom Character
						else
						{
							if (getPosY()-zeile==-1)
							{
								if (!(wall_type=='2' || wall_type=='3' || wall_type=='_'))
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_SOUTH, false, false,0,0);
									
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
								
								if (wall_type=='2')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_SOUTH, false, true,0,0);
								
								if (wall_type=='3')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_SOUTH, true, false,0,0);
							}
							
							else
							{
								if (!(wall_type=='_'))
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_SOUTH, true, true,0,0);
								
								if (!(wall_type=='2' || wall_type=='3' || wall_type=='_'))
								{
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
							}
						}
					}
					
					else if (zeile==getPosY())
					{						// Fall: Wand westlich vom Character
						if (getPosX()-spalte>0)
						{
							if (getPosX()-spalte==1)
							{
								if (!(wall_type=='3' || wall_type=='4' || wall_type=='i'))
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_WEST, false, false,0,0);
									
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
								
								if (wall_type=='3')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_WEST, false, true,0,0);
								
								if (wall_type=='4')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_WEST, true, false,0,0);
							}
							
							else
							{
								if (!(wall_type=='i'))
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_WEST, true, true,0,0);
								
								if (!(wall_type=='3' || wall_type=='4' || wall_type=='i'))
								{
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
							}
						}
												// Fall: Wand östlich vom Character
						else
						{
							if (getPosX()-spalte==-1)
							{
								if (!(wall_type=='1' || wall_type=='2' || wall_type=='i'))
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_EAST, false, false,0,0);
									
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
								
								if (wall_type=='1')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_EAST, false, true,0,0);
								
								if (wall_type=='2')
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_EAST, true, false,0,0);
							}
							
							else
							{
								if (!(wall_type=='i'))
								removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
								AyirahStaticVars.DIRECTION_EAST, true, true,0,0);
								
								if (!(wall_type=='1' || wall_type=='2' || wall_type=='i'))
								{
									removeVisible(getLayer(), spalte, zeile, 
									15-AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
							}
						}
					}
					
					
					/* Jetzt die Diagonalen */
					if (getPosX()-spalte==getPosY()-zeile)
					{
						// Fall Diagonale NW -> SO
						if (getPosX()-spalte<0)
						{
							if (getPosX()-spalte==-1)
							{
								if (!(wall_type=='1' || wall_type=='3'))
								{
									removeOddDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_SOUTH_EAST);
									
									if (wall_type!='2')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
								}
								
								if (wall_type=='1')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_EAST, true, false, 0, -1);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
								
								if (wall_type=='3')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_SOUTH, false, true, -1, 0);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
							}
							
							else
							{
								if (!(wall_type=='1' || wall_type=='3'))
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, 
									map.getHeight()-1-zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile+i))
											removeVisible(getLayer(), spalte+i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_ALL);
										if (map.isValidCoordPair(getLayer(), spalte+i+1, zeile+i))
											removeVisible(getLayer(), spalte+i+1, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
										if (map.isValidCoordPair(getLayer(), spalte+i, zeile+i+1))
											removeVisible(getLayer(), spalte+i, zeile+i+1,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
									}
									
									if (wall_type!='2')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
								}
								
								if (wall_type=='1')
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, 
									map.getHeight()-1-zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile+i))
											removeVisible(getLayer(), spalte+i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
										if (map.isValidCoordPair(getLayer(), spalte+i+1, zeile+i))
											removeVisible(getLayer(), spalte+i+1, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
								
								if (wall_type=='3')
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, 
									map.getHeight()-1-zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile+i))
											removeVisible(getLayer(), spalte+i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
											
										if (map.isValidCoordPair(getLayer(), spalte+i, zeile+i+1))
											removeVisible(getLayer(), spalte+i, zeile+i+1,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
							}
						}
						
						// Fall Diagonale SO -> NW
						else // if (charpos_x-spalte)>0 danach ist verzeichtbar
						{
							if (getPosX()-spalte==1)
							{
								if (!(wall_type=='1' || wall_type=='3'))
								{
									removeOddDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_NORTH_WEST);
									
									if (wall_type!='4')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
								}
								
								if (wall_type=='1')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_NORTH, false, true, -1, 0);
								
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
								
								if (wall_type=='3')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_WEST, true, false, 0, -1);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
							}
							
							else
							{
								if (!(wall_type=='1' || wall_type=='3'))
								{
									for (int i=0; i<=Math.min(spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile-i))
											removeVisible(getLayer(), spalte-i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_ALL);
										if (map.isValidCoordPair(getLayer(), spalte-i-1, zeile-i))
											removeVisible(getLayer(), spalte-i-1, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
										if (map.isValidCoordPair(getLayer(), spalte-i, zeile-i-1))
											removeVisible(getLayer(), spalte-i, zeile-i-1,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
									}
									
									if (wall_type!='4')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
								}
								
								if (wall_type=='1')
								{
									for (int i=0; i<=Math.min(spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile-i))
											removeVisible(getLayer(), spalte-i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
											
										if (map.isValidCoordPair(getLayer(), spalte-i, zeile-i-1))
											removeVisible(getLayer(), spalte-i, zeile-i-1,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
								
								if (wall_type=='3')
								{
									for (int i=0; i<=Math.min(spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile-i))
											removeVisible(getLayer(), spalte-i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
										if (map.isValidCoordPair(getLayer(), spalte-i-1, zeile-i))
											removeVisible(getLayer(), spalte-i-1, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);			
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
							}
						}
					}
					
					// Fall: Diagonale SW -> NO
					else if (getPosX()-spalte==zeile-getPosY())
					{
						if (getPosX()-spalte<0)
						{
							if (getPosX()-spalte==-1)
							{
								if (!(wall_type=='2' || wall_type=='4'))
								{
									removeOddDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_NORTH_EAST);
									
									if (wall_type!='1')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
								}
								
								if (wall_type=='4')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_NORTH, true, false, 0, -1);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
								
								if (wall_type=='2')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_EAST, false, true, -1, 0);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
							}
							
							else
							{
								if (!(wall_type=='2' || wall_type=='4'))
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile-i))
											removeVisible(getLayer(), spalte+i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_ALL);
										if (map.isValidCoordPair(getLayer(), spalte+i+1, zeile-i))
											removeVisible(getLayer(), spalte+i+1, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
										if (map.isValidCoordPair(getLayer(), spalte+i, zeile-i-1))
											removeVisible(getLayer(), spalte+i, zeile-i-1,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
									}
									
									if (wall_type!='1')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
								}
								
								if (wall_type=='4')
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile-i))
											removeVisible(getLayer(), spalte+i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
											
										if (map.isValidCoordPair(getLayer(), spalte+i, zeile-i-1))
											removeVisible(getLayer(), spalte+i, zeile-i-1,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_NORTH);
								}
								
								if (wall_type=='2')
								{
									for (int i=0; i<=Math.min(map.getWidth()-1-spalte, zeile); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte+i, zeile-i))
											removeVisible(getLayer(), spalte+i, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
										if (map.isValidCoordPair(getLayer(), spalte+i+1, zeile-i))
											removeVisible(getLayer(), spalte+i+1, zeile-i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
									}
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
							}
						}
						
						// Fall: Diagonale NO -> SW
						else // if (charpos_x-spalte)>0 danach ist verzeichtbar
						{
							if (getPosX()-spalte==1)
							{
								if (!(wall_type=='2' || wall_type=='4'))
								{
									removeOddDirectionWallInvisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.DIRECTION_SOUTH_WEST);
									
									if (wall_type!='3')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
								}
								
								if (wall_type=='2')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_SOUTH, true, false, 0, -1);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
								
								if (wall_type=='4')
								{
									removeEvenDirectionWallInvisible(getLayer(), spalte, zeile,
									AyirahStaticVars.DIRECTION_WEST, false, true, -1, 0);
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
							}
							
							else
							{
								if (!(wall_type=='2' || wall_type=='4'))
								{
									for (int i=0; i<=Math.min(spalte, (map.getHeight()-1-zeile)); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile+i))
											removeVisible(getLayer(), spalte-i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_ALL);
										if (map.isValidCoordPair(getLayer(), spalte-i-1, zeile+i))
											removeVisible(getLayer(), spalte-i-1, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
										if (map.isValidCoordPair(getLayer(), spalte-i, zeile+i+1))
											removeVisible(getLayer(), spalte-i, zeile+i+1,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
									}
									
									if (wall_type!='3')
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
								}
								
								if (wall_type=='2')
								{
									for (int i=0; i<=Math.min(spalte, (map.getHeight()-1-zeile)); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile+i))
											removeVisible(getLayer(), spalte-i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
										if (map.isValidCoordPair(getLayer(), spalte-i, zeile+i+1))
											removeVisible(getLayer(), spalte-i, zeile+i+1,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
								}
								
								if (wall_type=='4')
								{
									for (int i=0; i<=Math.min(spalte, (map.getHeight()-1-zeile)); i++)
									{
										if (!(i==0) && map.isValidCoordPair(getLayer(), spalte-i, zeile+i))
											removeVisible(getLayer(), spalte-i, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
										if (map.isValidCoordPair(getLayer(), spalte-i-1, zeile+i))
											removeVisible(getLayer(), spalte-i-1, zeile+i,
											AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
									}
									
									removeVisible(getLayer(), spalte, zeile, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
							}
						}
					}
					
					// jetzt die restlichen 8 Fälle
					if (getPosX()>spalte && getPosY()>zeile)
					{
						if (getPosY()-getPosX()>zeile-spalte)
						{
							if (!(wall_type=='1'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_NORTH, false, true,0,0);
							
							if (wall_type=='2')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
							
							if (wall_type=='3')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
							
							if (wall_type=='1')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_NORTH, false, true,-1,0);
						}
						
						else if (getPosY()-getPosX()<zeile-spalte)
						{
							if (!(wall_type=='3'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_WEST, true, false,0,0);
							
							if (wall_type=='1')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
							
							if (wall_type=='2')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
							
							if (wall_type=='3')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_WEST, true, false,0,-1);
						}
					}
					
					else if (getPosX()>spalte && getPosY()<zeile)
					{
						if (getPosY()+getPosX()>zeile+spalte)
						{
							if (!(wall_type=='4'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_WEST, false, true,0,0);
							
							if (wall_type=='1')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
							
							if (wall_type=='2')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
							
							if (wall_type=='4')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_WEST, false, true,-1,0);
						}
					
						else if (getPosY()+getPosX()<zeile+spalte)
						{
							if (!(wall_type=='2'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_SOUTH, true, false,0,0);
							
							if (wall_type=='1')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
							
							if (wall_type=='4')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
							
							if (wall_type=='2')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_SOUTH, true, false,0,-1);
						}
					}
					
					else if (getPosX()<spalte && getPosY()<zeile)
					{
						if (getPosY()-getPosX()>zeile-spalte)
						{
							if (!(wall_type=='1'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_EAST, true, false,0,0);
							
							if (wall_type=='4')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
							
							if (wall_type=='3')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
							
							if (wall_type=='1')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_EAST, true, false,0,-1);
						}
						
						else if (getPosY()-getPosX()<zeile-spalte)
						{
							if (!(wall_type=='3'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_SOUTH, false, true,0,0);
							
							if (wall_type=='4')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
							
							if (wall_type=='1')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_WEST);
							
							if (wall_type=='3')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_SOUTH, false, true,-1,0);
						}
					}
					
					else if (getPosX()<spalte && getPosY()>zeile)
					{
						if (getPosY()+getPosX()>zeile+spalte)
						{
							if (!(wall_type=='4'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_NORTH, true, false,0,0);
							
							if (wall_type=='2')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_WEST);
							
							if (wall_type=='3')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
							
							if (wall_type=='4')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_NORTH, true, false,0,-1);
						}
						
						else if (getPosY()+getPosX()<zeile+spalte)
						{
							if (!(wall_type=='2'))
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_EAST, false, true,0,0);
							
							if (wall_type=='2')
							removeEvenDirectionWallInvisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.DIRECTION_EAST, false, true,-1,0);
							
							if (wall_type=='3')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_NORTH_EAST);
							
							if (wall_type=='4')
							removeVisible(getLayer(), spalte, zeile, 
							AyirahStaticVars.VISIBLE_KNOWN_SOUTH_EAST);
						}
					}
				}
			}
	}
		
	protected void removeEvenDirectionWallInvisible(int l, int x, int y, int direction, 
	boolean visible_left, boolean visible_right, int delta_left, int delta_right)
	{
		int max;
		
		if (direction==AyirahStaticVars.DIRECTION_NORTH)			
			max=y;
		else if (direction==AyirahStaticVars.DIRECTION_EAST)
			max=map.getWidth()-1-x;
		else if (direction==AyirahStaticVars.DIRECTION_SOUTH)
			max=map.getHeight()-1-y;
		else if (direction==AyirahStaticVars.DIRECTION_WEST)
			max=x;
		else
			return;
		
		int dir_left=(direction+6)%8;
		int dir_right=(direction+2)%8;
			
		for (int i=0; i<=max; i++)
			for (int j=(visible_right?0:(-i-1-delta_right)); j<=(visible_left?0:(i+1+delta_left)); j++)
			{
				if (map.isValidCoordPair(l, 
				x+i*AyirahStaticVars.direction_modifier[direction][0]+
				j*AyirahStaticVars.direction_modifier[dir_left][0], 
				y+i*AyirahStaticVars.direction_modifier[direction][1]+
				j*AyirahStaticVars.direction_modifier[dir_left][1]))
				{
					if (!(j==0 && i==0))
					{
					
					// rechts
					if (j==(-i-1-delta_right))
					{
						removeVisible(l,
						x+i*AyirahStaticVars.direction_modifier[direction][0]+
						j*AyirahStaticVars.direction_modifier[dir_left][0], 
						y+i*AyirahStaticVars.direction_modifier[direction][1]+
						j*AyirahStaticVars.direction_modifier[dir_left][1],
						AyirahStaticVars.dir_right_visible_modifier[direction/2]); 
					}
					
					// links
					else if (j==(i+1+delta_left))
					{
						removeVisible(l,
						x+i*AyirahStaticVars.direction_modifier[direction][0]+
						j*AyirahStaticVars.direction_modifier[dir_left][0], 
						y+i*AyirahStaticVars.direction_modifier[direction][1]+
						j*AyirahStaticVars.direction_modifier[dir_left][1],
						AyirahStaticVars.dir_left_visible_modifier[direction/2]);
					}
							
					else
						removeVisible(l,
						x+i*AyirahStaticVars.direction_modifier[direction][0]+
						j*AyirahStaticVars.direction_modifier[dir_left][0], 
						y+i*AyirahStaticVars.direction_modifier[direction][1]+
						j*AyirahStaticVars.direction_modifier[dir_left][1],
						AyirahStaticVars.VISIBLE_KNOWN_ALL);
					}
				}
			}
	}
		
	protected void removeOddDirectionWallInvisible(int l, int x, int y, int direction)
	{
		if (!((direction==AyirahStaticVars.DIRECTION_NORTH_EAST) || 
		(direction==AyirahStaticVars.DIRECTION_SOUTH_EAST) ||
		(direction==AyirahStaticVars.DIRECTION_SOUTH_WEST) ||
		(direction==AyirahStaticVars.DIRECTION_NORTH_WEST)))
			return;
		
		for (int zeile=y; (zeile<map.getHeight() && (zeile>=0)); 
		zeile+=AyirahStaticVars.direction_modifier[direction][1])
			for (int spalte=x; (spalte<map.getWidth() && (spalte>=0)); 
			spalte+=AyirahStaticVars.direction_modifier[direction][0])
			{
				if (!((spalte==x) && (zeile==y)))
					removeVisible(l, spalte, zeile, AyirahStaticVars.VISIBLE_KNOWN_ALL);
			}
	}
		
	/**
	 * Die Methode calculateVisible() muss aus technischen 
	 * Gründen nach der Konstruktion des Objektes manuell 
	 * aufgerufen werden
	 */
	public void calculateVisible()
	{
		for (int i=0; i<map.getHeight(); i++)
			for (int j=0; j<map.getWidth(); j++)
			{
				setVisible(getLayer(), j, i, AyirahStaticVars.VISIBLE_KNOWN_NONE, 0);
				visibleWalls[getLayer()][i][j]=false;
			}
		
		addVisible(getLayer(), getPosX(), getPosY(), AyirahStaticVars.VISIBLE_KNOWN_ALL);
		
		if ((this.getViewDirection()%2)==0)
		{
			int max=0;
			int x=getPosX();
			int y=getPosY();
			
			int direction=getViewDirection();
			if (direction==AyirahStaticVars.DIRECTION_NORTH)			
				max=y;
			else if (direction==AyirahStaticVars.DIRECTION_EAST)
				max=map.getWidth()-1-x;
			else if (direction==AyirahStaticVars.DIRECTION_SOUTH)
				max=map.getHeight()-1-y;
			else if (direction==AyirahStaticVars.DIRECTION_WEST)
				max=x;
		
			int dir_left=(direction+6)%8;
			int dir_right=(direction+2)%8;
			
			for (int i=0; i<=max; i++)
				for (int j=(-i-2); j<=(i+2); j++)
				{
					if (map.isValidCoordPair(getLayer(), 
					x+i*AyirahStaticVars.direction_modifier[direction][0]+
					j*AyirahStaticVars.direction_modifier[dir_left][0], 
					y+i*AyirahStaticVars.direction_modifier[direction][1]+
					j*AyirahStaticVars.direction_modifier[dir_left][1]))
					{
						if (j==(-i-2))
						{
							addVisible(getLayer(),
							x+i*AyirahStaticVars.direction_modifier[direction][0]+
							j*AyirahStaticVars.direction_modifier[dir_left][0], 
							y+i*AyirahStaticVars.direction_modifier[direction][1]+
							j*AyirahStaticVars.direction_modifier[dir_left][1],
							AyirahStaticVars.dir_right_visible_modifier[getViewDirection()/2]); 
						}
						
						else if (j==(i+2))
						{
							addVisible(getLayer(), 
							x+i*AyirahStaticVars.direction_modifier[direction][0]+
							j*AyirahStaticVars.direction_modifier[dir_left][0], 
							y+i*AyirahStaticVars.direction_modifier[direction][1]+
							j*AyirahStaticVars.direction_modifier[dir_left][1],
							AyirahStaticVars.dir_left_visible_modifier[getViewDirection()/2]);
						}
							
						else if (!(j==0 && i==0))
							addVisible(getLayer(),
							x+i*AyirahStaticVars.direction_modifier[direction][0]+
							j*AyirahStaticVars.direction_modifier[dir_left][0], 
							y+i*AyirahStaticVars.direction_modifier[direction][1]+
							j*AyirahStaticVars.direction_modifier[dir_left][1],
							AyirahStaticVars.VISIBLE_KNOWN_ALL);
				}
			}
	}

	else if (getViewDirection()%2==1)
	{
		/* Erseinmal alles (theoretisch) Sichtbare feststellen
		 * (oben erfolgte schon ein Teil davon)
		 */
			for (int row=Math.max(getPosY()-AyirahStaticVars.direction_modifier
			[getViewDirection()][1],0); 
			row>=0 && row<map.getHeight(); 
			row+=AyirahStaticVars.direction_modifier[getViewDirection()][1])
				for (int col=Math.max(getPosX()-AyirahStaticVars.direction_modifier
				[getViewDirection()][0],0);
				col>=0 && col<map.getWidth(); 
				col+=AyirahStaticVars.direction_modifier
				[getViewDirection()][0])
				{
					if (!((row==getPosY()-AyirahStaticVars.direction_modifier
					[getViewDirection()][1]) && 
					(col==getPosX()-AyirahStaticVars.direction_modifier
					[getViewDirection()][0])))
						addVisible(getLayer(), col, row, 
						AyirahStaticVars.VISIBLE_KNOWN_ALL);
			
					if (row==getPosY()-AyirahStaticVars.direction_modifier
					[getViewDirection()][1] && 
					col==getPosX())
					{
						removeVisible(getLayer(), col, row, 
						15-
						(AyirahStaticVars.diagonal_view_visible[(getViewDirection()-1)/2]));
					}
			
					else if (row==getPosY() && 
					(col==getPosX()-AyirahStaticVars.direction_modifier
					[getViewDirection()][0]))
					{
						removeVisible(getLayer(), col, row, 
						15-
						(AyirahStaticVars.diagonal_view_visible[(getViewDirection()-1)/2]));
					}
				}
		}
		
		removeWallHiddenTiles();
		makeVisibleKnown();
	}

	/*
	 * (non-Javadoc)
	 * @see Creature#getTurn()
	 */
	public int getTurn() {
		return AyirahStaticVars.TURN_INVALID_TURN;
	}
}
