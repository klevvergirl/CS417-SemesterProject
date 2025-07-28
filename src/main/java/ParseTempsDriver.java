import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import static edu.odu.cs.cs417.TemperatureParser.CoreTempReading;
import static edu.odu.cs.cs417.TemperatureParser.parseRawTemps;
import static edu.odu.cs.cs417.Operations.PieceWise;
import static edu.odu.cs.cs417.Operations.LeastSquares;
import static edu.odu.cs.cs417.Operations.CubicSpline;

/**
 * A simple command line test driver for TemperatureParser.
 */
public class ParseTempsDriver {

    /**
     * The main function used to demonstrate the TemperatureParser class.
     *
     * @param args used to pass in a single filename
     */
    public static void main(String[] args)
    {
        BufferedReader tFileStream = null;
        String filename = "";

        // Parse command line argument 1
        try {
            tFileStream = new BufferedReader(new FileReader(new File(args[0])));
            Path path = Paths.get(args[0]);
            filename = path.getFileName().toString();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("A file path must be provided.");
            System.exit(1);
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not locate a file with the supplied file path.");
            System.exit(1);
        }

        List<CoreTempReading> allTheTemps = parseRawTemps(tFileStream);

        for (CoreTempReading aReading : allTheTemps) {
            System.out.println(aReading);
        }

        //----------------------------------------------------------------------
        // Split into separate arrays
        //----------------------------------------------------------------------
        final int numberOfReadings = allTheTemps.size();
        final int numberOfCores = allTheTemps.get(0).readings.length;

        int[] times = new int[numberOfReadings];
        double[][] coreReadings = new double[numberOfCores][numberOfReadings];

        for (int lineIdx = 0; lineIdx < numberOfReadings; ++lineIdx) {
            for (int coreIdx = 0; coreIdx < numberOfCores; ++coreIdx) {
                times[lineIdx] = allTheTemps.get(lineIdx).step;
                coreReadings[coreIdx][lineIdx] = allTheTemps.get(lineIdx).readings[coreIdx];
            }
        }

        //----------------------------------------------
        // Output times alongside each core to console
        // Create a new file for each core
        // Write each 
        //   -piece-wise interpolation, 
        //   -least squares approximation, 
        //   -cubic spline 
        // to the file.
        //----------------------------------------------
        System.out.println();

        //------------------------------------------
        // Declare variables to write to file
        //------------------------------------------
        StringBuilder stringbuilder = null;
        FileWriter WriteToCore = null;
        PieceWise PWbuilder = null;
        LeastSquares LSbuilder = null;

        for (int coreIdx = 0; coreIdx < numberOfCores; ++coreIdx) {

            System.out.printf("Core # %2d%n", coreIdx);

            //----------------------------------------------
            // Create a new file and name it in the format
            // {input file name}-core-0{core number}.txt
            //----------------------------------------------
            String moddedfilename = filename.replace(".txt", "-core-0" + coreIdx + ".txt" );
            File core = new File(moddedfilename);
            
            try{
                if (core.createNewFile()){
                    System.out.println("File " + core.getName() + " created.");
                }
                else{
                    System.out.println("File already exists.");
                }
            }
            catch(IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            //----------------------------------------------
            // Print the times and readings to the console
            // Write each equation to a string builder
            //----------------------------------------------
            try{
                WriteToCore = new FileWriter(core); 
                stringbuilder = new StringBuilder();  
            }
            catch(IOException e){
                System.out.println("Failed to write to file.");
                e.printStackTrace();
            }

            for (int i = 0; i < times.length; ++i) {
                System.out.printf("%8d -> %5.2f%n", times[i], coreReadings[coreIdx][i]);
                
                if(i != times.length - 1){
                    PWbuilder = new PieceWise(times[i], times[i+1], coreReadings[coreIdx][i], coreReadings[coreIdx][i+1]);
                    stringbuilder.append(PWbuilder.calculate());
                }
            }

            LSbuilder = new LeastSquares(times[0], times[times.length - 1], coreReadings[coreIdx][0], coreReadings[coreIdx][times.length - 1]);
            stringbuilder.append(LSbuilder.calculate());

            System.out.println();

            //----------------------------------------------------
            // Write each equation to the file and close it
            //----------------------------------------------------

            try{
                WriteToCore.write(stringbuilder.toString());
                WriteToCore.close();
            }
            catch(IOException e){
                System.out.println("Failed to close file.");
                e.printStackTrace();  
            }
        }
    }
}
