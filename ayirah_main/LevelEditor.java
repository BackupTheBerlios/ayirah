/* 
 * Created on 23.02.2003
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

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class LevelEditor extends JFrame {
	private JToolBar tb;
	private JTabbedPane choose_objects;
	
	public LevelEditor() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		tb=new JToolBar();
		
		choose_objects=new JTabbedPane();
		choose_objects.add("Tiles", new JPanel());
		choose_objects.addTab("Objekte", new JPanel());
		
		getContentPane().setLayout(new BorderLayout());
		
		tb.add(choose_objects);
		
		getContentPane().add(new JPanel(), BorderLayout.CENTER);
		getContentPane().add(tb, BorderLayout.NORTH);
		
		pack();
	}

	public static void main(String args[]) {
		LevelEditor mainFrame = new LevelEditor();
		mainFrame.setSize(400, 400);
		mainFrame.setTitle("LevelEditor");
		mainFrame.setVisible(true);
	}
}
