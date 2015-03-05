#!/bin/sh

SRC=$1/../../../netbeans/exquisitedatacorpse/dist/exquisitedatacorpse.jar
DST=$1/../processing-library/exquisitedatacorpse/library

if [ -d "$DST" ]; then
	rm -rf "$DST"
fi
mkdir "$DST"

cp "$SRC" "$DST"