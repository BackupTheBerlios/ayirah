/* 
 * Created on 10.09.2003
 * Last modified on 14.07.2004
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
	private Image[][][] character;
//	private Image test;
	
	private GameMap map;
	
//	private int top_x, top_y;
	
	private int max_top_x, max_top_y;
	
	private int scrolling_pixels_count=4;
	
	private static final int tile_width=48;
	private static final int tile_height=48;
	
	private Image dbImage;
	private Graphics dbGraphics;
	
	private final int[] im_delta_x={
		0, tile_width/2, 0, 0
	};
	private final int[] im_delta_y={
		0, 0, tile_height/2, 0
	};
	
	boolean map_changed=false;
	boolean actualize=true;
	
	AyirahComponent(GameMap gm)
	{
		map=gm;
		m_tiles=new MediaTracker(this);
		tiles=new Image[11][4][3];
		character=new Image[2][8][4];
		items=new Image[2][4][3];
		
		Image[] prepareImage=new Image[1+AyirahStaticVars.
		map_object_states.length*AyirahStaticVars.tile_names.length];
		
		Image[] prepareItems=new Image[AyirahStaticVars.
		map_object_states.length*AyirahStaticVars.item_names.length];
		
		max_top_x=map.getWidth()*tile_width-1024;
		max_top_y=map.getHeight()*tile_height-768;
		
		prepareImage[0]=getToolkit().getImage(AyirahStaticVars.tile_prefix+"unknown1.gif");
		m_tiles.addImage(prepareImage[0], 0);
		
		// erst Tiles
		for (int i=0; i<AyirahStaticVars.tile_names.length; i++)
			for (int j=0; j<AyirahStaticVars.map_object_states.length; j++)
			{
				prepareImage[AyirahStaticVars.map_object_states.length*i
				+j+1]=getToolkit().getImage(
				AyirahStaticVars.tile_prefix
				+AyirahStaticVars.tile_names[i]
				+AyirahStaticVars.map_object_states[j]
				+AyirahStaticVars.map_object_ending);
				m_tiles.addImage(prepareImage[AyirahStaticVars.
				map_object_states.length*i+j], 0);
			}
		
		// dann Items
		for (int i=0; i<AyirahStaticVars.item_names.length; i++)
			for (int j=0; j<AyirahStaticVars.map_object_states.length; j++)
			{
				prepareItems[AyirahStaticVars.
				map_object_states.length*i+j]=getToolkit().getImage(
				AyirahStaticVars.item_prefix
				+AyirahStaticVars.item_names[i]
				+AyirahStaticVars.map_object_states[j]
				+AyirahStaticVars.map_object_ending);
				m_tiles.addImage(prepareItems[AyirahStaticVars.
				map_object_states.length*i+j], 0);
			}

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
		
		for (int i=0; i<AyirahStaticVars.tile_names.length; i++)
		{
			for (int j=0; j<AyirahStaticVars.map_object_states.length; j++)
			{
				if (prepareImage[i*3+j+1]!=null)
				{
					collectionProducer=prepareImage[i*3+j+1].getSource();
					
					tiles[i+1][0][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_north));
					m_tiles.addImage(tiles[i+1][0][j], 2);
					
					tiles[i+1][1][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_east));
					m_tiles.addImage(tiles[i+1][1][j], 2);
					
					tiles[i+1][2][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_south));
					m_tiles.addImage(tiles[i+1][2][j], 2);
					
					tiles[i+1][3][j]=createImage(new FilteredImageSource(
					collectionProducer,filter_west));
					m_tiles.addImage(tiles[i+1][3][j], 2);
				}
			}
		}
		
		
		
		for (int i=0; i<AyirahStaticVars.item_names.length; i++)
		{
			for (int j=0; j<AyirahStaticVars.map_object_states.length; j++)
			{
				collectionProducer=prepareItems[i*AyirahStaticVars.
				map_object_states.length+j].getSource();
				
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
		
		prepareImage=null;
		prepareItems=null;
		
		for (int h=0; h<2; h++)
			for (int i=0; i<8; i++)
				for (int j=0; j<4; j++)
				{
					character[h][i][j]=
					getToolkit().createImage(AyirahStaticVars.avtr_prefix
					+(h+1)+AyirahStaticVars.sprite_name[j]+i+".gif");
					
					m_tiles.addImage(character[h][i][j], 4);
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
			map.getCharacter(map.getActualCharacterIndex()).
			setScrollTopY(map.getCharacter(map.getActualCharacterIndex()).getScrollTopY()+pixels);
			
			if (map.getCharacter(map.getActualCharacterIndex()).getScrollTopY()>max_top_y)
				map.getCharacter(map.getActualCharacterIndex()).setScrollTopY(max_top_y);
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_NORTH)
		{
			map.getCharacter(map.getActualCharacterIndex()).
			setScrollTopY(map.getCharacter(map.getActualCharacterIndex()).getScrollTopY()-pixels);
			
			if (map.getCharacter(map.getActualCharacterIndex()).getScrollTopY()<0)
				map.getCharacter(map.getActualCharacterIndex()).setScrollTopY(0);
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_EAST)
		{
			map.getCharacter(map.getActualCharacterIndex()).
			setScrollTopX(map.getCharacter(map.getActualCharacterIndex()).getScrollTopX()+pixels);
			
			if (map.getCharacter(map.getActualCharacterIndex()).getScrollTopX()>max_top_x)
				map.getCharacter(map.getActualCharacterIndex()).setScrollTopX(max_top_x);
		}
		
		else if (direction==AyirahStaticVars.DIRECTION_WEST)
		{
			map.getCharacter(map.getActualCharacterIndex()).
			setScrollTopX(map.getCharacter(map.getActualCharacterIndex()).getScrollTopX()-pixels);
			
			if (map.getCharacter(map.getActualCharacterIndex()).getScrollTopX()<0)
				map.getCharacter(map.getActualCharacterIndex()).setScrollTopX(0);
		}
	}
	
	public void actualize()
	{
		this.actualize=true;
		repaint();
	}
	
	/*public void update(Graphics g)
	{
		paint(g);
	}*/
	
	public void update (Graphics g)
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
			paint(dbGraphics);
			actualize=false;
		}
		
		g.drawImage(dbImage,-map.getCharacter(map.getActualCharacterIndex()).getScrollTopX(), 
			-map.getCharacter(map.getActualCharacterIndex()).getScrollTopY(),this);
	}

	
	public void paint (Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(
		0,0,map.getWidth()*tile_width, 
		map.getHeight()*tile_height);
		
		for (int zeile=0; zeile<map.getHeight(); zeile++)
			for (int spalte=0; spalte<map.getWidth(); spalte++)
			{
				GameTile gt=map.getAllCharacterKnownTile
				(map.getCharacter(map.getActualCharacterIndex())
				.getLayer(), zeile, spalte);
				
				char[] actual_tile=gt.getTiles();
				
				int visible=gt.getVisible();
				int vis_other=gt.getVisibleOther();
				
				int[] array_index=new int[4];
				
				for (int i=0; i<array_index.length; i++)
				{
					boolean isVisible=(visible & (1 << i))!=0;
					boolean isVisibleOther=(vis_other & (1 << i))!=0;
					
					if (actual_tile[i]=='?')
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
					else if (actual_tile[i]==' ')
						array_index[i]=3;
					
					if (array_index[i]!=0)
					{
						if (isVisible)
						g.drawImage(tiles[array_index[i]][i][1], 
						tile_width*spalte+im_delta_x[i], 
						tile_height*zeile+im_delta_y[i], this);
						
						else if (isVisibleOther)
						g.drawImage(tiles[array_index[i]][i][2], 
						tile_width*spalte+im_delta_x[i], 
						tile_height*zeile+im_delta_y[i], this);
						
						else
						g.drawImage(tiles[array_index[i]][i][0], 
						tile_width*spalte+im_delta_x[i], 
						tile_height*zeile+im_delta_y[i], this);
						
					}
					else
						g.drawImage(tiles[array_index[i]][i][0], 
						tile_width*spalte+im_delta_x[i], 
						tile_height*zeile+im_delta_y[i], this);
				}
				
				
				if (gt.getItem()!=null)
				{
					String item_type=gt.getItem().getType();
					String item_state=gt.getItem().getState();
					
					int item_index=-1;
					
					if (item_type.equals("box") && item_state.equals("open"))
					{
						item_index=0;
					}
					
					else if (item_type.equals("box") && item_state.equals("closed"))
					{
						item_index=1;
					}
					
					if (item_index!=-1)
						for (int i=0; i<array_index.length; i++)
						{
							int known=gt.getKnown();
							
							boolean isVisible=(visible & (1 << i))!=0;
							boolean isVisibleOther=(vis_other & (1 << i))!=0;
							
							if (((known & (1 << i))!=0) && 
							((gt.getItem().getUsingDirections() & (1 << i))!=0))
									
							{
								if (isVisible)
								g.drawImage(items[item_index][i][1], 
								tile_width*spalte+im_delta_x[i], 
								tile_height*zeile+im_delta_y[i], this);
								
								else if (isVisibleOther)
								g.drawImage(items[item_index][i][2], 
								tile_width*spalte+im_delta_x[i], 
								tile_height*zeile+im_delta_y[i], this);
								
								else
								g.drawImage(items[item_index][i][0], 
								tile_width*spalte+im_delta_x[i], 
								tile_height*zeile+im_delta_y[i], this);
							}
						}
				}
			}
		
		int character_width=30;
		int character_height=48;
		
//		Code zum transparenten Zeichnen eines Bildes
//		Graphics2D g2=(Graphics2D) g;
//		
//		AlphaComposite ac=AlphaComposite.getInstance(
//		AlphaComposite.SRC_OVER, 0.5f);
//		
//		g2.setComposite(ac); 
//		g2.drawImage(<foo>);
		
		for (int i=0; i<map.getCharactersCount(); i++)
		{
			if (map.getCharacter(map.getActualCharacterIndex()).getLayer()
			 == map.getCharacter(i).getLayer())
			g.drawImage(
			character[i][map.getCharacter(i).getViewDirection()][0], 
			tile_width*(map.getCharacter(i).getPosX())
			+(tile_width-character_width)/2, 
			tile_height*(map.getCharacter(i).getPosY())
			+(tile_height-character_height)/2, character_width, 
			character_height, this);
		}
		
//		g.drawImage(test, 0,0, this);
	}
}