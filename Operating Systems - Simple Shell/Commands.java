/*
Carson Seese - 09-09-2019 - Commands.java
CIT344 Assignment 1: Simple Shell
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.BiFunction;

public class Commands {
    /**
     * The HashMap containing the list of functions indexed by their command string
     */
    private HashMap<String, BiFunction> commandsMap = new HashMap<>();

    /**
     * Registers new commands by adding them to the HashMap.
     * @param command A string representation of the command to handle. E.g.: "cd", "ls", etc.
     * @param function A parameterized BiFunction including the logic to execute when a command string is entered
     */
    public void register(String command, BiFunction<String[], ProcessBuilder, String> function) {
        commandsMap.put(command, function); //Add the command to the map
    }

    /**
     * Calls the BiFunction associated with a command string whenever a command is received.
     * @param command A string containing the command itself and any additional arguments.
     * @param processBuilder A ProcessBuilder used to execute the command on the host system.
     * @return The output from the command. Informs the user if the command is not found.
     */
    public String handle(String command, ProcessBuilder processBuilder) {
        String[] commandParts = command.split(" "); //Split the string at whitespaces

        //Try to call the function and return the output.
        String out;
        try {
            out = commandsMap.get(commandParts[0]).apply(commandParts, processBuilder).toString();
        } catch (NullPointerException e) {
            //Inform the user if there is no command
            out =  String.format("Command %s not found.", commandParts[0]);
        }
        return out;
    }

    /**
     * Executes the specified command string as a helper for the command BiFunctions
     * @param processBuilder The specific ProcessBuilder used to execute the command.
     * @param isWindows A boolean flag indicating if the system is a windows machine or not. Used to determine the prefix of the command.
     * @param command A string containing the command to execute along with any arguments to include.
     * @return A string of the output of the command.
     * @throws IOException
     */
    public String execute(ProcessBuilder processBuilder, boolean isWindows, String command) throws IOException {
        //Use the isWindows boolean to determine how to prefix the command
        if (isWindows) processBuilder.command("bash", "-c", command);
        else processBuilder.command(command);

        //Initialize StringBuilder to build the output string
        StringBuilder out = new StringBuilder();
        //Initialize the process by starting the ProcessBuilder
        Process ps = processBuilder.start();

        //Initialize the buffered reader to read the output
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));

        //If there was a bash error, return stderr
        String line;
        while((line = br.readLine()) != null) {
            out.append(line + "\n");
        }
        br.close();

        //Return the output
        return out.toString();
    }
}
