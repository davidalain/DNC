/*
 * This file is part of the Disco Deterministic Network Calculator v2.2.6 "Hydra".
 *
 * Copyright (C) 2013 - 2016 Steffen Bondorf
 *
 * disco | Distributed Computer Systems Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de
 *
 *
 * The Disco Deterministic Network Calculator (DiscoDNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package unikl.disco.numbers;

import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;

import unikl.disco.numbers.NumFactory.SpecialValue;

/**
 * Wrapper class around org.apache.commons.math3.BigFraction.BigFraction
 * introducing special values like positive / negative infinity and NaN
 * as well as operators like min, max, ==, &gt;, &gt;=, &lt;, and &lt;= that are
 * not part of BigFraction but needed by the network calculator.
 * 
 * For the ease of converting from the primitive data type double
 * to BigFraction objects, copy by value semantic are is applied. 
 * 
 * @author Steffen Bondorf
 *
 */
public class NumBigFraction implements Num {
	private boolean isNaN, isPosInfty, isNegInfty;
	
	private BigFraction value = new BigFraction( 0.0 );
	
	// Unfortunately you cannot give the constructor the double value 0.0000001
	private static final BigFraction EPSILON = new BigFraction( 1, 1000000 ); 
	
	// BigFraction is based in Integer and thus there's no infinity (and BigFraction is prone to overflows as well)
	public static final NumBigFraction POSITIVE_INFINITY = createPositiveInfinity();
	public static final NumBigFraction NEGATIVE_INFINITY = createNegativeInfinity();
	public static final NumBigFraction NaN = createNaN();
	public static final NumBigFraction ZERO = createZero();
	
	private NumBigFraction(){}
	
	protected NumBigFraction( double value ) {
//		if (value == Double.NaN ) {
//			this.value = new BigFraction( 0.0 );
//			isNaN = true;
//		} else {
			this.value = new BigFraction( value );
			checkInftyNaN();
//		}
	}
	
	protected NumBigFraction( String num_str ) throws Exception {
		if( num_str.equals( "Infinity" ) ) {
			value = POSITIVE_INFINITY.value;
		} else {
			value = new BigFraction( NumFactory.parse( num_str ).doubleValue() );
			checkInftyNaN();
		}
	}
	
	protected NumBigFraction( int num ) {
		value = new BigFraction( num );
		checkInftyNaN();
	}
	
	protected NumBigFraction( int num, int den ) {
		value = new BigFraction( num, den );
		checkInftyNaN();
	}
	
	protected NumBigFraction( BigInteger num, BigInteger den ) {
		value = new BigFraction( num, den );
		checkInftyNaN();
	}
	
	protected NumBigFraction( NumBigFraction num ) {
		value = new BigFraction( num.value.getNumerator(), num.value.getDenominator() );
		checkInftyNaN();
	}
	
	private NumBigFraction( BigFraction frac ) {
		value = new BigFraction( frac.getNumerator(), frac.getDenominator() );
		checkInftyNaN();
	}
	
	protected NumBigFraction( SpecialValue indicator ) {
		switch (indicator) {
			case POSITIVE_INFINITY:
				instantiatePositiveInfinity();
				break;
			case NEGATIVE_INFINITY:
				instantiateNegativeInfinity();
				break;
			case NaN:
				instantiateNaN();
				break;
			case ZERO:
				instantiateZero();
				break;
		}
	}
	
	public boolean isNaN() {
		return isNaN;
	}
	
	public boolean isPosInfty() {
		return isPosInfty;
	}
	
	public boolean isNegInfty() {
		return isNegInfty;
	}
	
	private void checkInftyNaN() {
//		if( value.compareTo( NaN.value ) == 0 ) {
//			isNaN = true;
//			isPosInfty = false;
//			isNegInfty = false;
//			return;
//		}
//		if( value.compareTo( POSITIVE_INFINITY.value ) == 0 ) {
//			isNaN = false;
//			isPosInfty = true;
//			isNegInfty = false;
//			return;
//		}
//		if( value.compareTo( NEGATIVE_INFINITY.value ) == 0 ) {
//			isNaN = false;
//			isPosInfty = false;
//			isNegInfty = true;
//			return;
//		}
	}

	public static NumBigFraction createPositiveInfinity() {
		NumBigFraction pos_infty = new NumBigFraction();
		pos_infty.instantiatePositiveInfinity();
		return pos_infty;
	}
	
	private void instantiatePositiveInfinity() {
		value = new BigFraction( Integer.MAX_VALUE );
		isNaN = false;
		isPosInfty = true;
		isNegInfty = false;
	}

	public static NumBigFraction createNegativeInfinity() {
		NumBigFraction neg_infty = new NumBigFraction();
		neg_infty.instantiateNegativeInfinity();
		return neg_infty;
	}

	private void instantiateNegativeInfinity() {
		value = new BigFraction( Integer.MIN_VALUE );
		isNaN = false;
		isPosInfty = false;
		isNegInfty = true;
	}
	
	public static NumBigFraction createNaN() {
		NumBigFraction nan = new NumBigFraction();
		nan.instantiateNaN();
		return nan;
	}
	
	private void instantiateNaN() {
		value = new BigFraction( 0.0 ); // Double.NaN does not work
		isNaN = true;
		isPosInfty = false;
		isNegInfty = false;
	}
	
	public static NumBigFraction createZero() {
		NumBigFraction zero = new NumBigFraction();
		zero.instantiateZero();
		return zero;
	}
	
	private void instantiateZero() {
		value = new BigFraction( 0.0 );
		isNaN = false;
		isPosInfty = false;
		isNegInfty = false;
	}
	
	protected static NumBigFraction createEpsilon() {
        return new NumBigFraction( EPSILON );
	}
	
	// In order to simplify the transition from the primitive data type double to
	// a rational number object, these functions emulate copy by value for objects that
	// typically inhibit copy by reference
	protected static NumBigFraction add( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		// Prevent overflow exception when adding integer based number representations like BigFraction
		if( num1.isPosInfty() || num2.isPosInfty() ) {
			return createPositiveInfinity();
		}
		if( num1.isNegInfty() || num2.isNegInfty() ) {
			return createNegativeInfinity();
		}
		
		// May still throw MathArithmeticException due to integer overflow
        return new NumBigFraction( num1.value.add( num2.value ) );
	}
	
	@Override
	public void add( Num num2 ) {
		if( isNaN || num2.isNaN() ) {
			instantiateNaN();
			return;
		}
		// Prevent overflow exception when adding integer based number representations like BigFraction
		if( isPosInfty || num2.isPosInfty() ) {
			instantiatePositiveInfinity();
			return;
		}
		if( isNegInfty || num2.isNegInfty() ) {
			instantiateNegativeInfinity();
			return;
		}
		
		this.value = this.value.add( (BigFraction)((NumBigFraction)num2).value );
	}
	
	protected static NumBigFraction sub( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		// Prevent overflow exception when adding integer based number representations like BigFraction
		if( num1.isPosInfty() || num2.isPosInfty() ) {
			return createPositiveInfinity();
		}
		if( num1.isNegInfty() || num2.isNegInfty() ) {
			return createNegativeInfinity();
		}
		
        // May still throw MathArithmeticException due to integer overflow
        return new NumBigFraction( num1.value.subtract( num2.value ) );
	}

	@Override
	public void sub( Num num2 ) {
		if( isNaN || num2.isNaN() ) {
			instantiateNaN();
			return;
		}
		// Prevent overflow exception when adding integer based number representations like BigFraction
		if( isPosInfty || num2.isPosInfty() ) {
			instantiatePositiveInfinity();
			return;
		}
		if( isNegInfty || num2.isNegInfty() ) {
			instantiateNegativeInfinity();
			return;
		}
		
        // May throw MathArithmeticException due to integer overflow
		this.value =  this.value.subtract( (BigFraction)((NumBigFraction)num2).value );
	}
	
	protected static NumBigFraction mult( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		if( num1.isPosInfty() || num2.isPosInfty() ) {
			return createPositiveInfinity();
		}
		if( num1.isNegInfty() || num2.isNegInfty() ) {
			return createNegativeInfinity();
		}
		
        // May throw MathArithmeticException due to integer overflow
       	return new NumBigFraction( num1.value.multiply( num2.value ) );
	}

	@Override
	public void mult( Num num2 ) {
		if( isNaN || num2.isNaN() ) {
			instantiateNaN();
			return;
		}
		if( isPosInfty || num2.isPosInfty() ) {
			instantiatePositiveInfinity();
			return;
		}
		if( isNegInfty || num2.isNegInfty() ) {
			instantiateNegativeInfinity();
			return;
		}
		
        // May throw MathArithmeticException due to integer overflow
		this.value =  this.value.multiply( (BigFraction)((NumBigFraction)num2).value );
	}

	protected static NumBigFraction div( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		
		// Integer based number representations use Integer.MAX_VALUE to signal infinity so special treatment is necessary when dividing
		if( num1.isPosInfty() ) {
			return createPositiveInfinity();
		}
		if( num2.isPosInfty() ) {
			return createZero();
		}
		if( num1.isNegInfty() ) {
			return createNegativeInfinity();
		}
		if( num2.isNegInfty() ) {
			return createZero();
		}
		
        if ( num2.value.getNumerator().doubleValue() == 0 ) {
        	return createPositiveInfinity();
       	} else {
           	return new NumBigFraction( num1.value.divide( num2.value ) );        		
       	}
	}

	@Override
	public void div( Num num2 ) {
		if( isNaN || num2.isNaN() ) {
			instantiateNaN();
			return;
		}
		
		// Integer based number representations use Integer.MAX_VALUE to signal infinity so special treatment is necessary when dividing
		if( isPosInfty || ((BigFraction)((NumBigFraction)num2).value).getNumerator().doubleValue() == 0 ) {
			instantiatePositiveInfinity();
			return;
		}
		if( num2.isPosInfty() || num2.isNegInfty() ) {
			instantiateZero();
			return;
		}
		if( isNegInfty ) {
			instantiateNegativeInfinity();
			return;
		}
		
       	this.value =  this.value.divide( (BigFraction)((NumBigFraction)num2).value );        		
	}

	protected static NumBigFraction diff( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		
		if( num1.isPosInfty() || num1.isNegInfty() 
				 || num2.isPosInfty() || num2.isNegInfty() ) {
			return createPositiveInfinity();
		}
		
		return sub( max( num1, num2 ), min( num1, num2 ) );	
	}

	protected static NumBigFraction max( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		if( num1.isPosInfty() ) {
			return num1;
		}
		if( num1.isNegInfty() ) {
			return num2;
		}
		
		if( num1.value.compareTo( num2.value ) >= 0 ) {
			return num1;
		} else {
			return num2;
		}
	}

	protected static NumBigFraction min( NumBigFraction num1, NumBigFraction num2 ) {
		if( num1.isNaN() || num2.isNaN() ) {
			return createNaN();
		}
		if( num1.isPosInfty() ) {
			return num2;
		}
		if( num1.isNegInfty() ) {
			return num1;
		}
		
		if( num1.value.compareTo( num2.value ) <= 0 ) {
			return num1;
		} else {
			return num2;
		}
	}
	
	protected static NumBigFraction abs( NumBigFraction num ) {
		if ( num.isNaN() ) {
    		return createNaN();
    	}
    	if ( num.isPosInfty() ) {
    		return createPositiveInfinity();
    	}
    	if ( num.isNegInfty() ) {
			return createNegativeInfinity();
		}
	    
    	return new NumBigFraction( num.value.abs() );
	}

	protected static NumBigFraction negate( NumBigFraction num ) {
		if ( num.isNaN() ) {
    		return createNaN();
    	}
    	if ( num.isPosInfty() ) {
    		return createNegativeInfinity();
    	}
    	if ( num.isNegInfty() ) {
			return createPositiveInfinity();
		}
    	
    	return new NumBigFraction( num.value.negate() );
	}

	public boolean greater( Num num2 ) {
		if( isNaN || num2.isNaN() ){
			return false;
		}
		
		if( num2.isPosInfty() ){
			return false;
		}
		if( isPosInfty ){
			return true;
		}
		
		if( isNegInfty ){
			return false;
		}
		if( num2.isNegInfty() ){
			return true;
		}
		
		if( this.value.compareTo( ((NumBigFraction)num2).value ) > 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ge( Num num2 ) {
		if( isNaN || num2.isNaN() ){
			return false;
		}
		
		if( isPosInfty ){
			return true;
		}
		if( num2.isPosInfty() ){
			return false;
		}

		if( num2.isNegInfty() ){
			return true;
		}
		if( isNegInfty ){
			return false;
		}
		
		if( this.value.compareTo( ((NumBigFraction)num2).value ) >= 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean less( Num num2 ) {
		if( isNaN || num2.isNaN() ){
			return false;
		}

		if( isPosInfty ){
			return false;
		}
		if( num2.isPosInfty() ){
			return true;
		}
		
		if( num2.isNegInfty() ){
			return false;
		}
		if( isNegInfty ){
			return true;
		}
			
		if( this.value.compareTo( ((NumBigFraction)num2).value ) < 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean le( Num num2 ) {
		if( isNaN || num2.isNaN() ){
			return false;
		}

		if( num2.isPosInfty() ){
			return true;
		}
		if( isPosInfty ){
			return false;
		}

		if( isNegInfty ){
			return true;
		}
		if( num2.isNegInfty() ){
			return false;
		}
		
		if( this.value.compareTo( ((NumBigFraction)num2).value ) <= 0 ) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public double doubleValue() {
		if ( isNaN ) {
    		return Double.NaN;
    	}
    	if ( isPosInfty ) {
    		return Double.POSITIVE_INFINITY;
    	}
    	if ( isNegInfty ) {
			return Double.NEGATIVE_INFINITY;
		}
    	
	    return value.doubleValue();
	}

	@Override
	public Num copy() {
		if ( isNaN ) {
    		return createNaN();
    	}
    	if ( isPosInfty ) {
    		return createPositiveInfinity();
    	}
    	if ( isNegInfty ) {
			return createNegativeInfinity();
		}

		return new NumBigFraction( this.value.getNumerator(), this.value.getDenominator() );
	}
	
	@Override
	public boolean equals( double num2 ) {
		if( Double.isNaN( num2 ) ){
			return isNaN;
		}
		if( num2 == Double.POSITIVE_INFINITY ){
			return isPosInfty;
		}
		if( num2 == Double.NEGATIVE_INFINITY ){
			return isNegInfty;
		}
		
		return equals( new NumBigFraction( num2 ) );
	}

	public boolean equals( NumBigFraction num2 ) {
		if( isNaN & num2.isNaN() ){
			return true;
		}
		if( isPosInfty & num2.isPosInfty() ){
			return true;
		}
		if( isNegInfty & num2.isNegInfty() ){
			return true;
		}
        
		if( this.value.compareTo( num2.value ) == 0 ) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj == null ){
			return true;
		}
		
		NumBigFraction num2_Num;
		try {
			num2_Num = (NumBigFraction) obj;
			return equals( num2_Num );
		} catch( ClassCastException e ) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString(){
		if ( isNaN ) {
    		return "NaN";
    	}
    	if ( isPosInfty ) {
    		return "Infinity";
    	}
    	if ( isNegInfty ) {
			return "-Infinity";
		}
    	
		return value.toString();
	}
}
