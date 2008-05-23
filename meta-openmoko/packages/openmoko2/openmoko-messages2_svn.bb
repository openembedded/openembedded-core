DESCRIPTION = "The Openmoko Message application"
SECTION = "openmoko/pim"
DEPENDS = "libmokogsmd2 libmokoui2 libmokojournal2 libjana"
PV = "0.1.0+svnr${SRCREV}"
PR = "r1"

inherit openmoko2

EXTRA_OECONF = "--with-dbusbindir=${STAGING_BINDIR_NATIVE}"

FILES_${PN} += "${datadir}/openmoko-messages/ ${datadir}/dbus-1/services/"
