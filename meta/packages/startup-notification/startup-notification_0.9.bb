DESCRIPTION = "Startup notification support"
HOMEPAGE = "http://www.freedesktop.org/wiki/software/startup-notification/"
SECTION = "libs"
PRIORITY = "optional"

# most files are under MIT, but libsn/sn-utils.c is under LGPL, the
# effective license is LGPL
LICENSE = "LGPLv2+"

DEPENDS = "virtual/libx11 libsm"

inherit autotools pkgconfig

SRC_URI = "http://www.freedesktop.org/software/startup-notification/releases/${PN}-${PV}.tar.gz"

do_configure_prepend () {
        export X_LIBS=" -L${STAGING_LIBDIR}"
}
