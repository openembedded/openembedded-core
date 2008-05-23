DESCRIPTION = "The Openmoko Address Book"
SECTION = "openmoko/pim"
DEPENDS = "libmokoui2 libmokojournal2 dbus-glib libjana"
RDEPENDS = "libedata-book"
PV = "0.1.0+svnr${SRCREV}"
PR = "r5"

inherit openmoko2

SRC_URI = "svn://svn.o-hand.com/repos/contacts/branches;module=hito;proto=http"
S = "${WORKDIR}/hito/"

EXTRA_OECONF = "--disable-gnome-vfs --with-frontend=openmoko"
