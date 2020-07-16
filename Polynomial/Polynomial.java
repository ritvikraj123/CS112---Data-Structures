package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 *
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3
	 * </pre>
	 *
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc)
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 *
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/

		Node temp = null;
		Node result = null;

		while (poly1 != null && poly2 != null) {
			if (poly1.term.degree == poly2.term.degree) {//checks to see if degrees are equal
				if (result == null) {
					result = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree, null);
					temp = result;
				} else {
					temp.next = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree, null);
					temp = temp.next;
				}
				poly1 = poly1.next;
				poly2 = poly2.next;
			}

			if (poly2.term.degree > poly1.term.degree) {//checks to see if poly2 is grater than ooly1
				if (result == null) {
					result = new Node(poly1.term.coeff , poly1.term.degree, null);
					temp = result;
				} else {
					temp.next = new Node(poly1.term.coeff , poly1.term.degree, null);
					temp = temp.next;
				}
				poly1 = poly1.next;
			}

			if (poly1.term.degree > poly2.term.degree) {//checks to see if poly1 is greater than poly 2
				if (result == null) {
					result = new Node(poly2.term.coeff , poly2.term.degree, null);
					temp = result;
				} else {
					temp.next = new Node(poly2.term.coeff , poly2.term.degree, null);
					temp = temp.next;
				}
				poly2 = poly2.next;
			}

		}//end check if whiles

		while (poly1 != null && poly2 == null) {//first linked list
			if (result == null) {
				result = new Node(poly1.term.coeff , poly1.term.degree, null);
				temp = result;
			} else {
				temp.next = new Node(poly1.term.coeff, poly1.term.degree, null);
				temp = temp.next;
			}
			poly1 = poly1.next;
		}

		while (poly2 != null && poly1 == null) {//second linked list
			if (result == null) {
				result = new Node(poly2.term.coeff , poly2.term.degree, null);
				temp = result;
			} else {
				temp.next = new Node(poly2.term.coeff, poly2.term.degree, null);
				temp = temp.next;
			}
			poly2 = poly2.next;
		}


		return result;
	}






	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 *
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/

		
		Node tempPoly2 = poly2;
		Node temp = null;
		Node result = null;
		while (poly1 != null){
			while( poly2 != null){
				if (result == null){
					result = new Node(poly2.term.coeff * poly1.term.coeff, poly2.term.degree + poly1.term.degree, null);
					temp = result;
				}
				else{
					temp.next =  new Node(poly2.term.coeff * poly1.term.coeff, poly2.term.degree + poly1.term.degree, null);
					temp = temp.next;
				}
				poly2 = poly2.next;
			}//end inner while
			poly1 = poly1.next;
			poly2 = tempPoly2;

		}//end outer while
		return result;
	}


	/**
	 * Evaluates a polynomial at a given value.
	 *
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		//Node eval = poly;
		float endval=0;
		while (poly!=null){
			endval = endval + (poly.term.coeff*(float)Math.pow(x, poly.term.degree));
			poly = poly.next;
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		return endval;
	}

	/**
	 * Returns string representation of a polynomial
	 *
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
