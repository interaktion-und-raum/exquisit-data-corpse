#!/bin/sh

LIBRARY=exquisit_data_corpse

find .. -name ".DS_Store" -print0 | xargs -0 rm -f
cd ../processing-library/
zip --quiet -r ../dist/$LIBRARY.zip ./$LIBRARY
cd ../dist/