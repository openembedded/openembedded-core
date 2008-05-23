DESCRIPTION = "The Openmoko Agenda"
SECTION = "openmoko/pim"
DEPENDS = "dbus-glib eds-dbus libmokoui2"
RDEPENDS = "libedata-cal"
PV = "0.1.0+svnr${SRCREV}"
PR = "r1"

inherit openmoko2

SRC_URI = "svn://svn.o-hand.com/repos/tasks/;module=trunk;proto=http \
           file://openmoko-tasks.desktop \
           file://openmoko-tasks.png"
S = "${WORKDIR}/trunk"

EXTRA_OECONF = "--enable-omoko --disable-gtk"

do_install_append() {
	install -d ${D}${datadir}/applications
	install -m 0644 ${WORKDIR}/openmoko-tasks.desktop ${D}${datadir}/applications/tasks.desktop
	install -d ${D}${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/openmoko-tasks.png ${D}${datadir}/pixmaps
}

