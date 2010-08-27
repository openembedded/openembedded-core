DESCRIPTION = "Procps is the package that has a bunch \
of small useful utilities that give information \
about processes using the /proc filesystem. The package \
includes the programs ps, top, vmstat, w, kill, and skill."
HOMEPAGE = "http://procps.sf.net"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPL"
DEPENDS = "ncurses"

FILES_${PN} += "${base_libdir}/libproc-*.so"

SRC_URI = "http://procps.sourceforge.net/procps-${PV}.tar.gz \
           file://install.patch;patch=1"

inherit autotools
