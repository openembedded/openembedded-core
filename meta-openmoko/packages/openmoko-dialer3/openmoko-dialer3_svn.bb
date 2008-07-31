DESCRIPTION = "The Openmoko Dialer"
SECTION = "openmoko/pim"
HOMEPAGE = "http://projects.openmoko.org/projects/shr/"
PKG_TAGS_${PN} = "group::communication"
DEPENDS = " libmokoui2 libmokojournal2 pulseaudio dbus-glib libnotify"
PV = "0.0.1+svnr${SRCREV}"
SRCREV = "${AUTOREV}"

inherit autotools pkgconfig openmoko2

SRC_URI = "svn://svn.projects.openmoko.org/svnroot/shr/trunk/;module=${PN};proto=http"

LICENSE = "${@openmoko_two_get_license(d)}"
SUBDIR = "${@openmoko_two_get_subdir(d)}"

S = "${WORKDIR}/${PN}"

FILES_${PN} += "${datadir}/icons"
