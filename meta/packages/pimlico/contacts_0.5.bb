require contacts.inc

PR = "r1"

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
	   file://stock_contact.png \
	   file://stock_person.png  \
	   file://contacts-owl-window-menu.patch;patch=1 \
	  "
