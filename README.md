# TemperatureParser

Parses an input text file with time-stepped temperature readings of a multi-core CPU and processes
the information as follows:

    1. A piecewise linear interpolation.
    2. A global linear least squares approximation.
    3. (Optional) A cubic spline (or other non-linear) interpolation.

Then outputs the processed data into another text file.


## Input

The program is intended to be run from the command line like this:

`java` _absolute-path-to-ParseTempsDriver.jar_ _absolute-path-to-InputFile.txt_ ...

where

* _absolute-path-to-ParseTempsDriver.jar_ is the path to the jar file containing the compiled program.
* _absolute-path-to-InputFile.txt_ ... are paths to documents in plain text (ASCII) that will be read for processing.

Multiple text files have been provided in the directory `testData`.

## Output

The program should read the provided input documents, preprocess the data into seperate arrays, and then print a report for each core to a text file describing the temperature readings and the interpolations / approximations listed above. Each report will contain at least n - 1 interpolations, where n is the number of time steps in the input file, and exactly one approximation.

Each line in each report must take the form:

$x_{k} \le x < x_{k+1} ; y_{i} = C_{0} + \left(C_{1} * x\right)$ ; type

Where $x_{k}$ and $x_{k+1}$ are the domain in which $y_{k}$ is applicable, 
$y_{k}$ is the $k^{th}$ function, 
and 'type' is either least-squares or interpolation

## Example

The file `sample-input.txt` contains:

```

61.0 63.0 50.0 58.0
80.0 81.0 68.0 77.0
62.0 63.0 52.0 60.0
83.0 82.0 70.0 79.0
68.0 69.0 58.0 65.0

```

If the program is built with the provided makefile and run as

    java -cp ParseTempsDriver.jar testData\sample-input.txt 

the output would be:

```
We would end up with four output files

sample-input-core-00.txt 
       0 <= x <=       30 ; y =      61.0000 +       0.6333 x ; interpolation
      30 <= x <=       60 ; y =      98.0000 +      -0.6000 x ; interpolation
      60 <= x <=       90 ; y =      20.0000 +       0.7000 x ; interpolation
      90 <= x <=      120 ; y =     128.0000 +      -0.5000 x ; interpolation
       0 <= x <=      120 ; y =      67.4000 +       0.0567 x ; least-squares
sample-input-core-01.txt 
       0 <= x <=       30 ; y =      63.0000 +       0.6000 x ; interpolation
      30 <= x <=       60 ; y =      99.0000 +      -0.6000 x ; interpolation
      60 <= x <=       90 ; y =      25.0000 +       0.6333 x ; interpolation
      90 <= x <=      120 ; y =     121.0000 +      -0.4333 x ; interpolation
       0 <= x <=      120 ; y =      69.0000 +       0.0433 x ; least-squares
sample-input-core-02.txt 
       0 <= x <=       30 ; y =      50.0000 +       0.6000 x ; interpolation
      30 <= x <=       60 ; y =      84.0000 +      -0.5333 x ; interpolation
      60 <= x <=       90 ; y =      16.0000 +       0.6000 x ; interpolation
      90 <= x <=      120 ; y =     106.0000 +      -0.4000 x ; interpolation
       0 <= x <=      120 ; y =      56.0000 +       0.0600 x ; least-squares
sample-input-core-03.txt 
       0 <= x <=       30 ; y =      58.0000 +       0.6333 x ; interpolation
      30 <= x <=       60 ; y =      94.0000 +      -0.5667 x ; interpolation
      60 <= x <=       90 ; y =      22.0000 +       0.6333 x ; interpolation
      90 <= x <=      120 ; y =     121.0000 +      -0.4667 x ; interpolation
       0 <= x <=      120 ; y =      64.6000 +       0.0533 x ; least-squares

```
