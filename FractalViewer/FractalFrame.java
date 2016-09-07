import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/* I have completed parts 1 - 6 and part of 7, I have extended my fractal explorer with the the Burning Ship fractal 
 * and have implemented the ability to toggle live updates of the Juila Set when the mouse is dragged over the main fractal (Mandelbrot or Burning Ship).
 * When any changes are made to the text fields or the Burning Ship toggle is switched the Generate button must be clicked to see the updates/changes.
 * Creating and tracking of the Juila Set are done with the left mouse button, zooming is done with the right mouse button.
 * */

@SuppressWarnings("serial")
public class FractalFrame extends JFrame{
	private JLabel realPointMin, realPointMax, imagPointMax,  imagPointMin, iterationsLabal, pointOfSelect, generateLabel;
	private JPanel componentPanel, mandFractalPanel, mandlePanel, juilaFractalPanel, juilaPanel;
	private double userXPoint, userYPoint,xPressed, yPressed, xReleased, yReleased;
	private JTextField selectedPoint, realMax, realMin, imagMax, imagMin;
	private JButton generatebutton, saveButton, loadButton, deleteButton, resetButton;
	private JCheckBox liveActionButton;
	public  JCheckBox burningShipButton;
	private SpinnerModel iterationsSpin;
	private ComplexNumber pointToShow;
	private Mandelbrot mandbro;
	private JuliaSet juliaGal;
	private JSpinner iterations;
	private String savePoint;
	private int currentIter;
	// To toggle when the mouse can zoom
	private boolean zooming = false;
	private int zoomX, zoomY, curX, curY;

	//Set of user saved complex numbers
	final Set<ComplexNumber> favourites = new HashSet<ComplexNumber>();
	// Allows the user to select from the ComboBox the saved Julia fractal they want to view
	JComboBox<ComplexNumber> pointsSaved = new JComboBox<ComplexNumber>(favourites.toArray(new ComplexNumber[0]));

	public static void main(String[] args) {
		FractalFrame MandelFrame = new FractalFrame();
		MandelFrame.setVisible(true);
	}

	public FractalFrame(){
		super("Fractal Explorer");

		// New instances of the Mandelbort and Julia Set classes
		mandbro = new Mandelbrot(this);
		juliaGal = new JuliaSet(this);

		this.GUIManager();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void GUIManager(){

		// Size for the full GUI
		setSize(new Dimension(1400,700));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container mainPanel = this.getContentPane();

		// Main panel that all other panels are added to
		mainPanel.setLayout(new BorderLayout());
		getContentPane().setBackground(Color.white);

		// Panel that runs along the top edge of the GUI and holds the components
		componentPanel = new JPanel();
		componentPanel.setLayout(new GridBagLayout());
		mainPanel.add(componentPanel, BorderLayout.NORTH);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		realPointMin = new JLabel("Real Min: ");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		componentPanel.add(realPointMin, c);

		// Is initialised with the MIN of the real axis
		realMin = new JTextField(Double.toString(mandbro.getRealAxisFrom()));
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		componentPanel.add(realMin, c);

		realPointMax = new JLabel("Real Max: ");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 1;
		componentPanel.add(realPointMax, c);

		// Is initialised with the MAX of the real axis
		realMax = new JTextField(Double.toString(mandbro.getRealAxisTo()));
		c.ipady = 5;
		c.gridx = 1;
		c.gridy = 1;
		componentPanel.add(realMax, c);

		imagPointMin = new JLabel("Imaginary Min: ");
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		componentPanel.add(imagPointMin, c);

		// Is initialised with the MIN of the imaginary axis
		imagMin = new JTextField(Double.toString(mandbro.getImaginaryAxisFrom()));
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 0;
		componentPanel.add(imagMin, c);

		imagPointMax = new JLabel("Imaginary Max: ");
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		componentPanel.add(imagPointMax, c);

		// Is initialised with the MAX of the imaginary axis
		imagMax = new JTextField(Double.toString(mandbro.getImaginaryAxisTo()));
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 1;
		componentPanel.add(imagMax, c);

		iterationsLabal = new JLabel("Iteration no. : ");
		c.weightx = 0.2;
		c.gridx = 4;
		c.gridy = 0;
		componentPanel.add(iterationsLabal, c);

		// Current value 100, minimum 0, maximum 5000, step size 50
		iterationsSpin = new SpinnerNumberModel(mandbro.getIterations(), 0, 5000, 50);
		iterations = new JSpinner(iterationsSpin);
		c.weightx = 0.5;
		c.gridx = 5;
		c.gridy = 0;
		componentPanel.add(iterations, c);

		pointOfSelect = new JLabel("Selected Point: ");
		c.weightx = 0.2;
		c.gridx = 4;
		c.gridy = 1;
		componentPanel.add(pointOfSelect, c);

		// Displays the point that the user last clicked on
		selectedPoint = new JTextField();
		c.weightx = 0.5;
		c.gridx = 5;
		c.gridy = 1;
		componentPanel.add(selectedPoint, c);

		generateLabel = new JLabel("Update fractal: ");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 2;
		componentPanel.add(generateLabel, c);


		// Refreshes after the JSpinner components are changed
		generatebutton = new JButton("Generate");
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 2;
		componentPanel.add(generatebutton, c);

		// Displays the points of the fractals that the user has saved
		pointsSaved = new JComboBox();
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 2;
		componentPanel.add(pointsSaved, c);
		pointsSaved.setVisible(false);

		saveButton = new JButton("Save");
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 2;
		componentPanel.add(saveButton, c);
		saveButton.setVisible(false);

		saveButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// Makes a new complex number with points that the user clicked on
				ComplexNumber thisToSave = new ComplexNumber(getUserXPoint(), getUserYPoint());

				// Stops the same points from being saved twice
				for(ComplexNumber x : favourites) { 
					if(x.equals(thisToSave)){
						return;
					}
				}
				// Makes the buttons related to saving visible
				pointsSaved.setVisible(true);
				loadButton.setVisible(true);
				deleteButton.setVisible(true);

				// Adds the newly created complex number to a set of saved complex numbers
				favourites.add(thisToSave);
				pointsSaved.addItem(thisToSave);
			}
		});

		loadButton = new JButton("Load");
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		componentPanel.add(loadButton, c);
		loadButton.setVisible(false);

		loadButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Creates a complex number from the points that the user saved to redraw the saved fractal
				ComplexNumber c = (ComplexNumber) pointsSaved.getSelectedItem();
				userXPoint = c.getReal();
				userYPoint = c.getImaginary();
				juliaGal.repaint();
			}
		});

		deleteButton = new JButton("Delete");
		c.weightx = 0.5;
		c.gridx = 5;
		c.gridy = 2;
		componentPanel.add(deleteButton, c);
		deleteButton.setVisible(false);
		deleteButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// Removes the selcted saved points from both the list and the set
				favourites.remove(pointsSaved.getSelectedItem());
				pointsSaved.removeItem(pointsSaved.getSelectedItem());
			}
		});

		generateLabel = new JLabel("Reset fractal to starting state: ");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 3;
		componentPanel.add(generateLabel, c);

		liveActionButton = new JCheckBox("Live Action Toggle");
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 3;
		componentPanel.add(liveActionButton, c);
		
		burningShipButton = new JCheckBox("Burning Ship Fractal Toggle");
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 3;
		componentPanel.add(burningShipButton, c);

		resetButton = new JButton("Reset");
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 3;
		componentPanel.add(resetButton, c);
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			
				// Changes all the values to their defaults
				mandbro.setIterations(Mandelbrot.DefaultIterations);
				juliaGal.setIterations(Mandelbrot.DefaultIterations);
				mandbro.setrealAxisFrom(Mandelbrot.RMIN);
				mandbro.setrealAxisTo(Mandelbrot.RMAX);
				mandbro.setimaginaryAxisFrom(Mandelbrot.IMIN);
				mandbro.setimaginaryAxisTo(Mandelbrot.IMAX);
				
				// Updates the text fields with the default values
				realMin.setText(Double.toString(Mandelbrot.RMIN));
				realMax.setText(Double.toString(Mandelbrot.RMAX));
				imagMin.setText(Double.toString(Mandelbrot.IMIN));
				imagMax.setText(Double.toString(Mandelbrot.IMAX));
				iterationsSpin.setValue(Mandelbrot.DefaultIterations);
				
				mandbro.repaint();
				juliaGal.repaint();
			}
		});

		mandFractalPanel = new JPanel();
		mandFractalPanel.setPreferredSize(new Dimension(getWidth()/2, getHeight()));
		mandFractalPanel.setLayout(new BorderLayout());
		mainPanel.add(mandFractalPanel, BorderLayout.WEST);

		mandlePanel = new JPanel();
		mandlePanel.setLayout(new BorderLayout());
		mandFractalPanel.add(mandlePanel);

		juilaFractalPanel = new JPanel();
		juilaFractalPanel.setPreferredSize(new Dimension(getWidth()/2, getHeight()));
		juilaFractalPanel.setLayout(new BorderLayout());
		mainPanel.add(juilaFractalPanel, BorderLayout.EAST);

		juilaPanel = new JPanel();
		juilaPanel.setLayout(new BorderLayout());
		juilaFractalPanel.add(juilaPanel);

		this.addContent();
	}

	public void addContent(){
		// Adds a listener to the generate button
		generatebutton.addActionListener(new componentSetter());

		SelectedPointListener spl = new SelectedPointListener(selectedPoint);
		// Adds the Mandelbrot fractal to the GUI panel and adds the listeners
		mandlePanel.add(mandbro, BorderLayout.CENTER);
		mandlePanel.addMouseListener(spl);
		mandlePanel.addMouseMotionListener(spl);

		// Adds the Juila set fractal to the GUI but doesn't disply it till
		// the user has clicked on the Mandelbrot
		juilaPanel.add(juliaGal, BorderLayout.CENTER);
		juilaPanel.setVisible(false);

		mandbro.draw();
		mandbro.repaint();

		juliaGal.draw();
		juliaGal.repaint();

	} 

	class SelectedPointListener implements MouseListener, MouseMotionListener {

		private JTextField selectedPoint;
		private double xStep, yStep;

		public SelectedPointListener(JTextField selectedPoint){
			this.selectedPoint = selectedPoint;
		}

		public void mouseClicked(MouseEvent e) {

			xStep = (mandbro.getRealAxisTo() - mandbro.getRealAxisFrom()) / (double) mandbro.getWidth();
			yStep = (mandbro.getImaginaryAxisTo() - mandbro.getImaginaryAxisFrom()) / (double) mandbro.getHeight();

			// When the user clicks on the Mandelbrot, the Juila Set and the save button are displayed
			juilaPanel.setVisible(true);
			saveButton.setVisible(true);

			// Make a new complex number from the clicked on point to draw the Juila Set from
			pointToShow = (new ComplexNumber(mandbro.getRealAxisFrom() + e.getX() * xStep, mandbro.getImaginaryAxisFrom() + e.getY() * yStep));

			// DecimalFormat is to tindy up how the current points displayed look
			DecimalFormat deFormat = new DecimalFormat("#.###");
			// Displays the currently selected point of the Mandelbrot
			selectedPoint.setText(deFormat.format(pointToShow.getReal()) + ", " + deFormat.format(pointToShow.getImaginary()) + "i");

			userXPoint = pointToShow.getReal();
			userYPoint = pointToShow.getImaginary();
			currentIter = mandbro.getIterations();

			juliaGal.draw();
			juliaGal.repaint();
		}

		public void mousePressed(MouseEvent e) {

			xStep = (mandbro.getRealAxisTo() - mandbro.getRealAxisFrom()) / (double) mandbro.getWidth();
			yStep = (mandbro.getImaginaryAxisTo() - mandbro.getImaginaryAxisFrom()) / (double) mandbro.getHeight();

			// If they left click make the julia set with complex number from where they click
			if(e.getButton() != 1) {
				zooming = true;

				// Takes the X and Y points when the mouse is pressed to be able to com
				xPressed = Math.round( (mandbro.getRealAxisFrom() + e.getX() *xStep) * 10000000.0) / 10000000.0;
				yPressed = Math.round( (mandbro.getImaginaryAxisFrom() + e.getY() *yStep) * 10000000.0) / 10000000.0;

				zoomX = e.getX();
				zoomY = e.getY();
			}
		}
		public void mouseReleased(MouseEvent e) {

			xStep = (mandbro.getRealAxisTo() - mandbro.getRealAxisFrom()) / (double) mandbro.getWidth();
			yStep = (mandbro.getImaginaryAxisTo() - mandbro.getImaginaryAxisFrom()) / (double) mandbro.getHeight();

			//Zoom when the right button is released
			if(e.getButton() != 1){
				zooming = false;
				// Takes the X and Y points from release to be able to calulate the area to zooming in on
				xReleased = Math.round( (mandbro.getRealAxisFrom() + e.getX() *xStep) * 10000000.0) / 10000000.0;
				yReleased = Math.round( (mandbro.getImaginaryAxisFrom() + e.getY() *yStep) * 10000000.0) / 10000000.0;

				// Checks the X direction the mouse was moved in to be able to calulate the zoom
				if (xReleased > xPressed){
					mandbro.setrealAxisTo(xReleased);
					mandbro.setrealAxisFrom(xPressed);
					realMin.setText(Double.toString(xPressed));
					realMax.setText(Double.toString(xReleased));

				} else {
					mandbro.setrealAxisTo(xPressed);
					mandbro.setrealAxisFrom(xReleased);
					realMin.setText(Double.toString(xReleased));
					realMax.setText(Double.toString(xPressed));
				}

				// Checks the Y direction the mouse was moved in to be able to calulate the zoom
				if (yReleased > yPressed) {
					mandbro.setimaginaryAxisTo(yReleased);
					mandbro.setimaginaryAxisFrom(yPressed);
					imagMin.setText(Double.toString(yPressed));
					imagMax.setText(Double.toString(yReleased));
				} else {
					mandbro.setimaginaryAxisTo(yPressed);
					mandbro.setimaginaryAxisFrom(yReleased);
					imagMin.setText(Double.toString(yReleased));
					imagMax.setText(Double.toString(yPressed));
				}
				mandbro.repaint();
			}

		}
		public void mouseDragged(MouseEvent e) {

			if (liveActionButton.isSelected()){
				// Only with the left mouse button
				if(e.getButton() == 0){
					xStep = (mandbro.getRealAxisTo() - mandbro.getRealAxisFrom()) / (double) mandbro.getWidth();
					yStep = (mandbro.getImaginaryAxisTo() - mandbro.getImaginaryAxisFrom()) / (double) mandbro.getHeight();

					// When the user clicks on the Mandelbrot, the Juila Set and the save button are displayed
					juilaPanel.setVisible(true);
					saveButton.setVisible(true);

					pointToShow = (new ComplexNumber(mandbro.getRealAxisFrom() + e.getX() * xStep, mandbro.getImaginaryAxisFrom() + e.getY() * yStep));

					// DecimalFormat is to tindy up how the current points displayed look
					DecimalFormat deFormat = new DecimalFormat("#.###");
					// Displays the currently selected point of the Mandelbrot
					selectedPoint.setText(deFormat.format(pointToShow.getReal()) + ", " + deFormat.format(pointToShow.getImaginary()) + "i");

					userXPoint = pointToShow.getReal();
					userYPoint = pointToShow.getImaginary();
					currentIter = mandbro.getIterations();

					juliaGal.draw();
					juliaGal.repaint();
				}
			}
			
			// To be able to create the square when dragging for zooming
			if(e.getButton() != 1){
				curX = e.getX();
				curY = e.getY();
				mandbro.repaint();
			}
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
	}

	class componentSetter implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// Checks the value in the iteration spinner to be able to recalualte the number of iterations to draw with
			mandbro.setIterations((Integer)iterationsSpin.getValue());
			juliaGal.setIterations((Integer)iterationsSpin.getValue());

			// Checks the values of the real and imaginary axes to be able to redraw the Mandelbrot fractal 
			mandbro.setrealAxisFrom(Double.valueOf(realMin.getText()));
			mandbro.setrealAxisTo(Double.valueOf(realMax.getText()));
			mandbro.setimaginaryAxisFrom(Double.valueOf(imagMin.getText()));
			mandbro.setimaginaryAxisTo(Double.valueOf(imagMax.getText()));

			mandbro.repaint();
			juliaGal.repaint();
		}

	}


	public double getUserXPoint() {
		return userXPoint;
	}

	public double getUserYPoint() {
		return userYPoint;
	}

	public int getCurrntIter(){
		return currentIter;
	}

	public String pointToSave(){
		return savePoint;
	}

	public boolean getZooming(){
		return this.zooming;
	}

	public int getZoomX(){
		return this.zoomX;
	}

	public int getZoomY(){
		return this.zoomY;
	}

	public int getCurX(){
		return this.curX;
	}

	public int getCurY(){
		return this.curY;
	}
}
