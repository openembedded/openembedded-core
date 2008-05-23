DESCRIPTION = "The Openmoko Application Launcher"
SECTION = "openmoko/pim"
DEPENDS = "libmokoui2 libmokojournal2 libjana startup-notification dbus-glib libice libsm"
RDEPENDS = "libedata-cal openmoko-today2-folders"
PV = "0.1.0+svnr${SRCREV}"
PR = "r2"

inherit openmoko2 gtk-icon-cache 
