DESCRIPTION = "Matchbox Window Manager Panel"
LICENSE = "GPL"
DEPENDS = "libmatchbox x11 xext xpm apmd startup-notification virtual/kernel wireless-tools"
SECTION = "x11/wm"
PR="r2"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/${PN}/0.9/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig gettext

CFLAGS += "-D_GNU_SOURCE"

EXTRA_OECONF = "--enable-startup-notification --enable-dnotify"
EXTRA_OECONF_append_h3600 = " --enable-small-icons "
EXTRA_OECONF_append_h3900 = " --enable-small-icons "
EXTRA_OECONF_append_collie = " --enable-small-icons "
EXTRA_OECONF_append_poodle = " --enable-small-icons "
EXTRA_OECONF_append_mnci = " --enable-small-icons "
EXTRA_OECONF_append_integral = " --enable-small-icons "

FILES_${PN} = "${bindir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps"

