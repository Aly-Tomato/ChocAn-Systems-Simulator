import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * ChocAnSystem is a wrapper that allows ChocAn to access the different systems
 * built by Group Five to verify that each subsystem is correct.
 * 
 * @author  Group Five
 * @version 1.0
 * @since   2018-11-01 
 */
public class ChocAnSystem {
	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		int subsystemToEnter;

		System.out.println();
		System.out.println("Welcome to ChocAn's Data Processing System.");
		System.out.println("What subsystem would you like to use? (Please enter the arabic subsystem number)");
		System.out.println();
		System.out.println("\t0) Terminate this program");
		System.out.println("\t1) Providing and Billing a Member for a Service");
		System.out.println("\t2) Report Generator");
		System.out.println("\t3) Operator Mode");

		while(true) {
			System.out.println();
			System.out.print("> ");
			subsystemToEnter = inputScanner.nextInt();

			switch(subsystemToEnter) {
				case 0:
					System.out.println("Goodbye");
					System.out.println();
					inputScanner.close();
					return;
				case 1:
					runSubsystem1(inputScanner);
					break;
				case 2:
					runSubsystem2(inputScanner);
					break;
				case 3:
					runSubsystem3(inputScanner);
					break;
				default:
					System.out.println("The input of [" + subsystemToEnter + "] is not valid; try again.");
					break;
			}
		}
	}

	private static void runSubsystem1(Scanner inputScanner) {
		System.out.println("Loading: Providing and Billing a Member for a Service");
		// TODO: put the caller code here
	}

	private static void runSubsystem2(Scanner inputScanner) {
		System.out.println("Loading: Report Generator");
		// TODO: put the caller code here
	}

	private static void runSubsystem3(Scanner inputScanner) {
		System.out.println("Loading: Operator Mode");
		// TODO: put the caller code here
	}
}

