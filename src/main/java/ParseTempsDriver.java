import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import static edu.odu.cs.cs417.TemperatureParser.CoreTempReading;
import static edu.odu.cs.cs417.TemperatureParser.parseRawTemps;

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

        // Parse command line argument 1
        try {
            tFileStream = new BufferedReader(new FileReader(new File(args[0])));
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

        //----------------------------------------------------------------------
        // Output times alongside each core
        //----------------------------------------------------------------------
        System.out.println();
        for (int coreIdx = 0; coreIdx < numberOfCores; ++coreIdx) {
            System.out.printf("Core # %2d%n", coreIdx);

            for (int i = 0; i < times.length; ++i) {
                System.out.printf("%8d -> %5.2f%n", times[i], coreReadings[coreIdx][i]);
            }

            System.out.println();
        }
    }
}
