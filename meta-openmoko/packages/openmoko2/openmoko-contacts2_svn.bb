require openmoko.inc

DESCRIPTION = "The OpenMoko address book"
RDEPENDS = "libedata-book"
PV = "0.1+svn${SRCDATE}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/contacts/branches/;module=hito;proto=http"

S = "${WORKDIR}/hito/"

EXTRA_OECONF = "--disable-gnome-vfs --with-frontend=openmoko"
