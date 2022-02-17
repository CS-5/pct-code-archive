/*
Carson Seese - 09-09-2019 - Cd.java
CIT344 Assignment 1: Simple Shell

Notes: This separate class isn't really necessary, but I wanted "cd" to work properly which required some additional
logic, taking up more space than I'd like in Main.java
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cd {

    /**
     * Constant to define a backslash
     */
    private final String SLASH = "\\";

    /**
     * Checks to make sure the supplied path is a directory.
     * @param cd The current directory
     * @param dd The destination direction
     * @return True if the path is to a directory. False if not.
     */
    public boolean checkDirectory(String cd, String dd) {
        //Make a new file with the absolute path of the supplied path (in the form of the current and destination directories
        File f = new File(getAbsolute(cd, dd));

        //If the file exists and is a directory, return true
        if(f.exists() && f.isDirectory()) return true;
        return false;
    }

    /**
     * Gets the absolute path by first checking if the destination directory is an absolute path.
     * If the destination directory is an absolute path, return it. If it's not, add it to the current directory and return.
     * @param cd The current directory
     * @param dd The destination directory
     * @return The absolute path
     */
    private String getAbsolute(String cd, String dd) {
        //Make a new file with the current directory and destination directory (assuming the destination directory isn't absolute)
        File f = new File(cd + SLASH + dd);

        //If the destination directory is absolute, set File d to the path of the destination directory and ignore the current directory
        if(new File(dd).isAbsolute()) {
            f = new File(dd);
        }

        //Return the absolute path
        return f.getAbsolutePath();
    }

    /**
     * Despite the name, changeDirectory only returns the path built from the current directory and the destination directory.
     * This value is then used to set the currentDirectory variable.
     * @param cd The current directory
     * @param dd The destination directory
     * @return The path built from the current directory and the destination directory.
     */
    public String changeDirectory(String cd, String dd) {
        //Get the absolute path using the getAbsolute method
        String absPath = getAbsolute(cd, dd);

        //Initialize list of path elements
        List<String> pathElements = new ArrayList<>();

        //Iterate through the parts of the path split at "/" or "\". If a part contains ".." remove the previous element from the path.
        //Example: If you're currently in C:\\test\example and you want to move to test, you'd enter "cd ..". The path would look like
        //C:\\test\example\.. which works, but doesn't look so nice, so this loop removes "example". Turning the path into: C:\\test.
        for(String p : absPath.split("\\\\|\\/")) {
            if(p.equals("..")) {
                pathElements.remove(pathElements.size()-1);
                continue;
            }
            pathElements.add(p);
        }

        //After the loop exits, return the path by joining the elements of the List with "\"
        return String.join(SLASH, pathElements);
    }
}
