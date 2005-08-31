SECTION = "base"
DESCRIPTION = "System Utilities Based on Sysfs"
HOMEPAGE = "http://linux-diag.sourceforge.net/Sysfsutils.html"
LICENSE = "GPLv2"
SRC_URI = "${SOURCEFORGE_MIRROR}/linux-diag/sysfsutils-${PV}.tar.gz"

S = "${WORKDIR}/sysfsutils-${PV}"

inherit autotools

includedir += "/sysfs"

PACKAGES_prepend = "libsysfs "
FILES_libsysfs = "${libdir}/*.so.1.0.3"

do_stage () {
	oe_libinstall -a -so -C lib libsysfs ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/sysfs
	install -m 0644 ${S}/include/dlist.h ${STAGING_INCDIR}/sysfs
	install -m 0644 ${S}/include/libsysfs.h ${STAGING_INCDIR}/sysfs
}
