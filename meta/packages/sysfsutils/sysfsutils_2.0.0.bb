DESCRIPTION = "System Utilities Based on Sysfs"
HOMEPAGE = "http://linux-diag.sourceforge.net/Sysfsutils.html"
LICENSE = "GPLv2"
PR = "r3"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-diag/sysfsutils-${PV}.tar.gz"
S = "${WORKDIR}/sysfsutils-${PV}"

inherit autotools

PACKAGES_prepend = "libsysfs libsysfs-dbg libsysfs-dev "
FILES_libsysfs = "${libdir}/*.so.*"
FILES_libsysfs-dev = "${libdir}/* ${includedir}"
FILES_libsysfs-dbg = "${libdir}/.debug"
FILES_${PN}-dbg = "${bindir}/.debug"
