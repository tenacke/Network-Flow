import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tester {
    private final String dirName; // this is the directory of the inputs
    private final String realName; // this is the directory of the outputs that you want to compare
    private final String outName; // this is the directory that you want to store your outputs
//    private final String dirName = "updated_test_cases\\";
//    private final String realName = "updated_test_cases\\";
//    private final String outName = "my_outputs\\";
    private final String[] inputs;
    private final String[] outputs;
    // PLEASE CHECK THE VARIABLE NAMES ABOVE AND EDIT IF NECESSARY
    // ALL THESE DIRECTORIES MUST EXIST! IF YOU DON'T HAVE THIS DIRECTORIES PLEASE MAKE THEM
    // IF YOU GET A FILENOTFOUNDEXCEPTION ABOUT NOT EXISTING FILE TRY DELETING .DS_Store FILE
    // BELOW THIS LINE DO NOT CHANGE ANYTHING EXCEPT THE LINES 18-19

    public static void main(String[] args) throws IOException {
        String dirName = "emre_inputs\\";
        String realName = "emre_outputs\\";
        String outName = "my_outputs\\";
        Tester tester = new Tester(dirName, realName, outName);
        tester.run(); // you can toggle this to execute the inputs
        tester.compare(); // you can toggle this to compare yourputs (outName) with some outputs (realName)
    }

    public Tester(String dirName, String realName, String outName) {
        this.dirName = dirName;
        this.realName = realName;
        this.outName = outName;
        File inDir = new File(dirName);
        File[] inputFiles = Objects.requireNonNull(inDir.listFiles());
        inputs = new String[inputFiles.length];
        outputs = new String[inputFiles.length];
        int i = 0;
        for (File input : inputFiles) {
            String fileName = input.getName();
            if (fileName.startsWith("input")){
                inputs[i] = fileName;
                outputs[i++] = "output" + fileName.substring(5);
            }
        }
        if (new File(outName).mkdir())
            System.out.println("Output directory created!");
    }

    private void run() throws IOException {
        for (int i = 0; i < inputs.length; i++) {
            long start = System.nanoTime();
            project5.main(new String[]{dirName + inputs[i], outName + outputs[i]});
            long end = System.nanoTime();
            System.out.println(inputs[i] + " is executed in " + String.format("%,.8f", (double) (end - start)/1000000000L) + " seconds");
        }
    }

    private void compare() throws IOException {
        for (String output : outputs) {
            File realOutput = new File(realName + output);
            File myOutput = new File(outName + output);

            int line = filesCompareByLine(realOutput, myOutput);
            if (line == -2) {
                System.out.println(output + " : True");
            } else if (line == -1) {
                System.out.println(output + " : Only flow is true");
            } else {
                System.out.println(output + " : False. Wrong line is " + line);
            }
        }
    }

    private static int filesCompareByLine(File path1, File path2) throws IOException {
        BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1), StandardCharsets.UTF_8));
        BufferedReader bf2 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), StandardCharsets.UTF_8));
        Set<String> lines = new HashSet<>();

        int lineNumber = 1;
        boolean isTrue = false;
        String line1 = bf1.readLine();
        String line2 = bf2.readLine();
        if (Objects.equals(line1, line2))
            isTrue = true;
        lineNumber++;
        while ((line1 = bf1.readLine()) != null){
            lines.add(line1.strip());
        }
        while ((line2 = bf2.readLine()) != null){
            line2 = line2.strip();
            if (lines.contains(line2))
                lineNumber++;
            else {
                if (isTrue)
                    return -1;
                return lineNumber;
            }
        }
        if (isTrue) {
            if (bf2.readLine() != null)
                return -1;
            return -2;
        }
        return lineNumber;
    }
}
