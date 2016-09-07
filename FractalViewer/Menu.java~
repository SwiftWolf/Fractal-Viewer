package onepointfive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//import onepointfive.FractalFrame.SelectedPointListener;

public class Menu {

	protected ComplexNumber pointToShow;
	protected int iterations;
	protected double userXPoint;
	protected double userYPoint;

	protected Object getUserXPoint;
	protected Object getUserYPoint;
	protected FractalFrame window;
	protected JMenu favs, loadFrac, savedFrac;
	protected JMenuItem save, actLoad, delete;

	public Menu(Object getUserXPoint, Object getUserYPoint, FractalFrame window) {
		this.getUserXPoint = getUserXPoint;
		this.getUserYPoint = getUserYPoint;
		this.window = window;
	}

	public JMenuBar createMenuBar()	{
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		//Add a JMenu
		favs = new JMenu("Favourites");
		menuBar.add(favs);

		// Now we want to fill each of the menus.
		// Starters. This is a simple Menu, with three MenuItems.

		save = new JMenuItem("Save current Julia fractal");
		loadFrac = new JMenu("Load Julia fractal");
		loadFrac.setVisible(false);
		savedFrac = new JMenu();
		savedFrac.setVisible(false);

		//	System.out.println(window.pointToSave());
		//	JMenu savedFrac = new JMenu(Double.toString(window.getUserXPoint()));
		actLoad = new JMenuItem("Load");
		delete = new JMenuItem("Delete");

		loadFrac.add(savedFrac);
		savedFrac.add(actLoad);
		savedFrac.add(delete);

		favs.add(save);
		favs.add(loadFrac);

		save.addActionListener(new SaveListener());
		actLoad.addActionListener(new LoadListener());

		return menuBar;
	}

	public void setIterations(int iterations){
		this.iterations = iterations;
	}

	class SaveListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			loadFrac.setVisible(true);
			
			//getSaveParts is the real and imagnary point the users chose and the iteration it was on
			JMenu newSave = new JMenu(window.getSaveParts());
			loadFrac.add(newSave);
			newSave.add(actLoad);
			newSave.add(delete);

		}
	}
	
	class LoadListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			//System.out.println(newSave);
			System.out.println("Loading. . .");
		}
	}
}






