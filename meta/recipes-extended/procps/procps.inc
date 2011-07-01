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

FILES_${PN} += "${base_libdir}/libproc-*.so"

SRC_URI = "http://procps.sourceforge.net/procps-${PV}.tar.gz \
           file://install.patch"

inherit autotools
