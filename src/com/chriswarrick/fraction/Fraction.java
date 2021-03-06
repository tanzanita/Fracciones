/*
 * Fraction v0.1.0
 * Copyright © 2016-2017, Chris Warrick.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author of this software nor the names of
 *    contributors to this software may be used to endorse or promote
 *    products derived from this software without specific prior written
 *    consent.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.chriswarrick.fraction;
import java.util.Scanner;

/**
 * A fraction, represented in numerator/denominator form.
 */
public class Fraction {
    /**
     * The numerator (top) of the fraction.
     */
    private int numerator;
    /**
     * The denominator (bottom) of the fraction.
     */
    private int denominator;

    /**
     * Create a fraction with the value 0.
     */
    public Fraction() {
        setValue(0, 1);
    }

    /**
     * Create a new fraction.
     * @param numerator the numerator
     * @param denominator the denominator
     */
    public Fraction(int numerator, int denominator) {
        setValue(numerator, denominator);
    }

    /**
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Set the value of this fraction.
     * @param numerator the new numerator
     * @param denominator the new denominator
     */
    public void setValue(int numerator, int denominator) {
        // Support negative numbers.
        if (denominator == 0) {
            throw new java.lang.ArithmeticException("Denominator cannot be zero.");
        } else if (denominator < 0) {
            // put - sign to numerator
            numerator = -numerator;
            denominator = -denominator;
        }
        // All fractions have to be simplified. To do that, we need to find the GCD of two numbers and divide.
        int gcd = Fraction.gcd(Math.abs(numerator), Math.abs(denominator));

        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    // Operations

    /**
     * Add a fraction to this fraction.
     * @param b The fraction to add.
     * @return A new fraction object that is the result of this + b.
     */
    public Fraction add(Fraction b) {
        return addByValue(b.numerator, b.denominator);
    }

    /**
     * Subtract a fraction from this fraction.
     * @param b The fraction to subtract.
     * @return A new fraction object that is the result of this - b.
     */
    public Fraction subtract(Fraction b) {
        return addByValue(-b.numerator, b.denominator);
    }

    /**
     * Multiply two fractions together.
     * @param b The fraction to multiply this by.
     * @return New fraction object that is the result of this * b.
     */
    public Fraction multiply(Fraction b) {
        return new Fraction(this.numerator * b.numerator, this.denominator * b.denominator);
    }

    /**
     * Divide this by another fraction.
     * @param b The fraction to divide this by.
     * @return New fraction object that is the result of this / b, or this * (1 / b).
     */
    public Fraction divide(Fraction b) {
        return new Fraction(this.numerator * b.denominator, this.denominator * b.numerator);
    }

    // Helper functions

    /**
     * Return this fraction as a string.
     * @return numerator/denominator
     */
    @Override
    public String toString() {
        return toString(numerator, denominator);
    }

    /**
     * Return a fraction of numerator/denominator as a string.
     * @param numerator The numerator.
     * @param denominator The denominator.
     * @return numerator/denominator
     */
    private String toString(int numerator, int denominator) {
        return String.format("%d/%d", numerator, denominator);
    }

    /**
     * Return the fraction as a string, that may contain a mixed number.
     * @return numerator/denominator or integral numerator/denominator
     */
    public String toMixed() {
        if (Math.abs(numerator) >= denominator) {
            // Get out integer part
            int i = numerator / denominator;
            int n = numerator % denominator;
            String s = java.lang.Integer.toString(i);
            s += " ";
            if (n != 0) {
                s += toString(Math.abs(n), denominator);
            }
            return s;
        } else if (numerator == 0) {
            return "0";
        } else {
            return toString();
        }
    }

    /**
     * Print the fraction and mixed number versions of this object.
     */
    public void printBoth() {
        System.out.printf("%s == %s\n", toString(), toMixed());
    }

    /** Determine the Least Common Multiple (LCM) of two integers
     * @param a First integer
     * @param b Second integer
     * @return LCM(a, b)
     */
    public static int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    /** Determine the Greatest Common Divisor (GCD) of two integers (Euclid’s Algorithm)
     * @param a First integer
     * @param b Second integer
     * @return GCD(a, b)
     */
    public static int gcd(int a, int b) {
        int r;
        while (b > 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    /**
     * Add a fraction by value.
     * @param bn Numerator of the fraction to add.
     * @param bd Denominator of the fraction to add.
     * @return A new fraction object that is the result of this + bn/bd.
     */
    private Fraction addByValue(int bn, int bd) {
        int an = this.numerator;
        int ad = this.denominator;

        // Figure out the new denominator and the factors to get there.
        int nd = Fraction.lcm(ad, bd);
        int ax = nd/ad;
        int bx = nd/bd;

        int nn = (an * ax) + (bn * bx);

        return new Fraction(nn, nd);
    }

    /**
     * Run a console demo interface.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n, d = 0;
        try {
            while (true) {
                System.out.println("Type in two fractions:");
                n = s.nextInt();
                d = s.nextInt();

                Fraction a = new Fraction(n, d);
                a.printBoth();
                n = s.nextInt();
                d = s.nextInt();
                Fraction b = new Fraction(n, d);
                b.printBoth();

                Fraction c;

                System.out.print("Operation to perform (+-*/): ");
                String op = s.next();
                switch (op) {
                    case "+":
                        c = a.add(b);
                        break;
                    case "-":
                        c = a.subtract(b);
                        break;
                    case "*":
                        c = a.multiply(b);
                        break;
                    case "/":
                        c = a.divide(b);
                        break;
                    default:
                        System.out.println("Unknown operation!");
                        c = new Fraction(0, 1);
                }
                c.printBoth();
                System.out.print("\n---\n");
            }
        } catch (java.lang.ArithmeticException ex) {
            System.out.println(ex);
            s.close();
            System.exit(1);
        }
    }
}
