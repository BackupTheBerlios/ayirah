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
import java.util.*;
import java.awt.*;
import java.awt.event.*;import java.applet.*;
import java.net.*;

public class Ayirah extends Frame implements KeyListener {
	protected AyirahComponent ac;
	protected GameMap gm;
	
	public Ayirah(HashMap level) {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		gm=new GameMap(2, level);
		
		ac=new AyirahComponent(gm);
		this.add(ac);
		this.addKeyListener(this);
		
		MediaTracker mt=new MediaTracker(this);
		Image ic=getToolkit().createImage(AyirahStaticVars.cursor_name);
		mt.addImage(ic, 0);
		
		try
		{
			mt.waitForAll();
		}
		
		catch (InterruptedException e)
		{
		}
		
		this.setCursor(this.getToolkit().createCustomCursor(ic, new Point(2,2), "cursor"));
	}
	
	public void keyPressed(KeyEvent e)
	{
		ac.getKeyListener().keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public AyirahComponent getAyirahComponent()
	{
		return ac;
	}
	
	public static void main(String args[]) {
		HashMap hm=LevelLoader.loadLevel("levels/level.xml");
		
		Ayirah mainFrame = new Ayirah(hm);
		mainFrame.setSize(700, 500);
		mainFrame.setTitle("Ayirah");
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		try
		{
			AudioClip ac = Applet.newAudioClip(
			new URL( "file:" + System.getProperty( "user.dir" ) + "/sound/jacklight.mid" ));
			ac.loop();
		}
		
		catch(MalformedURLException e)
		{
			System.out.println(e.toString());
		}
	}
}
