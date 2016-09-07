import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;

@SuppressWarnings("serial")
public class Mandelbrot extends JPanel {

	// The portion of the plane stretching from -2 to 2 in the Real Axis
	// and from -1.6 to 1.6 in the Imaginary Axis
	
	protected static final double RMIN = -2;
	protected static final double RMAX = 2;
	protected static final double IMIN = -1.6;
	protected static final double IMAX = 1.6;
	protected static final int DefaultIterations = 100;
	
	private double realAxisFrom = RMIN;
	private double realAxisTo = RMAX;
	private double imaginaryAxisFrom = IMIN;
	private double imaginaryAxisTo = IMAX;
	private int iterations = DefaultIterations;
	private FractalFrame fractalFrame;
	
	// The number of iterations that the fractal will be drawn with 
	private boolean draw = false;

	public Mandelbrot(FractalFrame fractalFrame){
		this.fractalFrame = fractalFrame;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (draw){
			
			// The lenght of the x axis plane divided by the width
			double xStep = (realAxisTo - realAxisFrom) / (double) getWidth();
			// The lenght of the y axis plane divided by the height
			double yStep = (imaginaryAxisTo - imaginaryAxisFrom) / (double)  getHeight();

			if(fractalFrame.burningShipButton.isSelected()){
				// Iterate over the width and height of the panel to draw the fractal
				for (int i = 0; i < getWidth(); i++){
					for(int j = 0; j <  getHeight(); j++){
						// To calulate the complex number for each point of the picture
						int sequence = getIterBurningShip(new ComplexNumber(realAxisFrom + i * xStep, imaginaryAxisFrom + j * yStep));
						// If the value of sequence equals the iterations value it colours it black
						// Else it will calulate the colour using the values of sequence and iterations
						g.setColor(sequence == iterations ? Color.BLACK : Color.getHSBColor(0.5f + sequence/(float)iterations, 1f, 1f));
						// Fill each point of a rectangle to get the picture of the fractal and surrounding area
						g.fillRect(i, j, 1, 1);
					}
				}
			} else
			// Iterate over the width and height of the panel to draw the fractal
			for (int i = 0; i < getWidth(); i++){
				for(int j = 0; j <  getHeight(); j++){
					// To calulate the complex number for each point of the picture
					int sequence = getIterations(new ComplexNumber(realAxisFrom + i * xStep, imaginaryAxisFrom + j * yStep));
					// If the value of sequence equals the iterations value it colours it black
					// Else it will calulate the colour using the values of sequence and iterations
					g.setColor(sequence == iterations ? Color.BLACK : Color.getHSBColor(0.5f + sequence/(float)iterations, 1f, 1f));
					// Fill each point of a rectangle to get the picture of the fractal and surrounding area
					g.fillRect(i, j, 1, 1);
				}
			}
		}
		
		if(fractalFrame.getZooming()){
			// Color of the zooming rectangle
			g.setColor(new Color(200,0,200,50));
			// Caluates the area that is selcted for zooming and draws a rectangle over that area
			int startX = Math.min(fractalFrame.getZoomX(), fractalFrame.getCurX());
			int startY = Math.min(fractalFrame.getZoomY(), fractalFrame.getCurY());
			int xDistance = Math.abs(fractalFrame.getZoomX() - fractalFrame.getCurX());
			int yDistance = Math.abs(fractalFrame.getZoomY() - fractalFrame.getCurY());
			g.fillRect(startX, startY, xDistance, yDistance);
		}
	}

	public int getIterations(ComplexNumber c) {
		//Makes a new complex number
		ComplexNumber z = new ComplexNumber();
		int value;
		for(value = 0; value < iterations; value++) {
			// If the modulus squared is > 4 do nothing
			if (z.modulusSquared() > 4){
				break;
			}
			// Square the new complex number and add the given complex number to it
			z = z.multiply(z).add(c);
		}
		return value;
	}
	
	public int getIterBurningShip(ComplexNumber c) {
		//Makes a new complex number
		ComplexNumber z = new ComplexNumber();
		int value;
		for(value = 0; value < iterations; value++) {
			// If the modulus squared is > 4 do nothing
			if (z.modulusSquared() > 4){
				break;
			}
			// Takes the abslute value
			z = z.abs();
			// Square the new complex number and add the given complex number to it
			z = z.multiply(z).add(c);
		}
		return value;
	}

	public void draw(){
		draw = true;
	}
	
	public void setIterations(int iterations){
		this.iterations = iterations;
	}
	
	public int getIterations() {
		return iterations;
	}

	public void setrealAxisFrom(double realAxisFrom){
		this.realAxisFrom = realAxisFrom;
	}

	public void setrealAxisTo(double realAxisTo){
		this.realAxisTo = realAxisTo;
	}

	public void setimaginaryAxisFrom(double imaginaryAxisFrom){
		this.imaginaryAxisFrom = imaginaryAxisFrom;
	}

	public void setimaginaryAxisTo(double imaginaryAxisTo){
		this.imaginaryAxisTo = imaginaryAxisTo;
	}

	public double getRealAxisFrom() {
		return realAxisFrom;
	}

	public double getRealAxisTo() {
		return realAxisTo;
	}

	public double getImaginaryAxisFrom() {
		return imaginaryAxisFrom;
	}

	public double getImaginaryAxisTo() {
		return imaginaryAxisTo;
	}
}



