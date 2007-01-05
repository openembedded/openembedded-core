DESCRIPTION = "console-based exmap"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "GPL"

SRC_URI = \
    "http://projects.o-hand.com/sources/exmap-console/exmap-console-${PV}.tgz"

inherit module-base
inherit autotools

MYPV := "${PV}"

PACKAGES += "exmap-server kernel-module-exmap"

FILES_${PN}= "${bindir}/exmap ${bindir}/exmapd"
PACKAGE_ARCH_exmap-console = "${TARGET_ARCH}"
RDEPENDS_exmap-console += "kernel-module-exmap"

FILES_exmap-server = "${bindir}/exmapserver"
PACKAGE_ARCH_exmap-server = "${TARGET_ARCH}"
RDEPENDS_exmap-server += "kernel-module-exmap"

FILES_kernel-module-exmap = "${base_libdir}"
PACKAGE_ARCH_kernel-module-exmap = "${MACHINE_ARCH}"
PV_kernel-module-exmap = "${MYPV}-${KERNEL_VERSION}"
RDEPENDS_kernel-module-exmap += "kernel (${KERNEL_VERSION})"
DEPENDS_kernel-module-exmap += "virtual/kernel"

S = "${WORKDIR}/exmap-console-${PV}"

export MODULE_PATH="${D}${base_libdir}/modules/${KERNEL_VERSION}"

do_compile() {
	cd ${S}/src
	make

	cd ${S}/kernel
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   ${MAKE_TARGETS}
}

pkg_postinst_append_kernel-module-exmap () {
	if [ -n "$D" ]; then
		exit 1
	fi
	depmod -a
	update-modules || true
}

pkg_postrm_append_kernel-module-exmap () {
	update-modules || true
}
