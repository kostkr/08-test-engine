#!/usr/bin/env bash
-rm -r classes/*
javac -d classes $(find . -name "*.java")
java -cp classes uj.wmii.pwj.anns.MyTestEngine uj.wmii.pwj.anns.MyBeautifulTestSuite