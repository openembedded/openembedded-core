DESCRIPTION = "System Utilities Based on Sysfs"
HOMEPAGE = "http://linux-diag.sourceforge.net/Sysfsutils.html"
LICENSE = "GPLv2"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-diag/sysfsutils-${PV}.tar.gz"
S = "${WORKDIR}/sysfsutils-${PV}"

inherit autotools

includedir += "/sysfs"

do_stage () {
	oe_libinstall -a -so -C lib libsysfs ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/sysfs
	install -m 0644 ${S}/include/dlist.h ${STAGING_INCDIR}/sysfs
	install -m 0644 ${S}/include/libsysfs.h ${STAGING_INCDIR}/sysfs
}

PACKAGES_prepend = "libsysfs libsysfs-dbg libsysfs-dev "
FILES_libsysfs = "${libdir}/*.so.*"
FILES_libsysfs-dev = "${libdir}/* ${includedir}"
FILES_libsysfs-dbg = "${libdir}/.debug"
FILES_${PN}-dbg = "${bindir}/.debug"
