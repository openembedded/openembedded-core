DESCRIPTION = "The Openmoko Command Line Terminal"
SECTION = "openmoko/applications"
DEPENDS = "vala-native vte libmokoui2"
RDEPENDS = "liberation-fonts"
PV = "3.0.0+svnr${SRCREV}"
PR = "r2"

inherit openmoko2
