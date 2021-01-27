#!/usr/bin/env bash
INPUT_FILE="$1"
OUTPUT_FILE="$2"
if [ -z "$OUTPUT_FILE" ]
then
	python ../lexical-analyser.py $INPUT_FILE
else
	python ../lexical-analyser.py $INPUT_FILE > $OUTPUT_FILE
fi
