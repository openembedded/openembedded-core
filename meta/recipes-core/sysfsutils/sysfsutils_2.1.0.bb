SUMMARY = "Tools for working with sysfs."
DESCRIPTION = "Tools for working with the sysfs virtual filesystem.  The tool 'systool' can query devices by bus, class and topology."
HOMEPAGE = "http://linux-diag.sourceforge.net/Sysfsutils.html"

LICENSE = "GPLv2 & LGPLv2.1"
LICENSE_${PN} = "GPLv2"
LICENSE_libsysfs = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=3d06403ea54c7574a9e581c6478cc393 \
                    file://cmd/GPL;md5=d41d4e2e1e108554e0388ea4aecd8d27 \
                    file://lib/LGPL;md5=b75d069791103ffe1c0d6435deeff72e"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-diag/sysfsutils-${PV}.tar.gz"
S = "${WORKDIR}/sysfsutils-${PV}"

inherit autotools

PACKAGES_prepend = "libsysfs libsysfs-dbg libsysfs-dev "
FILES_libsysfs = "${libdir}/*.so.*"
FILES_libsysfs-dev = "${libdir}/* ${includedir}"
FILES_libsysfs-dbg = "${libdir}/.debug"
FILES_${PN}-dbg = "${bindir}/.debug"
