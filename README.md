# EMS Datalog Histogram Generator

![Alt text](screenshots/histo.png?raw=true)

A CSV parser and 2D histogram generator designed to read exported datalogs from standalone car computers.
I have been using this to great effect with my AEM Infinity to street tune my S2000. Basically, I didn't want 
to pay $30 for MegaLogViewer.

## Requirements
1. Java 8 or newer
2. Gradle (if you want to build source)

## How to build
```
gradle clean build
```

## How to run
```
gradle run
```
Alternatively, on Windows you can unzip *distributions/datalog2histogram.zip* and run 
*bin/datalog2histogram.bat*.

## Algorithm Overview
As rows get read from the datalog, the x and y values must get "binned", which means figuring out which cell the data
should be included in. Depending on how close the x and y values are to the center of the cell, the data will get 
counted more or less.

### Example:
Let's assume our table has RPM on the x-axis and MAP on the y-axis. Our RPM breakpoints are 0, 1000, 2000. Our MAP
breakpoints are 0, 10, 20.

If we read a row from the CSV that has (RPM=1000, MAP=10, value=60), the cell at (RPM=1000, MAP=10) will get updated
with the full value 60.

If we instead read a row from the CSV that has (RPM=1250, MAP=12.5, value=60), the cell at (RPM=1000, MAP=10) will 
get updated with 50% of 60, or 30. The x and y values are shifted to halfway between the center of the cell and 
the border of the cell so only half the value is counted.

Finally, if we read a row from the CSV that has (RPM=1500, MAP=15, value=60), no cell will get updated, since the x
and y values both fall directly on the borders between cells.

The closer a hit to the center of a cell, the higher the "Hit accuracy" will be and the more the value will get counted
towards the final average.

## Final Words

If you feel like giving back to me, you are always welcome to

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/donate?business=53J7RAATF9L2U&no_recurring=0&currency_code=USD)

Enjoy!
