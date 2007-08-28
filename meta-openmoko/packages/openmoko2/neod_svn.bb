DESCRIPTION = "Simple Neo1973 Daemon for Button Handling and Power Management"
SECTION = "openmoko/daemons"
DEPENDS = "gconf gtk+ pulseaudio"
PV = "0.1.0+svn${SVNREV}"
PR = "r0"

inherit openmoko2 gconf
