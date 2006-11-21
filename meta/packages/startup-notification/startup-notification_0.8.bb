DESCRIPTION = "Startup notification support"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11"
PR = "r1"

inherit autotools pkgconfig 

SRC_URI = "http://www.freedesktop.org/software/startup-notification/releases/startup-notification-0.8.tar.gz"

do_configure_prepend () {
        export X_LIBS=" -L${STAGING_LIBDIR}"
}

do_stage () {
	autotools_stage_all
}
