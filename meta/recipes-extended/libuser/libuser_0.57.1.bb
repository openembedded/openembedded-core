SUMMARY = "user and group account administration library"
DESCRIPTION = "The libuser library implements a standardized interface for manipulating and administering user \
and group accounts"
HOMEPAGE = "https://fedorahosted.org/libuser/"
BUGTRACKER = "https://fedorahosted.org/libuser/newticket"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://lib/user.h;endline=19;md5=9bc141f2e0d3e8b97ecdc945b2ed1ec7 \
                    file://samples/testuser.c;endline=19;md5=61e8c05bd37ce1cba5590071f6e17500"

SECTION = "base"

SRC_URI = "https://fedorahosted.org/releases/l/i/libuser/libuser-${PV}.tar.xz"

SRC_URI[md5sum] = "be82c6941264d0b4bd04f95fb342ec7d"
SRC_URI[sha256sum] = "a61289867581fa715354a3fafe09c3e481173ce0a2dcb33b04588b6ac13cead5"
PR = "r1"

DEPENDS = "popt libpam glib-2.0 xz-native docbook-utils-native linuxdoc-tools-native"

EXTRA_OECONF += "--without-python"

inherit autotools gettext
