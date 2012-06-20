DESCRIPTION = "Sanitized set of kernel headers for the C library's use."
SECTION = "devel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

python __anonymous () {
    major = d.getVar("PV",True).split('.')[0]
    if major == "3":
	d.setVar("HEADER_FETCH_VER", "3.0")
    else:
	d.setVar("HEADER_FETCH_VER", "2.6")
}

inherit kernel-arch

SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v${HEADER_FETCH_VER}/linux-${PV}.tar.bz2"

S = "${WORKDIR}/linux-${PV}"

do_configure() {
	oe_runmake allnoconfig
}

do_compile () {
}

do_install() {
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}
	# Kernel should not be exporting this header
	rm -f ${D}${exec_prefix}/include/scsi/scsi.h

	# The ..install.cmd conflicts between various configure runs
	find ${D}${includedir} -name ..install.cmd | xargs rm -f
}

BBCLASSEXTEND = "nativesdk"

#DEPENDS = "cross-linkage"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "unifdef-native"
