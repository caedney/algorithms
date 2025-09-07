package algorithms.Exercises.Exercise_1_3;

import java.io.File;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.43
 * 
 * <p>
 * <i>Listing files</i>. A folder is a list of files and folders. Write a
 * program that takes the name of a folder as a command-line argument and prints
 * out all of the files contained in that folder, with the contents of each
 * folder recursively listed (indented) under that folder's name. <i>Hint</i>:
 * Use a queue, and see {@code java.io.File}.
 * </p>
 */
public class Exercise_1_3_43 {
    public static void listFiles(File file, int level) {
        for (int i = 0; i < level; i++)
            StdOut.print("    ");

        StdOut.println(file.getName());

        if (file.isDirectory()) {
            File[] filePaths = file.listFiles();

            for (File path : filePaths)
                listFiles(path, level + 1);
        }
    }

    public static void main(String[] args) {
        File file = new File("src/main/java/algorithms/Exercises");

        if (file.exists())
            listFiles(file, 0);
        else
            StdOut.println("The given path is not a valid folder");

    }
}
