DESCRIPTION = "Console based version of exmap, a memory usage analysis tool"
HOMEPAGE = "http://projects.o-hand.com/exmap-console"
SECTION = "devel"
LICENSE = "GPL"
DEPENDS = "virtual/kernel readline glib-2.0"

SRC_URI = "http://projects.o-hand.com/sources/exmap-console/exmap-console-${PV}.tgz"

TEMPPACKAGE_ARCH := "${PACKAGE_ARCH}"

inherit module-base
inherit autotools

PACKAGE_ARCH = "${TEMPPACKAGE_ARCH}"

PACKAGES =+ "exmap-server kernel-module-exmap"

FILES_exmap-console = "${bindir}/exmap ${bindir}/exmapd"
RDEPENDS_exmap-console += "kernel-module-exmap"

FILES_exmap-server = "${bindir}/exmapserver"
RDEPENDS_exmap-server += "kernel-module-exmap"

FILES_kernel-module-exmap = "${base_libdir}"
PACKAGE_ARCH_kernel-module-exmap = "${MACHINE_ARCH}"
RDEPENDS_kernel-module-exmap += "update-modules kernel-image-${KERNEL_VERSION}"

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

do_install() {
	oe_runmake 'DESTDIR=${D}' 'DEPMOD=/bin/true' install
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
