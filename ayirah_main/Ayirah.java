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
import java.awt.*;
import java.awt.event.*;import java.applet.*;
import java.net.*;
// import org.mozilla.javascript.*;
// import org.mozilla.javascript.tools.ToolErrorReporter;

public class Ayirah extends Frame implements KeyListener {
	protected AyirahComponent ac;
	
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
		Ayirah mainFrame = new Ayirah();
		mainFrame.setSize(700, 500);
		mainFrame.setTitle("Ayirah");
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
		// Nein, der JavaScript-Interpreter wird allenfalls später eingebunden
//		Context cx=Context.enter();
//		
//		try {
//			Scriptable scope = cx.initStandardObjects();
//			
//			//String s="java.lang.System.out.println(3)";
//			String s="ayirahFrame.getAyirahComponent().getScrollingPixelsCount()";
//			
//			Scriptable jsArgs = Context.toObject(mainFrame, scope);
//			scope.put("ayirahFrame", scope, jsArgs);
//			
//		 	// Now evaluate the string we've colected.
//		 	Object result = cx.evaluateString(scope, s, "<cmd>", 1, null);
//		 	
//		 	// Convert the result to a string and print it.
//			System.err.println(cx.toString(result));
//		}
//		catch(Exception jse)
//		{
//			System.out.println(jse.toString());
//		}
//			
//		finally {
//			// Exit from the context.
//			Context.exit();
//		}
		
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
