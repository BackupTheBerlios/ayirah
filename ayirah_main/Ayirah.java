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
import java.applet.*;
import java.net.*;

public class Ayirah extends Frame implements KeyListener {
	public AyirahComponent ac;
	
	public Ayirah() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		ac=new AyirahComponent();
		this.add(ac);
		this.addKeyListener(this);
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

	
	public static void main(String args[]) {
		System.out.println();
		Ayirah mainFrame = new Ayirah();
		mainFrame.setSize(700, 500);
		mainFrame.setTitle("Ayirah");
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		try
		{
			AudioClip ac = Applet.newAudioClip(
			new URL("file:////C:\\eclipse\\workspace\\Ayirah\\bg.wav"));
			
			ac.loop();
		}
		catch(MalformedURLException e)
		{
			System.out.println(e.toString());
		}
	}
}
