DESCRIPTION = "Gamin is a file and directory monitoring system defined to be a subset of the FAM (File Alteration Monitor) system."
LICENSE = "LGPL"

SRC_URI = "http://www.gnome.org/~veillard/gamin/sources/gamin-${PV}.tar.gz \
           file://no-abstract-sockets.patch;patch=1"

EXTRA_OECONF = " --without-python " 

inherit autotools_stage pkgconfig
