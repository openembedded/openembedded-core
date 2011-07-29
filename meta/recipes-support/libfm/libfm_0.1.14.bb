DESCRIPTION = "Library for file management"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/fm.h;endline=22;md5=e64555171770a551e3b51cc06fc62f1a \
                    file://src/base/fm-config.h;endline=23;md5=ad0fc418c3cf041eea35ddb3daf37f17"

SECTION = "x11/libs"
DEPENDS = "gtk+ menu-cache intltool-native"

PR = "r3"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/libfm-${PV}.tar.gz \
           file://use_deprecate_func.patch"

SRC_URI[md5sum] = "d55e51dced6bb9ef46665243b079761f"
SRC_URI[sha256sum] = "e97bf3f9ed4f33b9f0be73e67d360b89337cccf816361faacd8422722269806b"

inherit autotools pkgconfig
