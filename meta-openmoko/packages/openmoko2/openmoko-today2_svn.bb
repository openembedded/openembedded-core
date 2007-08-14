require openmoko.inc

DESCRIPTION = "OpenMoko Today application"
SECTION = "openmoko/pim"
DEPENDS += "startup-notification dbus-glib libice libsm libmokoui2"
REAL_PN = "openmoko-today2"

inherit gtk-icon-cache 
