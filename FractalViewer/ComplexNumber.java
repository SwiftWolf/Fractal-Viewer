import java.text.DecimalFormat;

public class ComplexNumber {

	private double real, imaginary;

	//  Constructs the complex number z = x + yi, when we need f(z) = z^2 + c

	public ComplexNumber(double real,double imaginary){
		this.real = real;
		this.imaginary = imaginary;
	}

	public ComplexNumber(){
		this(0, 0);
	}

	// A complex number is made up of a real part and imaginary part
	public ComplexNumber(ComplexNumber comlxNum){ 
		this(comlxNum.real, comlxNum.imaginary); 
	}

	// Getter for the real part of a complex number
	public double getReal() {
		return real;
	}

	// Getter for the imaginary part of a complex number  
	public double getImaginary() {
		return imaginary;
	}

	// Complex number z = x + yi, where x is not squared
	public ComplexNumber notSquared(){
		return new ComplexNumber (real + imaginary, imaginary); 
	}
	
	// Complex number that is squared
	public ComplexNumber square() {
		// z^2 =(x + yi)^2 = x^2 - 2xyi + yi^2
		// i^2 = -1
		return new ComplexNumber ((real*real) - (imaginary*imaginary), (2*real*imaginary)); 
	}
	
	// Modulus squared of a Complex number 
	public double modulusSquared(){
		return ((real*real) + (imaginary*imaginary));
	}

	// Squared Complex number 
	public double modulus(){
		return Math.sqrt(this.modulusSquared());
	}
	
	// Complex number addtion, real + real, imaginary + imaginary
	public ComplexNumber add(ComplexNumber d) {
		return new ComplexNumber((d.getReal() + real), (d.getImaginary() + imaginary));
	}
	
	// Multiply a complex number
	public ComplexNumber multiply(ComplexNumber x) {
		return new ComplexNumber(
			(real * x.real) - (imaginary * x.imaginary), (real * x.imaginary) + (imaginary * x.real));
	}
	
	// Override equals() so that it handles complex numbers properly 
	public boolean equals(ComplexNumber c){
		return this.getReal() == c.getReal() && this.getImaginary() == c.getImaginary();
	}
	
	// Returns the absolute value of the complex number
	public ComplexNumber abs() {
		return new ComplexNumber(Math.abs(this.real), Math.abs(this.imaginary));
	}
	
	// Handles the desimal format of a complex number
	public String toString(){
		DecimalFormat deFormat = new DecimalFormat("#.###");
		return deFormat.format(this.getReal()) + " , " + deFormat.format(this.getImaginary());
	}
}

	

