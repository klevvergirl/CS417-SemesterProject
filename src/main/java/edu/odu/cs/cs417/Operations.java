package edu.odu.cs.cs417;

import java.lang.Math;

public class Operations
{
    public static class PieceWise{

       private int currentTime;

       private int nextTime;

       private double currentReading;

       private double nextReading;

       private double multiplier;

       private double adder;


        /** 
         * Set all variables to the provided values
         * 
         * @param curr The current reading's timestamp
         * @param next The next reading's timestamp
         * @param currR The current reading
         * @param nextR The next reading
         */
        public PieceWise(int curr, int next, double currR, double nextR){
            this.currentTime = curr;
            this.nextTime = next;
            this.currentReading = currR;
            this.nextReading = nextR;
        }

        /**
         * Calculate the values of adder and multiplier to build an equation. 
         * Then return a string for the equation at the specified time interval
         * in the format:
         * 
         * @return buildString()
         */
        public String calculate(){

            multiplier = (nextReading - currentReading) / (nextTime - currentTime);
            adder = currentReading - (multiplier * currentTime);

            return buildString();
        }

        /**
         * Build a string in the format:
         * 
         *       currentTime <= x <= nextTime; y = adder + multiplier(x); type
         * 
         * Where - 
         *       * currentTime is the current reading's timestamp
         *       * nextTime is the next reading's timestamp
         *       * multiplier = (nextReading - currentReading) / (nextTime - currentTime);
         *       * adder = currentReading - (multiplier * currentTime);
         *       * type is 'interpolation'
         * 
         * @return bld.toString()
         */
        private String buildString(){

            StringBuilder bld = new StringBuilder();
            
            bld.append(currentTime)
               .append(" <= x <= ")
               .append(nextTime).append("; y = ")
               .append(String.format("%10.4f", adder))
               .append(" + ")
               .append(String.format("%10.4f", multiplier))
               .append("x; interpolation \n");

            return bld.toString();
        }
    }

    public static class LeastSquares{

        private int firstTime;

        private int lastTime;

        private double firstReading;

        private double lastReading;

        private double adder;

        private double multiplier;


        /** 
        * Set all variables to the provided values
        * 
        * @param first The first reading's timestamp
        * @param last The last reading's timestamp
        * @param fRead The first reading
        * @param lRead The last reading
        */
        public LeastSquares(int first, int last, double fRead, double lRead){
            this.firstTime = first;
            this.lastTime = last;
            this.firstReading = fRead;
            this.lastReading = lRead;
        }

        /**
        * Calculate the values of adder and multiplier to build an equation. 
        *
        *@return The String built by buildString()
        */
        public String calculate(){

            //-----------------------------------------------------
            // Initialize a set of matrices to perform
            // the XTX|XTY method for Least Squares Approximation
            //-----------------------------------------------------
            double[][] X = {
                { 1 , firstTime },
                { 1 , lastTime  }
            };

            double[][] XT = {
                {     1   ,     1   },
                {firstTime, lastTime}
            };
            double[] Y = 
                { firstReading,
                  lastReading  };

            double[][] XTX = new double[2][2];
            double[]   XTY = new double[2];
            double[] Gaussed = new double[2];

            //------------------------------------------------------------
            //Matrix multiply XT and X and store the result in resultant
            //------------------------------------------------------------
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 2; j++){
                    for(int k = 0; k < 2; k++){
                        XTX[i][j] += XT[i][k] * X[k][j];
                    }
                }
            }

            //------------------------------------------------------------
            //Matrix multiply XT and Y and store the result in resultant
            //------------------------------------------------------------
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 1; j++){
                    for(int k = 0; k < 2; k++){
                        XTY[i] += XT[i][k] * Y[k];
                    }
                }
            }

            //---------------------------------------------------------
            //Perform Gaussian Elimination, take the resultant values
            //and store them in adder and multiplier
            //---------------------------------------------------------
            Gaussed = GaussElim(XTX, XTY);

            adder = Gaussed[0];
            multiplier = Gaussed[1];

            return buildString();
        }

        /**
         * An algorithm for performing Gaussian Elimination with partial pivoting
         * graciously provided by Robert Sedgewick and Kevin Wayne of Princeton University.
         * 
         * @param XTX A two dimensional array equivalent to the left hand side of an augmented matrix
         * @param XTY A single dimension array equivalent to the right hand side of an augmented matrix
         * 
         * @return A single dimension array of the resultant values 
         * equivalent to the right hand side of an augmented matrix after elimination has been performed.
         */

        public static double[] GaussElim(double[][] XTX, double[] XTY) {
            int n = XTY.length;

            for (int p = 0; p < n; p++) {

                //------------------------------
                // Find the pivot row and swap
                //------------------------------
                int max = p;
                for (int i = p + 1; i < n; i++) {
                    if (Math.abs(XTX[i][p]) > Math.abs(XTX[max][p])) {
                        max = i;
                    }
                }

                double[] temp = XTX[p]; XTX[p] = XTX[max]; XTX[max] = temp;
                double   t    = XTY[p]; XTY[p] = XTY[max]; XTY[max] = t;

                //---------------------------
                // Pivot within XTX and XTY
                //---------------------------
                for (int i = p + 1; i < n; i++) {
                    double alpha = XTX[i][p] / XTX[p][p];
                    XTY[i] -= alpha * XTY[p];
                    for (int j = p; j < n; j++) {
                        XTX[i][j] -= alpha * XTX[p][j];
                    }
                }
            }

            //------------------------------
            // Perform back substitution
            //------------------------------
            double[] resultant = new double[n];

            for (int i = n - 1; i >= 0; i--) {
                double sum = 0.0;
                for (int j = i + 1; j < n; j++) {
                    sum += XTX[i][j] * resultant[j];
                }
                resultant[i] = (XTY[i] - sum) / XTX[i][i];
            }

            return resultant;
        }

        /**
        * Build an equation in the format:
        * 
        *@return firstTime <= x <= lastTime; y = adder + multiplier(x); type
        * 
        * Where - 
        *       * firstTime is the first reading's timestamp
        *       * lastTime is the last reading's timestamp
        *       * multiplier = 
        *       * adder = 
        *       * type is 'least squares'
        */
        private String buildString(){

            StringBuilder bld = new StringBuilder();

            bld.append(firstTime)
               .append(" <= x <= ")
               .append(lastTime).append("; y = ")
               .append(String.format("%10.4f", adder))
               .append(" + ")
               .append(String.format("%10.4f", multiplier))
               .append("x; least squares \n");

            return bld.toString();
        }
    }
}