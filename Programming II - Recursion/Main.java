/*
Carson Seese - CIT260-02 - Assignment 4: Recursion
04-13-19

This program demonstrate 6 common examples of recursion:
	1. Calculating the result of a number to an exponent
	2. Calculating the sum of numbers
	3. Ackermann's Function (as described in the textbook)
	4. Printing 1 to a number
	5. Printing a number to 1
	6. Reversing the characters of a string
*/

public class Main {

    public static void main(String[] args) {
		/*
		14-7: Recursive Power Method
		      Write a method that uses recursion to raise a number to a power.
		      The method should accept two arguments: The number to be raised, and the exponent.
		      Assume the exponent is a non-negative integer.


		      Flows as follows:

		      n = 5, e = 3  <  n = 5, e = 2  <  n = 5, e = 1  <  n = 5, e = 0
		      (Returns 125)    (Returns 25)     (Returns 05)     (Returns 01)
		                                                         (Base Case)

		 */
		System.out.println("======================================================================");
		System.out.println("14-7: Recursive Power Method\nInput: 5^3\nOutput: " + power(5, 3));
	    System.out.println("======================================================================");

		/*
		14-8: Sum of Numbers
		      Write a method that accepts an integer argument and returns the sum of all the integers from 1 up to the number passed as an argument.
		      For example, if 50 is passed as an argument, the method will return the sum of 1, 2, 3, 4...50.
		      Use recursion to calculate the sum.


		      Flow as follows:

		          n = 5    <    n = 4    <    n = 3    <    n = 2    <    n = 1
		      (Returns 15)  (Returns 10)  (Returns 06)  (Returns 03)  (Returns 01)
		                                                              (Base Case )
		 */
	    System.out.println("\n\n======================================================================");
		System.out.println("14-8: Sum of Numbers\nInput: 5\nOutput: " + sumOfNumbers(5));
	    System.out.println("======================================================================");

		/*
		14-9: Ackermann's Function
		      Ackermann's function is a recursive mathematical algorithm that can be used to test how well a computer performs recursion.
		      Write a method ackermann(m,n) that solves Ackermann's function.
		      Use the following logic in your method:
		      If m = 0, then return n+1
		      If n = 0, then return ackermann(m - 1, 1)
		      Otherwise, return ackermann(m - 1, ackermann(m, n - 1)

		      Test your method in a program that displays the return values of the following method calls:

		      ackermann(0, 0)   ackermann(0, 1)     ackermann(1, 1)     ackermann(1, 2)
		      ackermann(1, 3)   ackermann(2, 2)     ackermann(3, 2)
		 */
	    System.out.println("\n\n======================================================================");
		System.out.println("14-9: Ackermann's Function"
							+ "\nInput: (0, 0)\nOutput: " + ackermann(0, 0)
							+ "\n\nInput: (0, 1)\nOutput: " + ackermann(0, 1)
							+ "\n\nInput: (1, 1)\nOutput: " + ackermann(1, 1)
							+ "\n\nInput: (1, 2)\nOutput: " + ackermann(1, 2)
							+ "\n\nInput: (1, 3)\nOutput: " + ackermann(1, 3)
							+ "\n\nInput: (2, 2)\nOutput: " + ackermann(2, 2)
							+ "\n\nInput: (3, 2)\nOutput: " + ackermann(3, 2));
	    System.out.println("======================================================================");

		/*
		HackerRank: Print 1 to N Recursively
					Sample input: 5
					Sample output: 1 2 3 4 5

					Sample input: 10
					Sample output: 1 2 3 4 5 6 7 8 9 10
		 */
	    System.out.println("\n\n======================================================================");
	    System.out.print("HackerRank: Print 1 to N\nInput: 10\nOutput: ");
	    printOneToN(10, 1);
	    System.out.println("\n======================================================================");

		/*
		HackerRank: Print N to 1 Recursively
					Sample input: 5
					Sample output: 5 4 3 2 1

					Sample input: 10
					Sample output: 10 9 8 7 6 5 4 3 2 1
		 */
	    System.out.println("\n\n======================================================================");
	    System.out.print("HackerRank: Print N to 1\nInput: 10\nOutput: ");
	    printNtoOne(10);
	    System.out.println("\n======================================================================");

		/*
		HackerRank: Recursively Reverse a String
					Sample input: Being
					Sample output: gnieB
		 */
		System.out.println("\n\n======================================================================");
		System.out.println("HackerRank: Reverse a String\nInput: \"Being\"\nOutput: " + reverseString("Being"));
	    System.out.println("======================================================================");
    }

	/**
	 * Takes the input string and reverses the characters recursively (e.g. s = "Being", Return = "gnieB")
	 * @param s Input string
	 * @return The reversed input string
	 */
    public static String reverseString(String s) {
    	//Base case - If the length of string is 0, return the string
		if (s.length() == 0)
			return s;

		//Return the reverse of everything but the first character + the last character
		return reverseString(s.substring(1)) + s.charAt(0);
    }

	/**
	 * Takes the input integer and counts up to 1, printing the output to the console recursively (e.g. n = 5, Console: 5 4 3 2 1)
	 * @param n Input value
	 * @return The final value (1)
	 */
	public static int printNtoOne(int n) {
    	//Base case - If the number is 0, return 1 finishing the recursion
    	if (n == 0)
    		return 1;

    	System.out.print(n + " ");

    	//Return the number - 1
    	return printNtoOne(n-1);
    }

	/**
	 * Takes the input integer and starting point, printing the output to the console recursively (e.g. n = 5, c = 1, Console: 1 2 3 4 5)
	 * @param n Input value
	 * @param c Starting value (1)
	 * @return The input value
	 */
	public static int printOneToN(int n, int c) {
    	//If the count is less than or equal to the end number, print out the current value. Return n and increase the count
    	if (c <= n) {
    		System.out.print(c + " ");
	        return printOneToN(n, c+1);
	    }
    	//Base case - If c > n, return n to finish the recursion
    	return n;
    }

	/**
	 * Takes two input integers, performs the ackermann function on them, and returns the result (e.g. m = 1, n = 2, Return = ackermann(1, 2) = 4)
	 * @param m Input 1
	 * @param n Input 2
	 * @return The output of ackermann(m, n) after the function terminates
	 */
	public static int ackermann(int m, int n) {
    	//Base case - If m = 0, return n + 1
    	if (m == 0)
    		return n + 1;

    	//If n = 0, return the result of ackermann(m - 1, 1)
    	if (n == 0)
    		return ackermann(m-1, 1);

    	//If both m and n are not 0, return the result of ackermann(m - 1, ackermann(m, n - 1))
    	return ackermann(m-1, ackermann(m,n-1));
    }

	/**
	 * Takes a single integer, sums the values (1 to n), and returns the result (e.g. n = 5, Return = 5 + 4 + 3 + 2 + 1 = 15)
	 * @param n Input value
	 * @return The summed output
	 */
    public static int sumOfNumbers(int n) {
    	//Base case - If the number is 0, return the number itself to finish the recursion
    	if (n == 0)
    		return n;

    	//Return the number plus the sum of the number - 1
    	return n + sumOfNumbers(n-1);
    }

	/**
	 * Takes a number and it's exponent and recursively calculates the result (e.g. n = 5, e = 3, Return = 125)
	 * @param n Number
	 * @param e Exponent
	 * @return n^e
	 */
	public static int power(int n, int e) {
    	//Base case - If the exponent is 0, return 1 to finish the recursion
    	if (e == 0)
    		return 1;

    	//Return the given number times the given number to the e-1
    	return n * power(n, e-1);
    }
}
