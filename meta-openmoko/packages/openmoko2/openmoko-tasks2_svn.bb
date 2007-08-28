DESCRIPTION = "The OpenMoko Agenda"
SECTION = "openmoko/pim"
DEPENDS = "dbus-glib eds-dbus libmokoui2"
RDEPENDS = "libedata-cal"
RCONFLICTS = "tasks"
PV = "0.1.0+svn${SVNREV}"
PR = "r1"

inherit openmoko2

SRC_URI = "svn://svn.o-hand.com/repos/tasks/;module=trunk;proto=http"
S = "${WORKDIR}/trunk"

EXTRA_OECONF = "--enable-omoko --disable-gtk"
