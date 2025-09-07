#!/bin/bash
# Script: create-files.sh
# Usage:
# ./create-files.sh <destination> <base_name> <total>
#
# Example:
# ./create_files.sh /app/java/algorithms/Exercises/Excercise_1_3 Excercise_1_3 50
# → Creates Exercise_1_3_1.java … Exercise_1_3_50.java

# Check argument count
if [ $# -ne 3 ]; then
  echo "Usage: $0 <destination> <base_name> <total>"
  exit 1
fi

DESTINATION=$1
BASENAME=$2
TOTAL=$3

# Ensure destination exists
mkdir -p "$DESTINATION"

# Create files
for ((i=1; i<=TOTAL; i++)); do
  FILE="$DESTINATION/${BASENAME}_${i}.java"
  if [ -e "$FILE" ]; then
    echo "File '$FILE' already exists, skipping..."
  else
    touch $FILE
    cat > $FILE <<-EOF
	package algorithms.Exercises.Exercise_1_3;

	/**
	 * Exercise 1.3.$i
	 * 
	 * <p>
	 * </p>
	 */
	public class Exercise_1_3_$i {
	    public static void main(String[] args) {
	    }
	}
	EOF
    echo "Created '$FILE'"
  fi
done
