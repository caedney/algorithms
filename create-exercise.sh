#!/bin/bash
# Script: create-exercise.sh
# Usage:
# ./create-exercise.sh <number> <total>
#
# Example:
# → ./create-exercise.sh 1_1 50
# → Creates Exercise_1_1_1.java … Exercise_1_1_50.java

# Check argument count
if [ $# -ne 2 ]; then
  echo "Usage: $0 <number> <total>"
  exit 1
fi

NUMBER=$1
TOTAL=$2

# Ensure destination exists
DESTINATION="./app/src/main/java/algorithms/Exercises/Exercise_$NUMBER"
mkdir -p "$DESTINATION"

# Create files
for ((i=1; i<=TOTAL; i++)); do
  FILE="$DESTINATION/Exercise_${NUMBER}_${i}.java"

  if [ -e "$FILE" ]; then
    echo "File '$FILE' already exists, skipping..."
  else
    touch $FILE
    cat > $FILE <<-EOF
	package algorithms.Exercises.Exercise_$NUMBER;

	/**
	 * Exercise ${NUMBER//_/.}.$i
	 * 
	 * <p>
	 * </p>
	 */
	public class Exercise_${NUMBER}_${i} {
	    public static void main(String[] args) {
	    }
	}
	EOF
    echo "Created '$FILE'"
  fi
done
