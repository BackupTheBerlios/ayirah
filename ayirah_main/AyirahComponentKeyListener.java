 /*
 * Created on 09.09.2003
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
import java.awt.event.*;

public class AyirahComponentKeyListener extends KeyAdapter {
	AyirahComponent ac;
	
	/**
	 * 
	 */
	public AyirahComponentKeyListener(AyirahComponent ac) {
		super();
		this.ac=ac;
	}
	
	public void keyPressed(KeyEvent e)
	{
		int kc=e.getKeyCode();
		
		boolean s=e.isControlDown();
		
		if (!s)
		{
		if (kc==KeyEvent.VK_DOWN || kc==KeyEvent.VK_KP_DOWN || 
		kc==KeyEvent.VK_NUMPAD2)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_SOUTH, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_UP || kc==KeyEvent.VK_KP_UP ||
		kc==KeyEvent.VK_NUMPAD8)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_NORTH, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_LEFT || kc==KeyEvent.VK_KP_LEFT ||
		kc==KeyEvent.VK_NUMPAD4)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_WEST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_RIGHT || kc==KeyEvent.VK_KP_RIGHT ||
		kc==KeyEvent.VK_NUMPAD6)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_EAST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
		
		else if (kc==KeyEvent.VK_PAGE_UP || kc==KeyEvent.VK_NUMPAD9)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_NORTH_EAST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_PAGE_DOWN || kc==KeyEvent.VK_NUMPAD3)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_SOUTH_EAST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
	
		else if (kc==KeyEvent.VK_HOME || kc==KeyEvent.VK_NUMPAD7)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_NORTH_WEST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_END || kc==KeyEvent.VK_NUMPAD1)
		{
			try
			{
				ac.getGameMap().move(AyirahStaticVars.DIRECTION_SOUTH_WEST, 
				ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
		
		else if (kc==KeyEvent.VK_X)
		{
			try
			{
				ac.getGameMap().getCharacter().goDown();
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
		
		else if (kc==KeyEvent.VK_Y)
		{
			try
			{
				ac.getGameMap().getCharacter().goUp();
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}

		else if (kc==KeyEvent.VK_Q)
		{
			ac.getGameMap().getCharacter().rotateLeft();
			ac.repaint();
		} 

		else if (kc==KeyEvent.VK_W)
		{
			ac.getGameMap().getCharacter().rotateRight();
			ac.repaint();
		}
		
		else if (kc==KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
		
		else if (kc==KeyEvent.VK_O)
		{
			try
			{
				ac.getGameMap().openThing(ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
		
		else if (kc==KeyEvent.VK_C)
		{
			try
			{
				ac.getGameMap().closeThing(ac.getGameMap().getCharacter());
				ac.repaint();
			}
			catch (IllegalTurnException exc) {}
		}
		}
		
		
		else if (s)
		{
			if (kc==KeyEvent.VK_DOWN || kc==KeyEvent.VK_KP_DOWN || 
			kc==KeyEvent.VK_NUMPAD2)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_SOUTH);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_UP || kc==KeyEvent.VK_KP_UP ||
			kc==KeyEvent.VK_NUMPAD8)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_NORTH);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_LEFT || kc==KeyEvent.VK_KP_LEFT ||
			kc==KeyEvent.VK_NUMPAD4)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_WEST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_RIGHT || kc==KeyEvent.VK_KP_RIGHT ||
			kc==KeyEvent.VK_NUMPAD6)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_EAST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_PAGE_UP || kc==KeyEvent.VK_NUMPAD9)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_NORTH_EAST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_PAGE_DOWN || kc==KeyEvent.VK_NUMPAD3)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_SOUTH_EAST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_HOME || kc==KeyEvent.VK_NUMPAD7)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_NORTH_WEST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}

			else if (kc==KeyEvent.VK_END || kc==KeyEvent.VK_NUMPAD1)
			{
				try
				{
					ac.scrollMap(AyirahStaticVars.DIRECTION_SOUTH_WEST);
					ac.repaint();
				}
				catch (IllegalTurnException exc) {}
			}
			
			
		}
	}
}
