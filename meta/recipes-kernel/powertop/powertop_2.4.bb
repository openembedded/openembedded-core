SUMMARY = "Power usage tool"
DESCRIPTION = "Linux tool to diagnose issues with power consumption and power management."
HOMEPAGE = "http://01.org/powertop/"
BUGTRACKER = "http://bugzilla.lesswatts.org/"
DEPENDS = "ncurses libnl pciutils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = "http://01.org/powertop/sites/default/files/downloads/powertop-${PV}.tar.gz"

SRC_URI[md5sum] = "3aa686bb245d6683b86cba9a6a4b8c6d"
SRC_URI[sha256sum] = "42796c94546ae7f3d232e41b7fa09b2532396ca389908ff528870311db6327b7"

inherit autotools gettext

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
ALTERNATIVE_LINK_NAME[powertop] = "${sbindir}/powertop"
ALTERNATIVE_PRIORITY = "100"
