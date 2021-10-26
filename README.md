# EMS Datalog Histogram Generator

![Alt text](screenshots/histo.png?raw=true)

A CSV parser and 2D histogram generator designed to read exported datalogs from standalone car computers.
I have been using this to great effect with my AEM Infinity to street tune my S2000. Basically, I didn't want 
to pay $30 for MegaLogViewer.

## How to build
```
gradle clean build
```

## How to Run
```
gradle run
```
Alternatively, on Windows you can unzip build/distributions/datalog2histogram.zip after building and run 
**bin/datalog2histogram.bat**.