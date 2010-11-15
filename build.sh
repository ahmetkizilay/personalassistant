#!/bin/bash
cd bin
jar cvf ../lib/PersonalAssistant.jar pa
cd ../lib
java -classpath PersonalAssistant.jar:mysql-connector-java-5.1.13-bin.jar pa.ToDoList --add -m "Hello"
