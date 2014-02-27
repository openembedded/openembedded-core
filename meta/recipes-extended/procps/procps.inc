SUMMARY = "System and process monitoring utilities"
DESCRIPTION = "Procps contains a set of system utilities that provide system information about processes using \
the /proc filesystem. The package \ includes the programs ps, top, vmstat, w, kill, and skill."
HOMEPAGE = "http://procps.sf.net"
SECTION = "base"
LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM="file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                  file://COPYING.LIB;md5=6e29c688d912da12b66b73e32b03d812 \
                  file://ps/COPYING;md5=6e29c688d912da12b66b73e32b03d812 \
                  file://proc/COPYING;md5=6e29c688d912da12b66b73e32b03d812"
DEPENDS = "ncurses"

SRC_URI = "http://procps.sourceforge.net/procps-${PV}.tar.gz \
           file://install.patch"

inherit autotools-brokensep update-alternatives

do_install_append() {
	mv ${D}${bindir}/watch ${D}${bindir}/watch.${BPN}
}

FILES_${PN} += "${libdir}/*-${PV}.so ${base_libdir}/*-${PV}.so"
FILES_SOLIBSDEV = ""

ALTERNATIVE_${PN} = "top uptime free pkill pmap kill sysctl ps pgrep pwdx watch"
ALTERNATIVE_LINK_NAME[kill] = "${base_bindir}/kill"
ALTERNATIVE_LINK_NAME[sysctl] = "${base_sbindir}/sysctl"
ALTERNATIVE_LINK_NAME[ps] = "${base_bindir}/ps"
ALTERNATIVE_LINK_NAME[watch] = "${base_bindir}/watch"
ALTERNATIVE_TARGET[watch] = "${bindir}/watch.${BPN}"
ALTERNATIVE_PRIORITY = "110"
