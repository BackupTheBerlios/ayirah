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
	private Image[][] tiles; // Alle Tiles
	private Image[][] items;
	private Image[][] character;
	
	private GameMap map;
	
	private int top_x, top_y;
	private int max_top_x, max_top_y;
	
	private int scrolling_pixels_count=4;
	
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
	

	private final AlphaComposite ac_unvis=AlphaComposite.getInstance(
	AlphaComposite.SRC_OVER, 0.5f);
						
	private final AlphaComposite ac_vis=AlphaComposite.getInstance(
	AlphaComposite.SRC_OVER, 1.0f);
	
	boolean map_changed=false;
	boolean actualize=true;
	
	AyirahComponent()
	{
		map=new GameMap();
		m_tiles=new MediaTracker(this);
		tiles=new Image[11][4];
		character=new Image[8][4];
		items=new Image[2][4];
		
		Image[] prepareImage=new Image[11];
		Image[] prepareItems=new Image[2];
		
		max_top_x=map.getWidth()*tile_width-1024;
		max_top_y=map.getHeight()*tile_height-768;
		
		prepareImage[0]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"unknown1.gif");
		m_tiles.addImage(prepareImage[0], 0);
		
		prepareImage[1]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"boden1_visible.gif");
		m_tiles.addImage(prepareImage[1], 0);
		
		prepareImage[2]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"wand1_visible.gif");
		m_tiles.addImage(prepareImage[2], 0);
		
		prepareImage[3]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"placeholder1_visible.gif");
		m_tiles.addImage(prepareImage[3], 0);
		
		prepareImage[4]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_waagrecht_offen_visible.gif");
		m_tiles.addImage(prepareImage[4], 0);
		
		prepareImage[5]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_waagrecht_geschlossen_visible.gif");
		m_tiles.addImage(prepareImage[5], 0);
		
		prepareImage[6]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_senkrecht_offen_visible.gif");
		m_tiles.addImage(prepareImage[6], 0);
		
		prepareImage[7]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"tuer_senkrecht_geschlossen_visible.gif");
		m_tiles.addImage(prepareImage[7], 0);
		
		prepareImage[8]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_visible.gif");
		m_tiles.addImage(prepareImage[8], 0);
		
		prepareImage[9]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_hoch_runter_visible.gif");
		m_tiles.addImage(prepareImage[9], 0);
		
		prepareImage[10]=getToolkit().getImage(
		AyirahStaticVars.tile_prefix+"treppe_runter_visible.gif");
		m_tiles.addImage(prepareImage[10], 0);
		
		// -------jetzt Items--------------
		prepareItems[0]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_open_visible.gif");
		m_tiles.addImage(prepareItems[0], 0);
		
		prepareItems[1]=getToolkit().getImage(
		AyirahStaticVars.item_prefix+"box_closed_visible.gif");
		m_tiles.addImage(prepareItems[1], 0);

		try {
			m_tiles.waitForAll();
		} catch (InterruptedException e) { }
		
		// -------Bilder vorbereiten----------
		
		ImageFilter filter_north=new CropImageFilter(0,0, 48, 24);
		ImageFilter filter_west=new CropImageFilter(48, 0, 24, 48);
		ImageFilter filter_south=new CropImageFilter(0, 24, 48, 24);
		ImageFilter filter_east=new CropImageFilter(72, 0, 24, 48);
		
		try {
			m_tiles.waitForAll();
		} catch (InterruptedException e) { }
		
		ImageProducer collectionProducer;
		
		for (int i=0; i<11; i++)
		{
			if (prepareImage[i]!=null)
			{
				collectionProducer=prepareImage[i].getSource();
				
				tiles[i][0]=createImage(new FilteredImageSource(
				collectionProducer,filter_north));
				m_tiles.addImage(tiles[i][0], 2);
				
				tiles[i][1]=createImage(new FilteredImageSource(
				collectionProducer,filter_east));
				m_tiles.addImage(tiles[i][1], 2);
				
				tiles[i][2]=createImage(new FilteredImageSource(
				collectionProducer,filter_south));
				m_tiles.addImage(tiles[i][2], 2);
				
				tiles[i][3]=createImage(new FilteredImageSource(
				collectionProducer,filter_west));
				m_tiles.addImage(tiles[i][3], 2);
			}
		}
		
		for (int i=0; i<2; i++)
		{
				collectionProducer=prepareItems[i].getSource();
				
				items[i][0]=createImage(new FilteredImageSource(
				collectionProducer,filter_north));
				m_tiles.addImage(items[i][0], 3);
				
				items[i][1]=createImage(new FilteredImageSource(
				collectionProducer,filter_east));
				m_tiles.addImage(items[i][1], 3);
				
				items[i][2]=createImage(new FilteredImageSource(
				collectionProducer,filter_south));
				m_tiles.addImage(items[i][2], 3);
				
				items[i][3]=createImage(new FilteredImageSource(
				collectionProducer,filter_west));
				m_tiles.addImage(items[i][3], 3);
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
	
	public int getScrollingPixelsCount()
	{
		return this.scrolling_pixels_count;
	}
	
	public void scrollMap(int direction, int pixels)
	{
		if (direction<0 || direction>=8)
			throw new IllegalTurnException("Falscher Parameter");
		
		if (direction%2!=0)
		{
			scrollMap((direction+7)%8, pixels);
			scrollMap((direction+1)%8, pixels);
		}
		
		if (direction==AyirahStaticVars.DIRECTION_SOUTH)
		{
			top_y+=pixels;
			
			if (top_y>max_top_y)
				top_y=max_top_y;
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_NORTH)
		{
			top_y-=pixels;
			
			if (top_y<0)
				top_y=0;
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_EAST)
		{
			top_x+=pixels;
			
			if (top_x>max_top_x)
				top_x=max_top_x;
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_WEST)
		{
			top_x-=pixels;
			
			if (top_x<0)
				top_x=0;
		}
	}
	
	public void actualize()
	{
		this.actualize=true;
		repaint();
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint (Graphics g)
	{
		if (dbImage == null || map_changed) {
			// Diese Zeile verursacht den Fehler bei der Soundwiedergabe
			// (seltsamerweise störte sie in der alten Version nicht)
			dbImage = createImage(map.getWidth()*tile_width, 
			map.getHeight()*tile_height);
		}
		
		if (actualize)
		{
			dbGraphics = dbImage.getGraphics();
			
			// Hintergrund löschen
			dbGraphics.setColor(Color.BLACK);
			dbGraphics.fillRect(0,0,this.getSize().width,this.getSize().height);
			
			// Vordergrund zeichnen
			dbGraphics.setColor(getForeground());
			paintIt(dbGraphics);
			actualize=false;
		}
		
		g.drawImage(dbImage,-top_x,-top_y,this);
	}

	
	public void paintIt (Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(
		0,0,map.getWidth()*tile_width, 
		map.getHeight()*tile_height);
		
		for (int zeile=0; zeile<map.getHeight(); zeile++)
			for (int spalte=0; spalte<map.getWidth(); spalte++)
			{
				GameTile gt=map.getCreatureKnownTile
				(map.getCharacter(), map.getCharacter().getLayer(), 
				zeile, spalte);
				
				char[] actual_tile=gt.getTiles();
				
				int visible=gt.getVisible();
				
				int[] array_index=new int[4];
				
				for (int i=0; i<array_index.length; i++)
				{
					if (actual_tile[i]==' ')
						array_index[i]=0;
					else if (actual_tile[i]=='.')
						array_index[i]=1;
					else if (actual_tile[i]=='#')
						array_index[i]=2;
					else if (actual_tile[i]=='_')
						array_index[i]=4;
					else if (actual_tile[i]=='-')
						array_index[i]=5;
					else if (actual_tile[i]=='i')
						array_index[i]=6;
					else if (actual_tile[i]=='I')
						array_index[i]=7;
					else if (actual_tile[i]=='<')
						array_index[i]=8;
					else if (actual_tile[i]=='|')
						array_index[i]=9;
					else if (actual_tile[i]=='>')
						array_index[i]=10;
					else
						array_index[i]=3;
					
					g.drawImage(tiles[array_index[i]][i], 
					tile_width*spalte+im_delta_x[i], 
					tile_height*zeile+im_delta_y[i], this);
				}
				
				String item=gt.getItem();
				
				if (item!="")
				{
					int item_index=-1;
					
					if (item=="box_open")
						item_index=0;
					else if (item=="box_closed")
						item_index=1;
					
					if (item_index!=-1)
						for (int i=0; i<array_index.length; i++)
						{
							int known=gt.getKnown();

							if ((known & (1 << i))!=0)
								g.drawImage(items[item_index][i], 
								tile_width*spalte+im_delta_x[i], 
								tile_height*zeile+im_delta_y[i], this);
						}
				}
				
				Graphics2D g2=(Graphics2D) g;
				g2.setComposite(ac_unvis); 
				
				// Sichtbarkeit einschränken
				for (int i=0; i<array_index.length; i++)
				{
					boolean isVisible=(visible & (1 << i))!=0;
					
					if (!isVisible)
					{
						g2.drawImage(tiles[0][i], tile_width*spalte+im_delta_x[i], 
						tile_height*zeile+im_delta_y[i], this);
					}
				}
				
				g2.setComposite(ac_vis);		
			}
		
		int character_width=30;
		int character_height=48;
		
		g.drawImage(
		character[map.getCharacter().getViewDirection()][0], 
		tile_width*(map.getCharacter().getPosX())
		+(tile_width-character_width)/2, 
		tile_height*(map.getCharacter().getPosY())
		+(tile_height-character_height)/2, character_width, character_height, this);
	}
}