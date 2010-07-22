DESCRIPTION = "Startup-notification contains a reference implementation of the startup notification protocol."
HOMEPAGE = "http://www.freedesktop.org/wiki/software/startup-notification/"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=Specifications"

# most files are under MIT, but libsn/sn-util.c is under LGPL, the
# effective license is LGPL
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a2ae2cd47d6d2f238410f5364dfbc0f2 \
                    file://libsn/sn-util.c;endline=18;md5=18a14dc1825d38e741d772311fea9ee1 \
                    file://libsn/sn-common.h;endline=23;md5=6d05bc0ebdcf5513a6e77cb26e8cd7e2 \
                    file://test/test-boilerplate.h;endline=23;md5=923e706b2a70586176eead261cc5bb98"

PR = "r0"

SECTION = "libs"

PRIORITY = "optional"

DEPENDS = "virtual/libx11 libsm xcb-util"

inherit autotools pkgconfig

SRC_URI = "http://www.freedesktop.org/software/startup-notification/releases/${PN}-${PV}.tar.gz"

do_configure_prepend () {
        export X_LIBS=" -L${STAGING_LIBDIR}"
}
