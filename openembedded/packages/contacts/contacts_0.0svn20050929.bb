LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Contacts is an address-book application."

S = "${WORKDIR}/trunk"

SRC_URI = "file:///tmp/${PN}-${PV}.tar.bz2"

inherit autotools pkgconfig

CFLAGS_prepend = " -DHAVE_PHOTO_TYPE=1 "

