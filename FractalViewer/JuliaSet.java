import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JuliaSet extends JPanel {
	
	protected double realAxisFrom = -2;
	protected double realAxisTo = 2;
	protected double imaginaryAxisFrom = -1.6;
	protected double imaginaryAxisTo = 1.6;

	protected int iterations = 100;
	private boolean draw = false;

	protected ComplexNumber pointToShow;
	protected FractalFrame window;

	//This method is the same as in the Mandelbrot class except for what the getIterations() does
	public void paintComponent(Graphics g) {
		if (draw){
			double xStep = (realAxisTo - realAxisFrom) / (double) getWidth();
			double yStep = (imaginaryAxisTo - imaginaryAxisFrom) / (double) getHeight();

			for (int i = 0; i < getWidth(); i++){
				for(int j = 0; j < getHeight(); j++){
					int sequence = getIterations(new ComplexNumber(realAxisFrom + i * xStep, imaginaryAxisFrom + j * yStep));
					g.setColor(sequence == iterations ? Color.BLACK : Color.getHSBColor(0.5f + sequence/(float)iterations, 1f, 1f));
					g.fillRect(i, j, 1, 1);
				}
			}
		}
	}

	public int getIterations(ComplexNumber d){
		// Makes a new complex number with the X and Y points that the user clicked on
		ComplexNumber c = new ComplexNumber(window.getUserXPoint(), window.getUserYPoint());
		int sequence;
		for(sequence = 0; sequence < iterations; sequence++){
			if (d.modulusSquared() > 4){
				break;
			}
			// Square the given complex number and add the new complex number
			// that was made by the user's click
			d = d.square().add(c);
		}
		return sequence;
	}

	public void draw(){
		draw = true;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public void setIterations(int iterations){
		this.iterations = iterations;
	}
	
	public JuliaSet(FractalFrame window) {
		this.window = window;
	}
	
	public void updateUserXY(ComplexNumber pointToShow){
		this.pointToShow = pointToShow;
	}
}