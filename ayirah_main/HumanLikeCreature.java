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
public abstract class HumanLikeCreature extends Creature {
	public VisibleKnownNode[][][] isVisible;
	public VisibleKnownNode[][][] isKnown;	
	public HumanLikeCreature(GameMap map, int l, 
	int x, int y, int direction)
	{
		super(map, l, x, y, direction);		
		isVisible=new VisibleKnownNode[map.getLayersCount()]
		[map.getHeight()][map.getWidth()];
		isKnown=new VisibleKnownNode[map.getLayersCount()]
		[map.getHeight()][map.getWidth()];
		
		this.map=map;
		
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
		else return isVisible[l][zeile][spalte].getType(); 
	}
	
	
	protected void addVisible(int l, int x, int y, int main_type)
	{
		if (map.isValidCoordPair(l, x,y))
		{
			isVisible[l][y][x].addType(main_type);	
		}		
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	public int getKnown(int l, int zeile, int spalte)
	{
		if ((spalte >= map.getWidth()) || (spalte<0) || 
		(zeile>=map.getHeight()) || (zeile<0))
			return AyirahStaticVars.VISIBLE_KNOWN_NONE;
		else 
			return isKnown[l][zeile][spalte].getType();
	}
	
	protected void removeVisible(int l, int x, int y, int main_type)
	{
		if (map.isValidCoordPair(l, x,y))
		{
			isVisible[l][y][x].removeType(main_type);
		}
		else
			System.out.println("Ungültige Koordinate: x="+x+" y="+y);
	}
	
	protected void setVisible(int l, int x, int y, int main_type, int diagonal_visible)
	{
		if (map.isValidCoordPair(l,x,y))
		{
			isVisible[l][y][x].setType(main_type);
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
	
	public void move(int direction) throws IllegalTurnException
	{
		if (isMovePossible(direction))
		{
			this.setPosX(this.getPosX()+
			AyirahStaticVars.direction_modifier[direction][0]);
			this.setPosY(this.getPosY()+
			AyirahStaticVars.direction_modifier[direction][1]);
			this.calculateVisible();
		}
		
		else 
			throw new IllegalTurnException("Zug unmöglich");
	}
	
	public void goUp() throws IllegalTurnException
	{
		char actTile=map.getTile(getLayer(), getPosY(), getPosX());
		
		if (!(actTile=='<' || actTile=='|'))
			throw new IllegalTurnException("GoUp(): Keine gültige Treppe");
			
		else if (getLayer()<=0) // <=: nur zur Sicherheit; eigentlich reichte ==
			throw new IllegalTurnException("GoUp(): Oberste Ebene");
		
		if (map.isTileEmpty(getLayer()-1, getPosY(), getPosX()))
		{
		setLayer(getLayer()-1);
		calculateVisible();
		}
	}
	
	public void goDown() throws IllegalTurnException
	{
		char actTile=map.getTile(getLayer(), getPosY(), getPosX());
		
		if (!(actTile=='>' || actTile=='|'))
			throw new IllegalTurnException("GoDown(): Keine gültige Treppe");
			
		else if (getLayer()>=(map.getLayersCount()-1)) // >=: nur zur Sicherheit; 
													   // eigentlich reichte ==
			throw new IllegalTurnException("GoDown(): Unterste Ebene");
		
		if (map.isTileEmpty(getLayer()+1, getPosY(), getPosX()))
		{
		setLayer(getLayer()+1);
		calculateVisible();
		}
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 * true, wenn Erfolg
	 * false, wenn Misserfolg
	 * @throws IllegalTurnException
	 */
	public boolean openThing() throws IllegalTurnException
	{
		if (this.getViewDirection()%2==1 && 
		getMap().isDoor(this.getLayer(),
		this.getPosY()+
		AyirahStaticVars.direction_modifier[this.getViewDirection()][1], 
		this.getPosX()+
		AyirahStaticVars.direction_modifier[this.getViewDirection()][0]))
			throw new IllegalTurnException("Ungültige Richtung");
		
		char tile=getMap().getTile(
		this.getLayer(),
		this.getPosY()+AyirahStaticVars.direction_modifier[this.getViewDirection()][1], 
		this.getPosX()+AyirahStaticVars.direction_modifier[this.getViewDirection()][0]);
	
		if (!(tile=='-' || tile=='I' || tile=='X'))
			throw new IllegalTurnException("Keine geschlossenes Ding");
	
		if (tile=='-' && !(this.getViewDirection()==AyirahStaticVars.DIRECTION_NORTH || 
		this.getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH))	
			throw new IllegalTurnException("Ungültige Richtung");
	
		if (tile=='I' && !(this.getViewDirection()==AyirahStaticVars.DIRECTION_WEST || 
		this.getViewDirection()==AyirahStaticVars.DIRECTION_EAST))	
			throw new IllegalTurnException("Ungültige Richtung");
	
		if (tile=='-')
		{
			getMap().setTile(this.getLayer(), 
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], '_');
			this.calculateVisible();
			return true;
		}
	
		else if (tile=='I')
		{
			getMap().setTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], 'i');
			this.calculateVisible();
			return true;
		}
		else if (tile=='X' && isReachable(this.getViewDirection()))
		{
			getMap().setTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], 'x');
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
	public boolean closeThing() throws IllegalTurnException
	{
		if (this.getViewDirection()%2==1 && 
		getMap().isDoor(this.getLayer(),
		this.getPosY()+AyirahStaticVars.direction_modifier
		[this.getViewDirection()][1], 
		this.getPosX()+AyirahStaticVars.direction_modifier
		[this.getViewDirection()][0]))
			throw new IllegalTurnException("Ungültige Richtung");
	
		char tile=getMap().getTile(this.getLayer(),
		this.getPosY()+AyirahStaticVars.direction_modifier
		[this.getViewDirection()][1], 
		this.getPosX()+AyirahStaticVars.direction_modifier
		[this.getViewDirection()][0]);

		if (!(tile=='_' || tile=='i' || tile=='x'))
			throw new IllegalTurnException("Keine offenes Ding");

		if (tile=='_' && 
		!(this.getViewDirection()==AyirahStaticVars.DIRECTION_NORTH || 
		this.getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH))	
			throw new IllegalTurnException("Ungültige Richtung");

		if (tile=='i' && 
		!(this.getViewDirection()==AyirahStaticVars.DIRECTION_WEST || 
		this.getViewDirection()==AyirahStaticVars.DIRECTION_EAST))	
			throw new IllegalTurnException("Ungültige Richtung");

		if (tile=='_')
		{
			getMap().setTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], '-');
			this.calculateVisible();
			return true;
		}
		
		else if (tile=='i')
		{
			getMap().setTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], 'I');
			this.calculateVisible();
			return true;
		}
		
		else if (tile=='x' && isReachable(this.getViewDirection()))
		{
			getMap().setTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][1], 
			this.getPosX()+AyirahStaticVars.direction_modifier
			[this.getViewDirection()][0], 'X');
			return true;
		}
	
		else return false;
	}
	
	protected boolean isMovePossible(int direction)
	{
		if (direction<0 || direction>=8)
			return false;
		
		if (!map.isTileEmpty(getLayer(), 
		this.getPosY()+AyirahStaticVars.direction_modifier[direction][1],
		this.getPosX()+AyirahStaticVars.direction_modifier[direction][0]))
			return false;
		
		char tile_to=getMap().getTile(this.getLayer(),
		this.getPosY()+AyirahStaticVars.direction_modifier[direction][1],
		this.getPosX()+AyirahStaticVars.direction_modifier[direction][0]);
		
		if (tile_to=='.' || tile_to=='i' || tile_to=='_' || 
		tile_to=='<' || tile_to=='>' || tile_to=='|')
		{
			if (direction%2==1)
			{
				char tile_before=getMap().getTile(this.getLayer(),
				this.getPosY()+AyirahStaticVars.direction_modifier
				[(direction+7)%8][1],
				this.getPosX()+AyirahStaticVars.direction_modifier
				[(direction+7)%8][0] );
			
				char tile_after=getMap().getTile(this.getLayer(),
				this.getPosY()+AyirahStaticVars.direction_modifier
				[(direction+1)%8][1],
				this.getPosX()+AyirahStaticVars.direction_modifier
				[(direction+1)%8][0] );
			
				if (tile_before=='.' || tile_before=='>' || 
				tile_after=='.' || tile_after=='>') 
				// nur an leeren Tiles oder nach unten 
				// gehenden Treppen kann sich easy
				// vorbeigeschlichen werden
					return true;
				
				else
				{
					if (direction==AyirahStaticVars.DIRECTION_NORTH_EAST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='4' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='2')
							return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_WEST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='4' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='2')
							 return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_NORTH_WEST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='3' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='1')
							return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_EAST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='1' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='3')
							return true;
						else return false;
					}
				
					else return false;
				}
			}
		
			else // if (direction%2==0)
			{
				char tile_from=getMap().getTile(this.getLayer(),
				this.getPosY(), this.getPosX());
			
				if (tile_from=='_' && 
				(direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
			
				else if (tile_from=='i' && 
				(direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
			
				if (tile_to=='_' && 
				(direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
			
				else if (tile_to=='i' && 
				(direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
				
				else return true;
			}
		}
		
		else return false;
		}
	
	
		protected boolean isReachable(int direction)
		{
			if (direction<0 || direction>=8)
				return false;
		
			char tile_to=getMap().getTile(this.getLayer(),
			this.getPosY()+AyirahStaticVars.direction_modifier[direction][1],
			this.getPosX()+AyirahStaticVars.direction_modifier[direction][0]);
		
			if (direction%2==1)
			{
				char tile_before=getMap().getTile(this.getLayer(),
				this.getPosY()+
				AyirahStaticVars.direction_modifier[(direction+7)%8][1],
				this.getPosX()+
				AyirahStaticVars.direction_modifier[(direction+7)%8][0] );
			
				char tile_after=getMap().getTile(this.getLayer(),
				this.getPosY()+
				AyirahStaticVars.direction_modifier[(direction+1)%8][1],
				this.getPosX()+
				AyirahStaticVars.direction_modifier[(direction+1)%8][0] );
				
				if (tile_before=='.' || tile_before=='>' || 
				tile_after=='.' || tile_after=='>') 
				// nur an leeren Tiles oder nach unten 
				// gehenden Treppen kann sich easy
				// vorbeigeschlichen werden
					return true;
			
				else
				{
					if (direction==AyirahStaticVars.DIRECTION_NORTH_EAST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='4' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='2')
							return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_WEST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='4' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='2')
							return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_NORTH_WEST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_WEST][0])=='3' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_NORTH][0])=='1')
							return true;
						else return false;
					}
				
					else if (direction==AyirahStaticVars.DIRECTION_SOUTH_EAST)
					{
						if (
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_EAST][0])=='1' || 
						getMap().getTile(this.getLayer(),
						this.getPosY()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][1],
						this.getPosX()+AyirahStaticVars.direction_modifier
						[AyirahStaticVars.DIRECTION_SOUTH][0])=='3')
							return true;
						else return false;
					}
				
					else return false;
				}
			}
		
			else // if (direction%2==0)
			{
				char tile_from=getMap().getTile(this.getLayer(),
				this.getPosY(), this.getPosX());
			
				if (tile_from=='_' && (
				direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
			
				else if (tile_from=='i' && (
				direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
			
				if (tile_to=='_' && (
				direction==AyirahStaticVars.DIRECTION_EAST ||
				direction==AyirahStaticVars.DIRECTION_WEST))
					return false;
			
				else if (tile_to=='i' && (
				direction==AyirahStaticVars.DIRECTION_NORTH || 
				direction==AyirahStaticVars.DIRECTION_SOUTH))
					return false;
			
				else return true;
			}
		 }
	
	protected void makeVisibleKnown()
	{
		// alles Sichtbare dem Bekannten zufügen
		for (int zeile=0; zeile<map.getHeight(); zeile++)
			for (int spalte=0; spalte<map.getWidth(); spalte++)
			{
				int visible=getVisible(getLayer(), zeile, spalte);
				
				isKnown[getLayer()][zeile][spalte].addType
				(isVisible[getLayer()][zeile][spalte].getType()
				);
			}
	}
	
	/**
	 * @return int[4]: minX, maxX, minY, maxY
	 */
	protected int[] getMinMaxArea()
	{
		int[] back=new int[4];
		
		if (!(getViewDirection()==AyirahStaticVars.DIRECTION_NORTH_EAST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_EAST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH_EAST))
		back[0]=0;
		else
		back[0]=Math.max(0, getPosX()-1);
		
		if (!(getViewDirection()==AyirahStaticVars.DIRECTION_NORTH_WEST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_WEST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH_WEST))
		back[1]=getMap().getWidth()-1;
		else
		back[1]=Math.min(getMap().getWidth()-1, getPosX()+1);
		
		if (!(getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH_EAST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH || 
		getViewDirection()==AyirahStaticVars.DIRECTION_SOUTH_WEST))
		back[2]=0;
		else
		back[2]=Math.max(0, getPosY()-1);
		
		if (!(getViewDirection()==AyirahStaticVars.DIRECTION_NORTH_EAST || 
		getViewDirection()==AyirahStaticVars.DIRECTION_NORTH || 
		getViewDirection()==AyirahStaticVars.DIRECTION_NORTH_WEST))
		back[3]=getMap().getHeight()-1;
		else
		back[3]=Math.min(getMap().getHeight()-1, getPosY()+1);
		
		return back;
	}
	
	protected void removeInvisibleArea(int x, int y, int vis_type)
	{
		if (getMap().isValidCoordPair(getLayer(), x, y) && vis_type>0)
		{
			if (vis_type>0
			&&  !(y==this.getPosY() &&
			x==this.getPosX()))
			{
				if (x==this.getPosX())
				{
					// Fall: Wand nördlich vom Character
					if (this.getPosY()-y>0)
					{
						if (this.getPosY()-y==1)
						{
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_NORTH, 
							
							vis_type==1 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE,
							
							vis_type==9 ? 
							AyirahStaticVars.FRONT_FULL_VISIBLE : 
							(vis_type==1 || vis_type==3) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==5 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							vis_type==3 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE);
						}
						
						else
						{
							if (vis_type!=9)
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_NORTH,
							AyirahStaticVars.SIDE_FULL_VISIBLE,
							
							(vis_type==1 || vis_type==3) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==5 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							AyirahStaticVars.SIDE_FULL_VISIBLE);
						}
					}
					
					// Fall: Wand südlich vom Character
					else
					{
						if (getPosY()-y==-1)
						{
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_SOUTH, 
							
							vis_type==6 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE,
							
							vis_type==9 ? 
							AyirahStaticVars.FRONT_FULL_VISIBLE :
							(vis_type==4 || vis_type==6) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==5 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							vis_type==4 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE);
						}
						
						else
						{
							if (vis_type!=9)
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_SOUTH, 
							AyirahStaticVars.SIDE_FULL_VISIBLE,
							
							(vis_type==4 || vis_type==6) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==5 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							AyirahStaticVars.SIDE_FULL_VISIBLE);
						}
					}
				}
				
				else if (y==getPosY())
				{
					// Fall: Wand westlich vom Character
					if (getPosX()-x>0)
					{
						if (getPosX()-x==1)
						{
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_WEST,
							
							vis_type==3 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE,
							
							vis_type==8 ? 
							AyirahStaticVars.FRONT_FULL_VISIBLE :
							(vis_type==3 || vis_type==6) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==2 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							vis_type==6 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE);
						}
						
						else
						{
							if (vis_type!=8)
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_WEST, 
							AyirahStaticVars.SIDE_FULL_VISIBLE,
							
							(vis_type==3 || vis_type==6) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==2 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							AyirahStaticVars.SIDE_FULL_VISIBLE);
						}
					}
					
					// Fall: Wand östlich vom Character
					else
					{
						if (getPosX()-x==-1)
						{
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_EAST, 
							
							vis_type==4 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE,
							
							vis_type==8 ? 
							AyirahStaticVars.FRONT_FULL_VISIBLE :
							(vis_type==1 || vis_type==4) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==2 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							vis_type==1 ? 
							AyirahStaticVars.SIDE_FULL_VISIBLE :
							AyirahStaticVars.SIDE_TILE_INVISIBLE);
						}
							
						else
						{
							if (vis_type!=8)
							removeEvenDirectionInvisibleArea
							(getLayer(), x, y, 
							AyirahStaticVars.DIRECTION_EAST,
							AyirahStaticVars.SIDE_FULL_VISIBLE,
							
							(vis_type==1 || vis_type==4) ? 
							AyirahStaticVars.FRONT_TILE_VISIBLE :
							vis_type==2 ? 
							AyirahStaticVars.FRONT_TILE_MOST_VISIBLE : 
							AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
							
							AyirahStaticVars.SIDE_FULL_VISIBLE);
						}
					}
				}
				
				
				/* Jetzt die Diagonalen */
				if (getPosX()-x==getPosY()-y)
				{
					// Fall Diagonale NW -> SO
					if (getPosX()-x<0)
					{
						if (getPosX()-x==-1)
						{
							if (!(vis_type==1 || vis_type==6))
							{
								removeOddDirectionInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_SOUTH_EAST);
								
								if (vis_type==3 || vis_type==7 )
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
							
							else if (vis_type==1)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x, y-1, 
								AyirahStaticVars.DIRECTION_EAST,
								AyirahStaticVars.SIDE_FULL_VISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE);
							}
							
							else if (vis_type==6)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x-1, y, 
								AyirahStaticVars.DIRECTION_SOUTH,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_FULL_VISIBLE);
							}
						}
						
						else
						{
							if (!(vis_type==1 || vis_type==6))
							{
								removeOddDirectionSmallInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_SOUTH_EAST);
								
								if (vis_type==1 || vis_type==3 || vis_type==6 ||
								vis_type==7)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
							
							else if (vis_type==1)
							{
								for (int i=0; i<=Math.min(map.getWidth()-1-x, 
								map.getHeight()-1-y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x+i, y+i))
										removeVisible(getLayer(), x+i, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
									if (map.isValidCoordPair
									(getLayer(), x+i+1, y+i))
										removeVisible(getLayer(), x+i+1, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
								
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
							}
							
							else if (vis_type==6)
							{
								for (int i=0; i<=Math.min(map.getWidth()-1-x, 
								map.getHeight()-1-y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x+i, y+i))
										removeVisible(getLayer(), x+i, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
										
									if (map.isValidCoordPair
									(getLayer(), x+i, y+i+1))
										removeVisible(getLayer(), x+i, y+i+1,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
									
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
						}
					}
					
					// Fall Diagonale SO -> NW
					else // if (charpos_x-spalte)>0 danach ist verzeichtbar
					{
						if (getPosX()-x==1)
						{
							if (!(vis_type==1 || vis_type==6))
							{
								removeOddDirectionInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_NORTH_WEST);
								
								if (vis_type==4 || vis_type==7)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH+
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
								
							else if (vis_type==1)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x+1, y,
								AyirahStaticVars.DIRECTION_NORTH,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_FULL_VISIBLE);
							}
							
							else if (vis_type==6)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x, y+1,
								AyirahStaticVars.DIRECTION_WEST,
								AyirahStaticVars.SIDE_FULL_VISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE);
							}
						}
							
						else
						{
							if (!(vis_type==1 || vis_type==6))
							{
								removeOddDirectionSmallInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_NORTH_WEST);
								
								if (vis_type==4 || vis_type==7)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH+
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
							
							else if (vis_type==1)
							{
								for (int i=0; i<=Math.min(x, y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x-i, y-i))
										removeVisible(getLayer(), x-i, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
										
									if (map.isValidCoordPair
									(getLayer(), x-i, y-i-1))
										removeVisible(getLayer(), x-i, y-i-1,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
									
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
							
							else if (vis_type==6)
							{
								for (int i=0; i<=Math.min(x, y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x-i, y-i))
										removeVisible(getLayer(), x-i, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
									if (map.isValidCoordPair
									(getLayer(), x-i-1, y-i))
										removeVisible(getLayer(), x-i-1, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);			
								}
								
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
							}
						}
					}
				}
				
				// Fall: Diagonale SW -> NO
				else if (getPosX()-x==y-getPosY())
				{
					if (getPosX()-x<0)
					{
						if (getPosX()-x==-1)
						{
							if (!(vis_type==3 || vis_type==4))
							{
								removeOddDirectionInvisibleArea(getLayer(), 
								x, y, 
								AyirahStaticVars.DIRECTION_NORTH_EAST);
								
								if (vis_type==6 || vis_type==7)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH+
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
								
							else if (vis_type==3)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x-1, y,
								AyirahStaticVars.DIRECTION_NORTH,
								AyirahStaticVars.SIDE_FULL_VISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE);
							}
							
							else if (vis_type==4)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x, y+1,
								AyirahStaticVars.DIRECTION_EAST,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_FULL_VISIBLE);
							}
						}
						
						else
						{
							if (!(vis_type==3 || vis_type==4))
							{
								removeOddDirectionSmallInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_NORTH_EAST);
								
								if (vis_type==6 || vis_type==7)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH+
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==2 || vis_type==8)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
								
								if (vis_type==5 || vis_type==9)
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
							
							else if (vis_type==3)
							{
								for (int i=0; i<=Math.min
								(map.getWidth()-1-x, y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x+i, y-i))
										removeVisible(getLayer(), x+i, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
										
									if (map.isValidCoordPair
									(getLayer(), x+i, y-i-1))
										removeVisible(getLayer(), x+i, y-i-1,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
								
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_NORTH);
							}
							
							else if (vis_type==4)
							{
								for (int i=0; i<=Math.min
								(map.getWidth()-1-x, y); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x+i, y-i))
										removeVisible(getLayer(), x+i, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
									if (map.isValidCoordPair
									(getLayer(), x+i+1, y-i))
										removeVisible(getLayer(), x+i+1, y-i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_EAST);
							}
						}
					}
					
					// Fall: Diagonale NO -> SW
					else // if (charpos_x-spalte)>0 danach ist verzeichtbar
					{
						if (getPosX()-x==1)
						{
							if (!(vis_type==3 || vis_type==4))
							{
								removeOddDirectionInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_SOUTH_WEST);
								
								if (vis_type==1 || vis_type==7)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
									
								if (vis_type==2 || vis_type==8)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
								
								if (vis_type==5 || vis_type==9)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
							
							else if (vis_type==3)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x, y-1,
								AyirahStaticVars.DIRECTION_WEST,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_FULL_VISIBLE);
							}
							
							else if (vis_type==4)
							{
								removeEvenDirectionInvisibleArea
								(getLayer(), x+1, y,
								AyirahStaticVars.DIRECTION_SOUTH,
								AyirahStaticVars.SIDE_FULL_VISIBLE,
								AyirahStaticVars.FRONT_FULL_VISIBLE,
								AyirahStaticVars.SIDE_TILE_HALFVISIBLE);
							}
						}
						
						else
						{
							if (!(vis_type==3 || vis_type==4))
							{
								removeOddDirectionSmallInvisibleArea
								(getLayer(), x, y, 
								AyirahStaticVars.DIRECTION_SOUTH_WEST);
								
								if (vis_type==1 || vis_type==7)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
									
								if (vis_type==2 || vis_type==8)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_WEST);
									
								if (vis_type==5 || vis_type==9)
									removeVisible(getLayer(), x, y, 
									AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
							
							else if (vis_type==3)
							{
								for (int i=0; i<=Math.min(x, 
								(map.getHeight()-1-y)); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x-i, y+i))
										removeVisible(
										getLayer(), x-i, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
									if (map.isValidCoordPair
									(getLayer(), x-i-1, y+i))
										removeVisible(
										getLayer(), x-i-1, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
								}
								
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_WEST);
							}
							
							else if (vis_type==4)
							{
								for (int i=0; i<=Math.min(x, 
								(map.getHeight()-1-y)); i++)
								{
									if (!(i==0) && map.isValidCoordPair
									(getLayer(), x-i, y+i))
										removeVisible(
										getLayer(), x-i, y+i,
										AyirahStaticVars.VISIBLE_KNOWN_SOUTH+
										AyirahStaticVars.VISIBLE_KNOWN_EAST);
									if (map.isValidCoordPair
									(getLayer(), x-i, y+i+1))
										removeVisible(
										getLayer(), x-i, y+i+1,
										AyirahStaticVars.VISIBLE_KNOWN_NORTH+
										AyirahStaticVars.VISIBLE_KNOWN_WEST);
								}
								
								removeVisible(getLayer(), x, y, 
								AyirahStaticVars.VISIBLE_KNOWN_SOUTH);
							}
						}
					}
				}
				
				// jetzt die restlichen 8 Fälle
				if (getPosX()>x && getPosY()>y)
				{
					// Fall: Wand NNW
					if (getPosY()-getPosX()>y-x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x+
						(vis_type==1 ? 1 : 0), y, 
						AyirahStaticVars.DIRECTION_NORTH,
						
						vis_type==1 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE,
						
						vis_type==1 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==5 || vis_type==9) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==3 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,

						AyirahStaticVars.SIDE_FULL_VISIBLE);
					}
					
					// Fall: Wand NWW
					else if (getPosY()-getPosX()<y-x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x, y+(vis_type==6 ? 1 : 0), 
						AyirahStaticVars.DIRECTION_WEST,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE,
						
						vis_type==6 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==2 || vis_type==8) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==3 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						vis_type==6 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE);
					}
				}
				
				else if (getPosX()>x && getPosY()<y)
				{
					// Fall: Wand SWW
					if (getPosY()+getPosX()>y+x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x, y+(vis_type==3 ? -1 : 0), 
						AyirahStaticVars.DIRECTION_WEST,
						
						vis_type==3 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE,
						
						vis_type==3 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==2 || vis_type==8) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==6 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE);
					}
					
					// Fall: Wand SSW
					else if (getPosY()+getPosX()<y+x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x+(vis_type==4 ? 1 : 0), y, 
						AyirahStaticVars.DIRECTION_SOUTH,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE,
						
						vis_type==4 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==5 || vis_type==9) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==6 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						vis_type==4 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE);
					}
				}
				
				else if (getPosX()<x && getPosY()<y)
				{
					// Fall: SSO
					if (getPosY()-getPosX()<y-x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x+(vis_type==6 ? -1 : 0), y, 
						AyirahStaticVars.DIRECTION_SOUTH,
						
						vis_type==6 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE,
						
						vis_type==6 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==5 || vis_type==9) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==4 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE);
					}
					
					// Fall: SOO
					else if (getPosY()-getPosX()>y-x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x, y+(vis_type==1 ? -1 : 0), 
						AyirahStaticVars.DIRECTION_EAST,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE,
						
						vis_type==1 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==2 || vis_type==8) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==4 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						vis_type==1 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE);
					}
				}
				
				else if (getPosX()<x && getPosY()>y)
				{
					// Fall: Wand NOO
					if (getPosY()+getPosX()<y+x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x, y+(vis_type==4 ? 1 : 0), 
						AyirahStaticVars.DIRECTION_EAST,
						
						vis_type==4 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE,
						
						vis_type==4 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE : 
						(vis_type==2 || vis_type==8) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==1 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						AyirahStaticVars.SIDE_FULL_VISIBLE);
					}
					
					// Fall: Wand NNO
					else if (getPosY()+getPosX()>y+x)
					{
						removeEvenDirectionInvisibleArea
						(getLayer(), x+(vis_type==3 ? -1 : 0), y, 
						AyirahStaticVars.DIRECTION_NORTH, 
						
						AyirahStaticVars.SIDE_FULL_VISIBLE,
						
						vis_type==3 ? 
						AyirahStaticVars.FRONT_FULL_VISIBLE :
						(vis_type==5 || vis_type==9) ? 
						AyirahStaticVars.FRONT_TILE_MOST_VISIBLE :
						vis_type==1 ? 
						AyirahStaticVars.FRONT_TILE_VISIBLE : 
						AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE,
						
						vis_type==3 ? 
						AyirahStaticVars.SIDE_TILE_VISIBLE : 
						AyirahStaticVars.SIDE_TILE_INVISIBLE);
					}
				}
			}
			
			// Dieser Fall macht bis jetzt nur bei Türen Sinn!
			else if (vis_type>0 &&
			y==this.getPosY() &&
			x==this.getPosX())
			{
				if (vis_type==8) // senkrechte Tür
				{
					removeEvenDirectionInvisibleArea(getLayer(), x, y-1, 
					AyirahStaticVars.DIRECTION_NORTH,
					AyirahStaticVars.SIDE_TILE_INVISIBLE,
					AyirahStaticVars.FRONT_TILE_INVISIBLE,
					AyirahStaticVars.SIDE_TILE_INVISIBLE);
					
					removeEvenDirectionInvisibleArea(getLayer(), x, y+1, 
					AyirahStaticVars.DIRECTION_SOUTH,
					AyirahStaticVars.SIDE_TILE_INVISIBLE,
					AyirahStaticVars.FRONT_TILE_INVISIBLE,
					AyirahStaticVars.SIDE_TILE_INVISIBLE);
				}
				
				else if (vis_type==9) // waagrechte Tür
				{
					removeEvenDirectionInvisibleArea(getLayer(), x-1, y, 
					AyirahStaticVars.DIRECTION_WEST,
					AyirahStaticVars.SIDE_TILE_INVISIBLE,
					AyirahStaticVars.FRONT_TILE_INVISIBLE,
					AyirahStaticVars.SIDE_TILE_INVISIBLE);
					
					removeEvenDirectionInvisibleArea(getLayer(), x+1, y, 
					AyirahStaticVars.DIRECTION_EAST,
					AyirahStaticVars.SIDE_TILE_INVISIBLE,
					AyirahStaticVars.FRONT_TILE_INVISIBLE,
					AyirahStaticVars.SIDE_TILE_INVISIBLE);
				}
			}
		}
	}
	
	protected void removeInvisibleTiles()
	{
		int[] minmax_area=this.getMinMaxArea();
		
		for (int zeile=minmax_area[2]; zeile<=minmax_area[3]; zeile++)
			for (int spalte=minmax_area[0]; spalte<=minmax_area[1]; spalte++)
			{
				this.removeInvisibleArea(spalte, zeile, 
				map.getVisibilityType(getLayer(), zeile, spalte));
			}
	}
	
	protected void removeEvenDirectionInvisibleArea(
	int l, int x, int y, int direction,
	int left_tile_type, int front_tile_type, int right_tile_type)
	{
		boolean visible_left;
		boolean visible_front;
		boolean visible_right;
		
		int max;
		int line_type=-1; // waagrecht=2, senkrecht=0
		
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
			
		if (!map.isValidCoordPair(l, x, y))
			return;
		
		int left_invisible=-1;
		int front_invisible=-1;
		int right_invisible=-1;
		
		switch (left_tile_type)
		{
			case AyirahStaticVars.SIDE_FULL_VISIBLE:
				visible_left=true;
				break;
			case AyirahStaticVars.SIDE_TILE_INVISIBLE:
				visible_left=false;
				left_invisible=(1<<((direction%8)/2)) | (1<<(((direction+2)%8)/2));
				break;
			case AyirahStaticVars.SIDE_TILE_HALFVISIBLE:
				visible_left=false;
				left_invisible=(1<<(direction/2));
				break;
			case AyirahStaticVars.SIDE_TILE_VISIBLE:
				visible_left=false;
				left_invisible=AyirahStaticVars.VISIBLE_KNOWN_NONE;
				break;
			default:
				return;
		}
		
		switch (front_tile_type)
		{
			case AyirahStaticVars.SIDE_FULL_VISIBLE:
				visible_front=true;
				break;
			case AyirahStaticVars.FRONT_TILE_INVISIBLE:
				visible_front=false;
				front_invisible=AyirahStaticVars.VISIBLE_KNOWN_ALL;
				break;
			case AyirahStaticVars.FRONT_TILE_MOST_INVISIBLE:
				visible_front=false;
				front_invisible=AyirahStaticVars.VISIBLE_KNOWN_ALL-(1<<(((direction+4)%8)/2));
				break;
			case AyirahStaticVars.FRONT_TILE_MOST_VISIBLE:
				visible_front=false;
				front_invisible=(1<<(direction/2));
				break;
			case AyirahStaticVars.FRONT_TILE_VISIBLE:
				visible_front=false;
				front_invisible=AyirahStaticVars.VISIBLE_KNOWN_NONE;
				break;
			default:
				return;
		}
		
		switch (right_tile_type)
		{
			case AyirahStaticVars.SIDE_FULL_VISIBLE:
				visible_right=true;
				break;
			case AyirahStaticVars.SIDE_TILE_INVISIBLE:
				visible_right=false;
				right_invisible=(1<<((direction%8)/2)) | (1<<(((direction+6)%8)/2));
				break;
			case AyirahStaticVars.SIDE_TILE_HALFVISIBLE:
				visible_right=false;
				right_invisible=(1<<(direction/2));
				break;
			case AyirahStaticVars.SIDE_TILE_VISIBLE:
				visible_right=false;
				right_invisible=AyirahStaticVars.VISIBLE_KNOWN_NONE;
				break;
			default:
				return;
		}
		
		line_type=direction%4;
		
		int dir_left=(direction+6)%8;
		int dir_right=(direction+2)%8;

		for (int i=0; i<=max; i++)
			for (int j=(visible_right?0:(-i-1)); 
			j<=(visible_left?0:(i+1)); j++)
			{
				int actual_x=x+i*AyirahStaticVars.direction_modifier[direction][0]+
				j*AyirahStaticVars.direction_modifier[dir_left][0];
				
				int actual_y=y+i*AyirahStaticVars.direction_modifier[direction][1]+
				j*AyirahStaticVars.direction_modifier[dir_left][1];
				
				if (map.isValidCoordPair(l, actual_x, actual_y))
				{
						// rechts
						if (j==(-i-1))
						{
							if (i==0)
							{
								removeVisible(l, actual_x, actual_y,
								right_invisible);
							}
							
							else
								removeVisible(l, actual_x, actual_y,
								(1<<((direction%8)/2)) | 
								(1<<(((direction+6)%8)/2)));
						}
						
						// links
						else if (j==(i+1))
						{
							if (i==0)
							{
								removeVisible(l, actual_x, actual_y,
								left_invisible);
							}
							
							else
								removeVisible(l, actual_x, actual_y,
								(1<<((direction%8)/2)) | 
								(1<<(((direction+2)%8)/2)));
						}
						
						else if (!visible_front || 
						(!(line_type==0 && x==actual_x) && 
						!(line_type==2 && y==actual_y)))
						{
							if ((j==0) && (i==0))
								removeVisible(l, actual_x, actual_y,
								front_invisible);
							
							else
								removeVisible(l, actual_x, actual_y,
								AyirahStaticVars.VISIBLE_KNOWN_ALL);
						}
				}
			}
	}
	
	protected void removeOddDirectionInvisibleArea(int l, int x, int y, int direction)
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
	
	protected void removeOddDirectionSmallInvisibleArea(int l, int x, int y, int direction)
	{
		if (!((direction==AyirahStaticVars.DIRECTION_NORTH_EAST) || 
		(direction==AyirahStaticVars.DIRECTION_SOUTH_EAST) ||
		(direction==AyirahStaticVars.DIRECTION_SOUTH_WEST) ||
		(direction==AyirahStaticVars.DIRECTION_NORTH_WEST)))
			return;
		
		if (!map.isValidCoordPair(l, x, y))
			return;
		
		int max=0;
		
		if (direction==AyirahStaticVars.DIRECTION_NORTH_EAST)
			max=Math.min(map.getWidth()-1-x, y);
		
		else if (direction==AyirahStaticVars.DIRECTION_SOUTH_EAST)
			max=Math.min(map.getWidth()-1-x, map.getHeight()-1-y);
			
		else if (direction==AyirahStaticVars.DIRECTION_SOUTH_WEST)
			max=Math.min(x, map.getHeight()-1-y);
			
		else if (direction==AyirahStaticVars.DIRECTION_NORTH_WEST)
			max=Math.min(x, y);
		
		else
			return;
		
		for (int i=0; i<=max; i++)
		{
			if (!(i==0) && map.isValidCoordPair
			(l, x+i*AyirahStaticVars.direction_modifier[direction][0],
			y+i*AyirahStaticVars.direction_modifier[direction][1]))
				removeVisible(l, 
				x+i*AyirahStaticVars.direction_modifier[direction][0],
				y+i*AyirahStaticVars.direction_modifier[direction][1],
				AyirahStaticVars.VISIBLE_KNOWN_ALL);
			
			// rechts
			if (map.isValidCoordPair(l, 
			x+i*AyirahStaticVars.direction_modifier[direction][0]
			+AyirahStaticVars.direction_modifier[(direction+1)%8][0],
			y+i*AyirahStaticVars.direction_modifier[direction][1]
			+AyirahStaticVars.direction_modifier[(direction+1)%8][1]))
				removeVisible(l, 
				x+i*AyirahStaticVars.direction_modifier[direction][0]
				+AyirahStaticVars.direction_modifier[(direction+1)%8][0],
				y+i*AyirahStaticVars.direction_modifier[direction][1]
				+AyirahStaticVars.direction_modifier[(direction+1)%8][1],
				(1<<(((direction+7))%8)/2) | (1<<(((direction+5))%8)/2));
			
			// links
			if (map.isValidCoordPair
			(l, x+i*AyirahStaticVars.direction_modifier[direction][0]
			+AyirahStaticVars.direction_modifier[(direction+7)%8][0],
			y+i*AyirahStaticVars.direction_modifier[direction][1]
			+AyirahStaticVars.direction_modifier[(direction+7)%8][1]))
				removeVisible(l, 
				x+i*AyirahStaticVars.direction_modifier[direction][0]
				+AyirahStaticVars.direction_modifier[(direction+7)%8][0],
				y+i*AyirahStaticVars.direction_modifier[direction][1]
				+AyirahStaticVars.direction_modifier[(direction+7)%8][1],
				(1<<(((direction+1))%8)/2) | (1<<(((direction+3))%8)/2));
			}
		
		for (int i=0; i<=max; i++)
		{
			
			
			
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
				setVisible(getLayer(), j, i, 
				AyirahStaticVars.VISIBLE_KNOWN_NONE, 0);
			}
		
		addVisible(getLayer(), getPosX(), getPosY(), 
		AyirahStaticVars.VISIBLE_KNOWN_ALL);
		
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
							(1<<((direction%8)/2)) | (1<<(((direction+6)%8)/2))); 
						}
						
						else if (j==(i+2))
						{
							addVisible(getLayer(), 
							x+i*AyirahStaticVars.direction_modifier[direction][0]+
							j*AyirahStaticVars.direction_modifier[dir_left][0], 
							y+i*AyirahStaticVars.direction_modifier[direction][1]+
							j*AyirahStaticVars.direction_modifier[dir_left][1],
							(1<<((direction%8)/2)) | (1<<(((direction+2)%8)/2)));
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
			
					if ((row==getPosY()-AyirahStaticVars.direction_modifier
					[getViewDirection()][1] && col==getPosX()) || 
					(row==getPosY() && (col==getPosX()-AyirahStaticVars.
					direction_modifier[getViewDirection()][0])))
					{
						removeVisible(getLayer(), col, row, 
						AyirahStaticVars.VISIBLE_KNOWN_ALL
						-((1<<(((getViewDirection()+1)%8)/2))
						| (1<<(((getViewDirection()+7)%8)/2))));
					}
				}
		}
		
		removeInvisibleTiles();
		makeVisibleKnown();
	}
}
