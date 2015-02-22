#!/bin/sh

SRC=$1/../../../netbeans/exquisit-data-corpse/dist/exquisit_data_corpse.jar
DST=$1/../processing-library/exquisit_data_corpse/library

if [ -d "$DST" ]; then
	rm -rf "$DST"
fi
mkdir "$DST"

cp "$SRC" "$DST"