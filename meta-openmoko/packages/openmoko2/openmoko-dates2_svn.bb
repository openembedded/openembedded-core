DESCRIPTION = "The Openmoko Calendar"
SECTION = "openmoko/pim"
DEPENDS = "libmokoui2 libmokojournal2 gtk+ libglade eds-dbus libjana"
RDEPENDS = "libedata-cal"
PV = "0.1.0+svnr${SRCREV}"
PR = "r2"

inherit openmoko2 pkgconfig

SRC_URI = "svn://svn.o-hand.com/repos/dates/branches;module=openmoko2;proto=http"
S = "${WORKDIR}/openmoko2/"

EXTRA_OECONF = "--with-frontend=openmoko"

do_configure_prepend() {
	touch gtk-doc.make
}


