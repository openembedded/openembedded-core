DESCRIPTION = "The Openmoko Appearance Editor"
SECTION = "openmoko/pim"
DEPENDS = "libmokoui2 gconf gtk+"
RDEPENDS = "libedata-cal openmoko-today2-folders"
PV = "0.1.0+svnr${SRCREV}"
PR = "r0"

inherit openmoko2 gtk-icon-cache 
