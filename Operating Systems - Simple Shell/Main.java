/*
Carson Seese - 09-09-2019 - Main.java
CIT344 Assignment 1: Simple Shell

Notes: I could've kept this very basic, but I wanted to experiment with writing a command multiplexer. Hopefully that does
not go beyond the scope of the assignment. To put it simply, each possible command (cd, ls, cat, etc) is added to a map
of BiFunctions which are called when a command is entered via the commands.handle() method. These functions are supplied
via parameters to the commands.register function in the form of lambda functions. (p, c) -> {} with 'p' being a
reference to the process builder and 'c' being a string array of the commands and arguments entered.
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    /**
     * Tracks the current directory of the shell. For use with the ProcessBuilder and "cd" command
     */
    private static String currentDirectory;

    /**
     * Contains the user's home directory for when the "cd" command is executed
     */
    private static String userHome;

    /**
     * Boolean flag to indicate whether the system is Windows or not. Necessary when executing commands with ProcessBuilder
     */
    private static boolean isWindows = false;

    public static void main(String[] args) {
        //Initialization of the command handler
        Commands commands = new Commands();

        //Determine the current user's home directory and set the global variable. Set home to "/" if property is blank.
        if (!System.getProperty("user.home").isEmpty()) userHome = System.getProperty("user.home");
        else userHome = "/";

        //Set currentDirectory to the calling directory of the application
        currentDirectory = System.getProperty("user.dir");

        //Determine the OS so that the appropriate prefix can be applied to the command
        if (System.getProperty("os.name").toLowerCase().contains("windows")) isWindows = true;

        //Register the "pwd" command
        commands.register("pwd", (c, p) -> {
            String out = "";
            try {
                //Execute the command (passing the ProcessBuilder, isWindows boolean, and the command)
                out = commands.execute(p, isWindows, c[0]);
            } catch (IOException e) {
                out = "Unable to execute command due to an IOException.";
            }
            return out;
        });

        //Register the "exit" command
        commands.register("exit", (c, p) -> {
            //Kill the process
            System.exit(0);
            return "";
        });

        //Register the "ls" command
        commands.register("ls", (c, p) -> {
            //Since ls accepts additional parameters, build a string of these parameters
            String cmdString = String.join(" ", c);

            String out = "";
            try {
                //Execute the command (passing the ProcessBuilder, isWindows boolean, and the built command string)
                out = commands.execute(p, isWindows, cmdString);
            } catch (IOException e) {
                out = "Unable to execute command due to an IOException.";
            }
            return out;
        });

        //Register the "cd" command
        commands.register("cd", (c, p) -> {
            //Since cd accepts additional parameters, build a string of these parameters
            String cmdString = String.join(" ", c);

            //Ensure there is only one argument for the cd command
            if (c.length > 2) return String.format("Too many arguments for command: %s", c[0]);

            //If only "cd" is entered, take user to their home directory
            if (c.length == 1) {
                currentDirectory = userHome;
                return "";
            }

            //If "cd [arg]" is entered, change directories
            //Initialize the cd object to take advantage of it's utility methods
            Cd cd = new Cd();

            //Check to make sure the path is actually a directory. If not, inform the user
            if(!cd.checkDirectory(currentDirectory, c[1])) return String.format("%s is not a valid directory", c[1]);

            //Set the current directory to the new directory
            currentDirectory = cd.changeDirectory(currentDirectory, c[1]);

            return "";
        });

        //Register the "cat" command
        commands.register("cat", (c, p) -> {
            //Since cat accepts additional parameters, build a string of these parameters
            String cmdString = String.join(" ", c);

            //Check to make sure any arguments of cat are actually files
            for(int i = 1; i < c.length; i++) {
                File f = new File(currentDirectory + "\\" + c[i]);
                if(!(f.exists() && f.isFile())) {
                    return String.format("%s is not a file.", c[i]);
                }
            }

            String out = "";
            try {
                //Execute the command (passing the ProcessBuilder, isWindows boolean, and the built command string)
                out = commands.execute(p, isWindows, cmdString);
            } catch (IOException e) {
                out = "Unable to execute command due to an IOException.";
            }
            return out;
        });

        //Call the run method after all the commands are initialized
        run(commands);
    }

    /**
     * Initializes the processBuilder and starts the infinite loop until "exit" is executed.
     * @param commands A commands object with all the registered commands used to handle command execution.
     */
    public static void run(Commands commands) {
        //Initialization of the process builder
        ProcessBuilder processBuilder = new ProcessBuilder();

        //Loop the shell "interface" forever (or until System.exit() is called)
        while(true) {
            //Prompt the user for input
            System.out.printf("CIT344 %s> ", currentDirectory);

            //Set the directory to the current directory every time a command is executed
            processBuilder.directory(new File(currentDirectory));

            //Handle the input with the command handler
            System.out.print(commands.handle(new Scanner(System.in).nextLine(), processBuilder) + "\n");
        }
    }
}
