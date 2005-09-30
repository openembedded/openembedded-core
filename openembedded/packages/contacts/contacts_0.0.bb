LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Contacts is an address-book application."
PR = "r1"


SRC_URI = "file:///tmp/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig
