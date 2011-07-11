#!/bin/sh
# usage: sh update-webkit.sh <revision>

export rev=$1

for i in autogen.sh configure.ac GNUmakefile.am Makefile Makefile.shared  ; do
	wget "http://trac.webkit.org/browser/trunk/$i?rev=$rev&format=raw" -O $i
done
