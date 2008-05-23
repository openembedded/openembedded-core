DESCRIPTION = "The Openmoko Command Line Terminal"
SECTION = "openmoko/applications"
DEPENDS = "vala-native vte libmokoui2"
RDEPENDS = "ttf-liberation-mono"
PV = "3.0.0+svnr${SRCREV}"
PR = "r1"

inherit openmoko2
