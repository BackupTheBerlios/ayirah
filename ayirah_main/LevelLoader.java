/*
 * Created on 04.07.2004
 * Last modified on 16.07.2004
 *
 * Ayirah - a Java (tm)-based Roleplaying Game 
 * Copyright (C) 2003 Wolfgang Keller
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
 * @author Wolfgang
 */

import java.util.*;
import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

public final class LevelLoader extends DefaultHandler {
	
	private CoordVector actual_coords;
	private int l, x, y;
	
	private GameItem gi;
	private String gi_type, gi_sub_type, gi_state, gi_description;
	private int gi_vis_type, gi_using_directions;
	private boolean gi_takeable, gi_walk_on_able;
	private long gi_weight;
	
	private HashMap coords_items;
	
	private GameItem def;
	private String def_name, def_type, def_sub_type, def_state, def_description;
	private int def_vis_type, def_using_directions;
	private boolean def_takeable, def_walk_on_able;
	private long def_weight;
	
	private boolean use_type, use_sub_type, use_state, use_description,
	use_vis_type, use_using_directions, use_takeable, use_walk_on_able, use_weight;
	
	private HashMap names_objects;
	
	private String use_name;
	
	private boolean inMapTile;
	
	protected LevelLoader()
	{
		coords_items=new HashMap();
		names_objects=new HashMap();
	}
	
	public static final HashMap loadLevel(String file_name)
	{
		LevelLoader handler=new LevelLoader();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse( new File(file_name), handler);
			
		}
		
		catch (SAXParseException spe)
		{
			System.out.println("** Parsing error" 
					+ ", line " + spe.getLineNumber()
					+ ", uri " + spe.getSystemId());
			System.out.println("   " + spe.getMessage() );
			
			/*Exception  x = spe;
			if (spe.getException() != null)
				x = spe.getException();
			x.printStackTrace();*/
		}
		
		catch (SAXException sxe) {
			// Error generated by this application
			// (or a parser-initialization error)
			Exception  x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
			
//			Exception  ex = sxe;
//			if (sxe.getException() != null)
//				x = sxe.getException();
//			x.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return handler.getHashMap();
	}
	
	public HashMap getHashMap()
	{
		return coords_items;
	}
	
	public void startDocument()
	throws SAXException
	{
		
	}
	
	public void endDocument()
	throws SAXException
	{
		
	}
	
	public void startElement(String namespaceURI,
		String lName, // local name
		String qName, // qualified name
		Attributes attrs)
	throws SAXException
	{
		String eName = lName; // element name
		if ("".equals(eName)) eName = qName; // namespaceAware = false
		
		if (eName.equals("maptile"))
		{
			reinitCoords();
		
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					String aName = attrs.getLocalName(i); // Attr name 
					if ("".equals(aName)) 
						aName = attrs.getQName(i);
						
					if (aName.equals("l"))
					{
						l=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("x"))
					{
						x=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("y"))
					{
						y=Integer.parseInt(attrs.getValue(i));
					}
				}
			}
			
			inMapTile=true;
			
			actual_coords=new CoordVector(l, x, y);
		}
		
		else if (eName.equals("object") && inMapTile)
		{
			reinitItem();
		
			if (attrs != null) 
			{
				for (int i = 0; i < attrs.getLength(); i++) {
					String aName = attrs.getLocalName(i); // Attr name 
					if ("".equals(aName)) 
						aName = attrs.getQName(i);
				
					if (aName.equals("type"))
					{
						gi_type=attrs.getValue(i);
					}
			
					else if (aName.equals("sub_type"))
					{
						gi_sub_type=attrs.getValue(i);
					}
		
					else if (aName.equals("state"))
					{
						gi_state=attrs.getValue(i);
					}
					
					else if (aName.equals("description"))
					{
						gi_description=attrs.getValue(i);
					}
					
					else if (aName.equals("vis_type"))
					{
						gi_vis_type=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("using_directions"))
					{
						gi_using_directions=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("take_able"))
					{
						gi_takeable=Boolean.getBoolean(attrs.getValue(i));
					}
					
					else if (aName.equals("walk_on_able"))
					{
						gi_walk_on_able=Boolean.getBoolean(attrs.getValue(i));
					}
					
					else if (aName.equals("weight"))
					{
						gi_weight=Long.parseLong(attrs.getValue(i));
					}
				}
			}
			
			gi=new GameItem(gi_type, gi_sub_type, gi_state, gi_description, gi_vis_type, gi_using_directions,
			gi_takeable,gi_walk_on_able, gi_weight);
		}
		
		else if (eName.equals("object") && !inMapTile)
			System.out.println("<object>-Tags d�rfen nur in <maptile>-Tags benutzt werden");
		
		else if (eName.equals("defobject"))
		{
			reinitDefItem();
		
			if (attrs != null) 
			{
				for (int i = 0; i < attrs.getLength(); i++) {
					String aName = attrs.getLocalName(i); // Attr name 
					if ("".equals(aName)) 
						aName = attrs.getQName(i);
					
					if (aName.equals("name"))
					{
						def_name=attrs.getValue(i);
					}
					
					else if (aName.equals("type"))
					{
						def_type=attrs.getValue(i);
					}
			
					else if (aName.equals("sub_type"))
					{
						def_sub_type=attrs.getValue(i);
					}
		
					else if (aName.equals("state"))
					{
						def_state=attrs.getValue(i);
					}
					
					else if (aName.equals("description"))
					{
						def_description=attrs.getValue(i);
					}
					
					else if (aName.equals("vis_type"))
					{
						def_vis_type=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("using_directions"))
					{
						def_using_directions=Integer.parseInt(attrs.getValue(i));
					}
					
					else if (aName.equals("take_able"))
					{
						def_takeable=Boolean.getBoolean(attrs.getValue(i));
					}
					
					else if (aName.equals("walk_on_able"))
					{
						def_walk_on_able=Boolean.getBoolean(attrs.getValue(i));
					}
					
					else if (aName.equals("weight"))
					{
						def_weight=Long.parseLong(attrs.getValue(i));
					}
				}
			}
			
			if (def_name!=null)
			{
				def=new GameItem(def_type, def_sub_type, def_state, def_description, 
				def_vis_type, def_using_directions, def_takeable,def_walk_on_able, def_weight);
				
				names_objects.put(def_name, def);
			}
			
			else
			{
				System.out.println("<defobject> muss ein name-Attribut besitzen");
			}
		}
		
		else if (eName.equals("useobject") && inMapTile)
		{
			reinitUseItem();
		
			if (attrs != null) 
			{
				for (int i = 0; i < attrs.getLength(); i++) {
					String aName = attrs.getLocalName(i); // Attr name 
					if ("".equals(aName)) 
						aName = attrs.getQName(i);
					
					if (aName.equals("name"))
					{
						use_name=attrs.getValue(i);
					}
					
					else if (aName.equals("type"))
					{
						gi_type=attrs.getValue(i);
						use_type=true;
					}
			
					else if (aName.equals("sub_type"))
					{
						gi_sub_type=attrs.getValue(i);
						use_sub_type=true;
					}
		
					else if (aName.equals("state"))
					{
						gi_state=attrs.getValue(i);
						use_state=true;
					}
					
					else if (aName.equals("description"))
					{
						gi_description=attrs.getValue(i);
						use_description=true;
					}
					
					else if (aName.equals("vis_type"))
					{
						gi_vis_type=Integer.parseInt(attrs.getValue(i));
						use_vis_type=true;
					}
					
					else if (aName.equals("using_directions"))
					{
						gi_using_directions=Integer.parseInt(attrs.getValue(i));
						use_using_directions=true;
					}
					
					else if (aName.equals("take_able"))
					{
						gi_takeable=Boolean.getBoolean(attrs.getValue(i));
						use_takeable=true;
					}
					
					else if (aName.equals("walk_on_able"))
					{
						gi_walk_on_able=Boolean.getBoolean(attrs.getValue(i));
						use_walk_on_able=true;
					}
					
					else if (aName.equals("weight"))
					{
						gi_weight=Long.parseLong(attrs.getValue(i));
						use_weight=true;
					}
				}
			}
			
			if (use_name!=null)
			{
				if (names_objects.containsKey(use_name))
				{
					GameItem foo=(GameItem) (names_objects.get(use_name));
					
					if (foo != null)
					{
						gi=new GameItem(
							use_type ? gi_type : foo.getType(),
							use_sub_type ? gi_sub_type : foo.getSubType(),
							use_state ? gi_state : foo.getState(),
							use_description ? gi_description : foo.getDescription(),
							use_vis_type ? gi_vis_type : foo.getVisibilityType(),
							use_using_directions ? gi_using_directions : foo.getUsingDirections(),
							use_takeable ? gi_takeable : foo.isTakeable(),
							use_walk_on_able ? gi_walk_on_able : foo.isWalkOnAble(),
							use_weight ? gi_weight : foo.getWeight());
					}
					
					else
					{
						System.out.print("Ein Objekt mit dem Namen ");
						System.out.print(use_name);
						System.out.println(" zeigt auf null");
					}
				}
				
				else
				{
					System.out.print("Ein Objekt mit dem Namen ");
					System.out.print(use_name);
					System.out.println(" existiert nicht.");
				}
			}
			
			else
			{
				System.out.println("<useobject> muss ein name-Attribut besitzen");
			}
		}
		
		else if (eName.equals("useobject") && !inMapTile)
			System.out.println("<useobject>-Tags d�rfen nur in <maptile>-Tags benutzt werden");
	}
	
	public void endElement(String namespaceURI,
		String sName, // simple name
		String qName  // qualified name
		)
		throws SAXException
	{
		String eName = sName; // element name
		if ("".equals(eName)) eName = qName; // namespaceAware = false
		
		if (eName.equals("maptile") && gi!=null)
		{
			coords_items.put(actual_coords, (GameItem) gi.clone());
			inMapTile=false;
		}
	}
	
	protected void reinitCoords()
	{
		actual_coords=null;
		l=0;
		x=0;
		y=0;
	}
	
	protected void reinitItem()
	{
		gi=null;
		gi_type="";
		gi_sub_type="";
		gi_state="";
		gi_description="";
		gi_vis_type=0;
		gi_using_directions=15;
		gi_takeable=false;
		gi_walk_on_able=false;
		gi_weight=0;
	}
	
	protected void reinitDefItem()
	{
		def=null;
		def_name=null;
		def_type="";
		def_sub_type="";
		def_state="";
		def_description="";
		def_vis_type=0;
		def_using_directions=15;
		def_takeable=false;
		def_walk_on_able=false;
		def_weight=0;
	}
	
	protected void reinitUseItem()
	{
		reinitItem();
		use_name=null;
		
		use_type=false;
		use_sub_type=false;
		use_state=false;
		use_description=false;
		use_vis_type=false;
		use_using_directions=false;
		use_takeable=false;
		use_walk_on_able=false;
		use_weight=false;
	}
}
