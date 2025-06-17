#!/bin/bash

# This file is runnable if you are on Linux or Mac or Windows-using-WSL.

# setting up the java classpath
export CLASSPATH=${CLASSPATH}:libs/json-20250107.jar:libs/javax.json-1.0.jar:.

# If you are on Windows-using-PowerShell you can set the classpath using:
# $env:CLASSPATH = "libs/*;."

# If you are on Windows-using-CommandPrompt you can set the classpath using:
# set CLASSPATH=" libs/*;."

# compile the java files
javac *.java

# run the tests
java CS1003P2Test

