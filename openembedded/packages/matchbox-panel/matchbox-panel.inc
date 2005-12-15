DESCRIPTION = "Matchbox Window Manager Panel"
LICENSE = "GPL"
DEPENDS = "libmatchbox x11 xext xpm apmd startup-notification virtual/kernel wireless-tools"
SECTION = "x11/wm"

inherit autotools pkgconfig gettext

CFLAGS += "-D_GNU_SOURCE"

EXTRA_OECONF = " --enable-startup-notification --enable-dnotify "
EXTRA_OECONF_append_h3600 = " --enable-small-icons "
EXTRA_OECONF_append_h3900 = " --enable-small-icons "
EXTRA_OECONF_append_h2200 = " --enable-small-icons "
EXTRA_OECONF_append_collie = " --enable-small-icons "
EXTRA_OECONF_append_poodle = " --enable-small-icons "
EXTRA_OECONF_append_mnci = " --enable-small-icons "
EXTRA_OECONF_append_integral13 = " --enable-small-icons "

FILES_${PN} = "${bindir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps"

