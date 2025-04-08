#!/bin/bash

# mvn clean install
mvn clean install exec:java -Dexec.mainClass="ufsc.cco.security.App"