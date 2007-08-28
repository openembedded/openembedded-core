DESCRIPTION = "The OpenMoko Application Launcher"
SECTION = "openmoko/pim"
DEPENDS = "libmokoui2 libmokojournal2 startup-notification dbus-glib libice libsm"
RDEPENDS = "libedata-cal openmoko-today2-folders"
PV = "0.1.0+svn${SVNREV}"
PR = "r1"

inherit openmoko2 gtk-icon-cache 
