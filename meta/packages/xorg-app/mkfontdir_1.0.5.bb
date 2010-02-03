require xorg-app-common.inc

DESCRIPTION = "a program to create an index of X font files in a directory"

PE = "1"

RDEPENDS += "mkfontscale"

BBCLASSEXTEND = "native"
