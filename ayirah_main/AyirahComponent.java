/* 
 * 
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
import java.awt.event.*;
import java.awt.image.*;

public class AyirahComponent extends Canvas
{
	private AyirahComponentKeyListener ackl;
	private MediaTracker m_tiles;
	
	// tiles[tile_number][direction][visible]
	private Image[][][] tiles; // Alle Tiles
	private Image[][][] items;
	private Image[][] character;
	
	private GameMap map;
	
	// obere Ecke des angezeigten Bereichs der Map
	private int top_corner_x=0;
	private int top_corner_y=0;
	
	private int max_top_corner_x, max_top_corner_y;	
	// Ist immer <=0
	private int delta_x, delta_y;
	private static final int tile_width=48;
	private static final int tile_height=48;
	private static final char[] sprite_name={'a', 'b', 'c', 'd'};
	
	private Image dbImage;
	private Graphics dbGraphics;
	
	private final int[] im_delta_x={
		0, tile_width/2, 0, 0
	};
	private final int[] im_delta_y={
		0, 0, tile_height/2, 0
	};
	
	AyirahComponent()
	{
		map=new GameMap();
		m_tiles=new MediaTracker(this);
		tiles=new Image[11][4][2];
		character=new Image[8][4];
		items=new Image[2][4][2];
		
		Image[] prepareImage=new Image[21];
		Image[] prepareItems=new Image[4];
		
		max_top_corner_x=map.getWidth()-16;
		max_top_corner_y=map.getHeight()-16;
		
		prepareImage[0]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"unknown1.gif");
		m_tiles.addImage(prepareImage[0], 0);
		
		prepareImage[1]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"boden1_invisible.gif");
		m_tiles.addImage(prepareImage[1], 0);
		
		prepareImage[2]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"boden1_visible.gif");
		m_tiles.addImage(prepareImage[2], 0);
		
		prepareImage[3]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"wand1_invisible.gif");
		m_tiles.addImage(prepareImage[3], 0);
		
		prepareImage[4]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"wand1_visible.gif");
		m_tiles.addImage(prepareImage[4], 0);
		
		prepareImage[5]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"placeholder1_invisible.gif");
		m_tiles.addImage(prepareImage[5], 0);
		
		prepareImage[6]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"placeholder1_visible.gif");
		m_tiles.addImage(prepareImage[6], 0);
		
		prepareImage[7]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_waagrecht_offen_invisible.gif");
		m_tiles.addImage(prepareImage[7], 0);
		
		prepareImage[8]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_waagrecht_offen_visible.gif");
		m_tiles.addImage(prepareImage[8], 0);
		
		prepareImage[9]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_senkrecht_offen_invisible.gif");
		m_tiles.addImage(prepareImage[9], 0);
		
		prepareImage[10]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_senkrecht_offen_visible.gif");
		m_tiles.addImage(prepareImage[10], 0);
		
		prepareImage[15]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_invisible.gif");
		m_tiles.addImage(prepareImage[15], 0);
		
		prepareImage[16]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_visible.gif");
		m_tiles.addImage(prepareImage[15], 0);
		
		prepareImage[17]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_runter_invisible.gif");
		m_tiles.addImage(prepareImage[17], 0);
		
		prepareImage[18]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_runter_visible.gif");
		m_tiles.addImage(prepareImage[18], 0);
		
		prepareImage[19]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_runter_invisible.gif");
		m_tiles.addImage(prepareImage[19], 0);
		
		prepareImage[20]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_runter_visible.gif");
		m_tiles.addImage(prepareImage[20], 0);
		
		// -------jetzt Items--------------
		
		prepareItems[0]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_open_invisible.gif");
		m_tiles.addImage(prepareItems[0], 0);
		
		prepareItems[1]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_open_visible.gif");
		m_tiles.addImage(prepareItems[1], 0);
		
		prepareItems[2]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_closed_invisible.gif");
		m_tiles.addImage(prepareItems[2], 0);
		
		prepareItems[3]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_closed_visible.gif");
		m_tiles.addImage(prepareItems[3], 0);

		try {
			m_tiles.waitForAll();
		} catch (InterruptedException e) { }
		
		// -------Bilder vorbereiten----------
		
		ImageFilter filter_north=new CropImageFilter(0,0, 48, 24);
		ImageFilter filter_west=new CropImageFilter(48, 0, 24, 48);
		ImageFilter filter_south=new CropImageFilter(0, 24, 48, 24);
		ImageFilter filter_east=new CropImageFilter(72, 0, 24, 48);
		
		ImageProducer collectionProducer=prepareImage[0].getSource();
		tiles[0][0][0]=createImage(new FilteredImageSource(
		collectionProducer,filter_north));
		m_tiles.addImage(tiles[0][0][0], 1);
		tiles[0][1][0]=createImage(new FilteredImageSource(
		collectionProducer,filter_east));
		m_tiles.addImage(tiles[0][1][0], 1);
		tiles[0][2][0]=createImage(new FilteredImageSource(
		collectionProducer,filter_south));
		m_tiles.addImage(tiles[0][2][0], 1);
		tiles[0][3][0]=createImage(new FilteredImageSource(
		collectionProducer,filter_west));
		m_tiles.addImage(tiles[0][3][0], 1);
		
		try {
			m_tiles.waitForAll();
		} catch (InterruptedException e) { }
		
		for (int i=1; i<11; i++)
		{
			for (int j=0; j<2; j++)
			{
				if (prepareImage[i*2+j-1]!=null)
				{
					collectionProducer=prepareImage[i*2+j-1].getSource();
					
					tiles[i][0][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_north));
					m_tiles.addImage(tiles[i][0][j], 2);
					
					tiles[i][1][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_east));
					m_tiles.addImage(tiles[i][1][j], 2);
					
					tiles[i][2][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_south));
					m_tiles.addImage(tiles[i][2][j], 2);
					
					tiles[i][3][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_west));
					m_tiles.addImage(tiles[i][3][j], 2);
				}
			}
		}
		
		for (int i=0; i<2; i++)
		{
			for (int j=0; j<2; j++)
			{
				collectionProducer=prepareItems[i*2+j].getSource();
				
				items[i][0][j]=createImage(new FilteredImageSource(
				collectionProducer,filter_north));
				m_tiles.addImage(items[i][0][j], 3);
				
				items[i][1][j]=createImage(new FilteredImageSource(
				collectionProducer,filter_east));
				m_tiles.addImage(items[i][1][j], 3);
				
				items[i][2][j]=createImage(new FilteredImageSource(
				collectionProducer,filter_south));
				m_tiles.addImage(items[i][2][j], 3);
				
				items[i][3][j]=createImage(new FilteredImageSource(
				collectionProducer,filter_west));
				m_tiles.addImage(items[i][3][j], 3);
			}
		}
		
		for (int i=0; i<8; i++)
			for (int j=0; j<4; j++)
			{
				character[i][j]=
				getToolkit().createImage(AyirahStaticVars.avtr_prefix
				+sprite_name[j]+i+".gif");
				m_tiles.addImage(character[i][j], 4);
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
			throw new IllegalTurnException("Falscher Parameter");
		
		if (direction%2!=0)
		{
			scrollMap((direction+7)%8);
			scrollMap((direction+1)%8);
		}
		
		if (direction==AyirahStaticVars.DIRECTION_SOUTH)
		{
			delta_y--;
			if (delta_y<=-tile_height)
			{
				delta_y=0;
				top_corner_y++;
			}
			
			if (top_corner_y>=max_top_corner_y)
			{
				top_corner_y=max_top_corner_y;
				delta_y=0;
			}
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_NORTH)
		{
			delta_y++;
			if (delta_y>0)
			{
				delta_y=delta_y-tile_height;
				top_corner_y--;
			}
			
			if (top_corner_y<0)
			{
				top_corner_y=0;
				delta_y=0;
			}
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_EAST)
		{
			delta_x--;
			if (delta_x<=-tile_width)
			{
				delta_x=0;
				top_corner_x++;
			}
		
			if (top_corner_x>=max_top_corner_x)
			{
				top_corner_x=max_top_corner_x;
				delta_x=0;
			}
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_WEST)
		{
			delta_x++;
			if (delta_x>0)
			{
				delta_x=delta_x-tile_width;
				top_corner_x--;
			}
		
			if (top_corner_x<0)
			{
				top_corner_x=0;
				delta_x=0;
			}
		}
	}
	
	public void update (Graphics g)
	{
		if (dbImage == null) {
			/* Diese Zeile verursacht den Fehler bei der Soundwiedergabe
			 * (seltsamerweise st�rt sie in der alten Version nicht)
			 */
			dbImage = createImage(this.getSize().width,this.getSize().height);
			
			dbGraphics = dbImage.getGraphics();
		}
		
		//Hintergrund l�schen
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
		g.setColor(Color.BLACK);
		g.fillRect(
		0,0,this.getSize().width,this.getSize().height);
		
		for (int zeile=0; zeile<17; zeile++)
			for (int spalte=0; spalte<22; spalte++)
			{
				GameTile gt=map.getCreatureKnownTile
				(map.getCharacter(), map.getCharacter().getLayer(), 
				zeile+top_corner_y, spalte+top_corner_x);
				
				char[] actual_tile=gt.getTiles();
				
				int visible=gt.getVisible();
				
				int[] array_index=new int[4];
				
				for (int i=0; i<array_index.length; i++)
				{
					boolean isVisible=(visible & (1 << i))!=0;
					
					if (actual_tile[i]==' ')
						array_index[i]=0;
					else if (actual_tile[i]=='.')
						array_index[i]=1;
					else if (actual_tile[i]=='#')
						array_index[i]=2;
//					else if (actual_tile[i]=='_')
//						array_index[i]=4;
//					else if (actual_tile[i]=='i')
//						array_index[i]=5;
					else if (actual_tile[i]=='<')
						array_index[i]=8;
					else if (actual_tile[i]=='|')
						array_index[i]=9;
					else if (actual_tile[i]=='>')
						array_index[i]=10;
					else
						array_index[i]=3;
					
					if (!(array_index[i]==0))
						g.drawImage(tiles[array_index[i]][i][isVisible?1:0], 
						tile_width*spalte+delta_x+im_delta_x[i], 
						tile_height*zeile+delta_y+im_delta_y[i], this);
					else
						g.drawImage(tiles[array_index[i]][i][0], 
						tile_width*spalte+delta_x+im_delta_x[i], 
						tile_height*zeile+delta_y+im_delta_y[i], this);
				}
				
				String item=gt.getItem();
				
				if (item!="")
				{
					int item_index=-1;
					
					
					if (item=="box_open")
					{
						item_index=0;
					}
					
					else if (item=="box_closed")
					{
						item_index=1;
					}
					
					if (item_index!=-1)
						for (int i=0; i<array_index.length; i++)
						{
							int known=gt.getKnown();
							
							boolean isVisible=(visible & (1 << i))!=0;
							
							if ((known & (1 << i))!=0)
								g.drawImage(items[item_index][i][isVisible?1:0], 
								tile_width*spalte+delta_x+im_delta_x[i], 
								tile_height*zeile+delta_y+im_delta_y[i], this);
						}
				}
			}
		
		int character_width=30;
		int character_height=48;
		
		g.drawImage(
		character[map.getCharacter().getViewDirection()][0], 
		tile_width*(map.getCharacter().getPosX()-top_corner_x)
		+(tile_width-character_width)/2+delta_x, 
		tile_height*(map.getCharacter().getPosY()-top_corner_y)
		+(tile_height-character_height)/2+delta_y, character_width, character_height, this);
		
	}
}