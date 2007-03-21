require contacts.inc

#DEPENDS += "gnome-vfs"
#RDEPENDS += "gnome-vfs-plugin-file"
#RRECOMMENDS += "gnome-vfs-plugin-http"

PV = "0.4+svn${SRCDATE}"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
	   file://stock_contact.png \
	   file://stock_person.png"

S = "${WORKDIR}/trunk"

#EXTRA_OECONF = "--enable-gnome-vfs"
