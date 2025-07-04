import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;

import static edu.odu.cs.cs417.TemperatureParser.CoreTempReading;
import static edu.odu.cs.cs417.TemperatureParser.parseRawTemps;
import static edu.odu.cs.cs417.Operations.PLInterpolation;
import static edu.odu.cs.cs417.Operations.LSApproximation;

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
        File inputFile = new File(args[0]);
        
        // Parse command line argument 1
        try {
            tFileStream = new BufferedReader(new FileReader(inputFile));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // TBW
        }
        catch (FileNotFoundException e) {
            // TBW
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

        System.out.println();
        
        //----------------------------------------------------------------------
        // Create output file header based on input file name
        //----------------------------------------------------------------------
        
        StringBuilder head = new StringBuilder();
        
        head.append(inputFile.getName())
        	.delete(head.length() - 4, head.length())
        	.append("-core-");
        
        //----------------------------------------------------------------------
        // 1. Output times alongside each core
        // 2. Create an output file for each core
        // 3. Perform a Piecewise Linear Interpolation on the input data
        // 4. Write PLI to core's output text
        // 5. Perform a Least Squares Approximation on the input Data
        // 6. Write LSA to core's output text
        // 7. Perform [optional]
        // 8. Write [optional] to core's output text
        //----------------------------------------------------------------------
              
        for (int coreIdx = 0; coreIdx < numberOfCores; ++coreIdx) {
   
            System.out.printf("Core # %2d%n", coreIdx); //Step 1

            //Step 2
            StringBuilder bld = new StringBuilder();
            
            bld.append(head)
               .append(coreIdx)
               .append(".txt");
            
            FileWriter coreWriter = new FileWriter(new File(bld.toString()));
            String writable = PLInterpolation(coreReadings[coreIdx]);
            		
            for (int i = 0; i < times.length; ++i) {
                System.out.printf("%8d -> %5.2f%n", times[i], coreReadings[coreIdx][i]);
                
            }
            

            
            System.out.println();
        }
    }
}
