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
import java.awt.event.*;

public class AyirahComponent extends Canvas
{
	private AyirahComponentKeyListener ackl;
	private MediaTracker m_tiles;
	private MediaTracker m_boden;
	private Image[] tiles; // Alle sonstigen Tiles außer Boden
	private Image sidebar;
	private Image[][] character;
	
	private GameMap map;
	
	// obere Ecke des angezeigten Bereichs der Map
	private int top_corner_x=0;
	private int top_corner_y=0;
	
	// Ist immer <=0
	private int delta_x, delta_y;
	
	//	48x48 sieht am besten aus; zumindest bei den
	 // derzeitigen Grafiken
	private static final int tile_width=48;
	private static final int tile_height=48;
	private static final char[] sprite_name={'a', 'b', 'c', 'd'};
	
	private Image dbImage;
	private Graphics dbGraphics;
	
	AyirahComponent()
	{
		map=new GameMap();
		m_tiles=new MediaTracker(this);
		m_boden=new MediaTracker(this);
		tiles=new Image[17];
		character=new Image[8][4];
		tiles[0]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"leer.png");
		m_tiles.addImage(tiles[0], 0);
		
		tiles[1]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"boden.png");
		m_tiles.addImage(tiles[0], 1);
		tiles[2]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"wand.png");
		m_tiles.addImage(tiles[2], 2);
		
		for (int i=0; i<=3; i++)
		{
			tiles[3+i]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"wand" +(i*2+1)+".gif");
			m_tiles.addImage(tiles[3+i], 3+i);
		}
		
		tiles[7]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"tür_senkrecht_offen.png");
		m_tiles.addImage(tiles[7], 7);
		
		tiles[8]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"tür_waagrecht_offen.png");
		m_tiles.addImage(tiles[8], 8);
		
		tiles[9]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"tür_senkrecht_geschlossen.png");
		m_tiles.addImage(tiles[9], 9);
		
		tiles[10]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"tür_waagrecht_geschlossen.png");
		m_tiles.addImage(tiles[10], 10);
		
		tiles[11]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"treppe_runter.png");
		m_tiles.addImage(tiles[11], 11);
		
		tiles[12]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"treppe_hoch.png");
		m_tiles.addImage(tiles[12], 12);
		
		tiles[13]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"treppe_hoch_runter.png");
		m_tiles.addImage(tiles[13], 13);
		
		tiles[14]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"kiste.png");
		m_tiles.addImage(tiles[14], 14);
		
		tiles[15]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"kiste_offen.png");
		m_tiles.addImage(tiles[15], 15);
		
		sidebar=getToolkit().getImage("sidebar.png");
		m_tiles.addImage(sidebar, 3);
		
		for (int i=0; i<8; i++)
			for (int j=0; j<4; j++)
			{
				character[i][j]=
				getToolkit().createImage(AyirahStaticVars.avtr_prefix
				+sprite_name[j]+i+".gif");
				m_tiles.addImage(character[i][j], 4+4*i+j);
			}
		
		ackl=new AyirahComponentKeyListener(this);
		this.addKeyListener(ackl);
		
		try {
			m_tiles.waitForAll();
		} catch (InterruptedException e) { }
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(1024,768);
	}
	
	public Dimension getMinimumSize()
	{
		return new Dimension(1024,768);
	}
	
	public KeyListener getKeyListener()
	{
		return ackl;
	}
	
	public GameMap getGameMap()
	{
		return map;
	}
	
	public void scrollMap(int direction)
	{
		if (direction<0 || direction>=8)
			throw new IllegalTurnException("Scrollen unmöglich");
		if (
		/*(delta_x-AyirahStaticVars.direction_modifier[direction][0]<=0) &&
		(delta_x-AyirahStaticVars.direction_modifier[direction][0]>-tile_width)
		&& (delta_y-AyirahStaticVars.direction_modifier[direction][1]<=0)
		&& (delta_y-AyirahStaticVars.direction_modifier[direction][1]>-tile_height)*/
		this.top_corner_x+AyirahStaticVars.direction_modifier[direction][0]>=0 &&
		this.top_corner_x+
		AyirahStaticVars.direction_modifier[direction][0]<map.getWidth()-15 &&
		this.top_corner_y+AyirahStaticVars.direction_modifier[direction][1]>=0 &&
		this.top_corner_y+
		AyirahStaticVars.direction_modifier[direction][1]<map.getHeight()-15)
		{
			/*delta_x-=AyirahStaticVars.direction_modifier[direction][0];
			delta_y-=AyirahStaticVars.direction_modifier[direction][1];*/
			this.top_corner_x+=
			+AyirahStaticVars.direction_modifier[direction][0];
			this.top_corner_y+=
			+AyirahStaticVars.direction_modifier[direction][1];
		}
		/*else if (
		((delta_x-AyirahStaticVars.direction_modifier[direction][0]>0) && (this.top_corner_x-
		AyirahStaticVars.direction_modifier[direction][0]<map.getWidth()-15)) ||
		
		((delta_x-AyirahStaticVars.direction_modifier[direction][0]<=-tile_width) &&
		this.top_corner_x-AyirahStaticVars.direction_modifier[direction][0]>=0) ||
		
		((delta_y-AyirahStaticVars.direction_modifier[direction][1]>0) &&
		(this.top_corner_y-AyirahStaticVars.direction_modifier[direction][1]<map.getHeight()-15))
		
		|| ((delta_y-AyirahStaticVars.direction_modifier[direction][1]>-tile_height) &&
		this.top_corner_y-AyirahStaticVars.direction_modifier[direction][1]>=0))
		{
			delta_x-=AyirahStaticVars.direction_modifier[direction][0];
			delta_y-=AyirahStaticVars.direction_modifier[direction][1];
			
			if (delta_x>0)
			{
				top_corner_x--;
				delta_x-=tile_width;
			}
			
			else if (delta_x<=-tile_width)
			{
				top_corner_x++;
				delta_x+=tile_width;
			}
			
			if (delta_y>0)
			{
				top_corner_y--;
				delta_y-=tile_height;
			}
			
			else if (delta_y<=-tile_height)
			{
				top_corner_y++;
				delta_y+=tile_height;
			}
		}*/
		
		else 
			throw new IllegalTurnException("Scrollen unmöglich");
	}
	
	public void update (Graphics g)
	{
		if (dbImage == null) {
			dbImage = createImage(
				this.getSize().width,
				this.getSize().height
			);
			dbGraphics = dbImage.getGraphics();
		}
		//Hintergrund löschen
		dbGraphics.setColor(Color.BLACK);
		dbGraphics.fillRect(
		0,
		0,
		this.getSize().width,
		this.getSize().height
		);

		//Vordergrund zeichnen
		dbGraphics.setColor(getForeground());
		paint(dbGraphics);
		//Offscreen anzeigen
		g.drawImage(dbImage,0,0,this);
	}
	
	public void fillRect(Graphics g, int x, int y, int width, int height)
	{
		g.fillRect(x+delta_x, y+delta_y, width,height);
	}
	
	public void fillPolygon(Graphics g, int[] argx, int[] argy, int length)
	{
		for (int i=0; i<argx.length; i++)
		{
			argx[i]+=delta_x;
		}
		
		for (int i=0; i<argy.length; i++)
		{
			argy[i]+=delta_y;
		}
		
		g.fillPolygon(argx, argy, length);
	}
	
	public void paint (Graphics g)
	{
		for (int zeile=0; zeile<16; zeile++)
			for (int spalte=0; spalte<16; spalte++)
			{
				char actual_tile=map.getCharacterKnownTile
				(map.getCharacter().getLayer(), zeile+top_corner_y, spalte+top_corner_x);
				int array_index=0;

				if (actual_tile==' ')
					array_index=0;
				else if (actual_tile=='.'|| actual_tile=='1' || actual_tile=='2' ||
				actual_tile=='3' || actual_tile=='4')
					array_index=1;
				else if (actual_tile=='#')
					array_index=2;
				else if (actual_tile=='_')
					array_index=7;
				else if (actual_tile=='i')
					array_index=8;
				else if (actual_tile=='-')
					array_index=9;
				else if (actual_tile=='I')
					array_index=10;
				else if (actual_tile=='>')
					array_index=11;
				else if (actual_tile=='<')
					array_index=12;
				else if (actual_tile=='|')
					array_index=13;
				else if (actual_tile=='X')
					array_index=14;
				else if (actual_tile=='x')
					array_index=15;

				g.drawImage(tiles[array_index], 
				tile_width*spalte+delta_x, tile_height*zeile+delta_y, this);
				
				if (actual_tile=='1' || actual_tile=='2' ||
				actual_tile=='3' || actual_tile=='4')
				{
					try
					{
					array_index=new Integer(new Character(actual_tile).toString()).intValue()+2;
					
					g.drawImage(tiles[array_index], 
					tile_width*spalte+delta_x, tile_height*zeile+delta_y, this);
					}
					catch (Exception e) {}
				}
				
				int visible=map.getCharacter().getVisible
				(map.getCharacter().getLayer(), zeile+top_corner_y, spalte+top_corner_x)				.getMainType();
				
				if (visible==AyirahStaticVars.VISIBLE_NONE)
				{
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillRect(g,tile_width*spalte, tile_height*zeile, 
					tile_width, tile_height);
				}
				else if (visible==AyirahStaticVars.VISIBLE_SOUTH_WEST)
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*(spalte+1)};
					int[] ary={tile_height*zeile, tile_height*zeile, tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillPolygon(g,arx, ary, arx.length);
				}
				else if (visible==AyirahStaticVars.VISIBLE_NORTH_EAST)
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*spalte};
					int[] ary={tile_height*zeile, tile_height*(zeile+1), tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillPolygon(g,arx, ary, arx.length);
				}
				else if (visible==AyirahStaticVars.VISIBLE_NORTH_WEST)
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*(spalte+1)};
					int[] ary={tile_height*(zeile+1), tile_height*(zeile+1), tile_height*zeile};
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				else if (visible==AyirahStaticVars.VISIBLE_SOUTH_EAST)
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*spalte};
					int[] ary={tile_height*zeile, tile_height*zeile, tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				else if (visible!=AyirahStaticVars.VISIBLE_ALL)
					System.out.println("Visible:"+visible+" wird nicht unterstützt");
					
					
				
				int known=map.getCharacter().getKnown
				(map.getCharacter().getLayer(), zeile+top_corner_y, spalte+top_corner_x)				.getMainType();
				
				if (known==AyirahStaticVars.KNOWN_NONE)
				{
					g.setColor(AyirahStaticVars.color_invisible);
					this.fillRect(g,tile_width*spalte, tile_height*zeile, 
					tile_width, tile_height);
				}
				else if (known==(AyirahStaticVars.KNOWN_SOUTH_WEST))
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*(spalte+1)};
					int[] ary={tile_height*zeile, tile_height*zeile, tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_unknown);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				else if (known==(AyirahStaticVars.KNOWN_NORTH_EAST))
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*spalte};
					int[] ary={tile_height*zeile, tile_height*(zeile+1), tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_unknown);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				else if (known==(AyirahStaticVars.KNOWN_NORTH_WEST))
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*(spalte+1)};
					int[] ary={tile_height*(zeile+1), tile_height*(zeile+1), tile_height*zeile};
					g.setColor(AyirahStaticVars.color_unknown);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				else if (known==(AyirahStaticVars.KNOWN_SOUTH_EAST))
				{
					int[] arx={tile_width*spalte, tile_width*(spalte+1), tile_width*spalte};
					int[] ary={tile_height*zeile, tile_height*zeile, tile_height*(zeile+1)};
					g.setColor(AyirahStaticVars.color_unknown);
					this.fillPolygon(g, arx, ary, arx.length);
				}
				
				else if (known!=AyirahStaticVars.KNOWN_ALL)
					System.out.println("Known:"+known+" wird nicht unterstützt");
				
			}
			
		int character_width=30;
		int character_height=48;
		
		g.drawImage(
		character[map.getCharacter().getViewDirection()][0], 
		tile_width*(map.getCharacter().getPosX()-top_corner_x)
		+(tile_width-character_width)/2+delta_x, 
		tile_height*(map.getCharacter().getPosY()-top_corner_y)
		+(tile_height-character_height)/2+delta_y, character_width, character_height, this);
		
		g.drawImage(sidebar, 768, 0, this);
	}
}