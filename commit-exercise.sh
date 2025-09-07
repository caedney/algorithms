#!/bin/bash
# Script: commit-exercise.sh
# Usage:
# ./commit-exercise.sh <number>
#
# Example:
# → ./commit-exercise.sh 1_4
# → Commits all files in the directory

# Check argument count
if [ $# -ne 1 ]; then
  echo "Usage: $0 <number>"
  exit 1
fi

NUMBER=$1
DIR="./app/src/main/java/algorithms/Exercises/Exercise_$NUMBER"

# Loop through each file inside it
for file in $(ls "$DIR" | sort -n -t _ -k 4); do
    path="$DIR/$file"

    # Make sure it’s a regular file
    if [ -f "$path" ]; then
        number=$(basename "$file" | sed -E 's/Exercise_?([0-9_]+)\.java/\1/' | tr '_' '.')
        echo $number
        git add "$path"
        git commit -m "feat: add exercise $number"
    else
        echo $path
    fi
done