SUMMARY = "Power usage tool"
DESCRIPTION = "Linux tool to diagnose issues with power consumption and power management."
HOMEPAGE = "http://01.org/powertop/"
BUGTRACKER = "http://bugzilla.lesswatts.org/"
DEPENDS = "ncurses virtual/gettext libnl pciutils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = "http://01.org/powertop/sites/default/files/downloads/powertop-${PV}.tar.gz"

SRC_URI[md5sum] = "dc03608f20e56cdc99d121a6191556f6"
SRC_URI[sha256sum] = "b8c1add69afee28c77dca56fdcedb4a46820f3a71c86aae7891b0c5c595cd744"

inherit autotools

# we need to explicitly link with libintl in uClibc systems
LDFLAGS += "${EXTRA_LDFLAGS}"
EXTRA_LDFLAGS_libc-uclibc = "-lintl"

# we do not want libncursesw if we can
do_configure_prepend() {
    # configure.ac checks for delwin() in "ncursesw ncurses" so let's drop first one
    sed -i -e "s/ncursesw//g" ${S}/configure.ac
}

inherit update-alternatives
ALTERNATIVE_${PN} = "powertop"
ALTERNATIVE_TARGET[powertop] = "${sbindir}/powertop"
ALTERNATIVE_LINK_NAME[powertop] = "${base_bindir}/powertop"
ALTERNATIVE_PRIORITY = "100"
