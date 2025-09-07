#!/bin/bash

# Base directory (default: current directory)
DIR="${1:-.}"

# Check if directory exists
if [ ! -d "$DIR" ]; then
    echo "Error: '$DIR' is not a valid directory."
    exit 1
fi

echo "Scanning: $DIR"

# First, rename directories (depth-first)
find "$DIR" -depth -type d -iname "*excercise*" -print0 | while IFS= read -r -d '' d; do
    newd=$(echo "$d" | sed 's/[Ee]xcercise/Exercise/g')
    echo "Renaming directory: $d -> $newd"
    mv "$d" "$newd"
done

# Then, rename files
find "$DIR" -type f -iname "*excercise*" -print0 | while IFS= read -r -d '' f; do
    newf=$(echo "$f" | sed 's/[Ee]xcercise/Exercise/g')
    echo "Renaming file: $f -> $newf"
    mv "$f" "$newf"
done

echo "Renaming complete."