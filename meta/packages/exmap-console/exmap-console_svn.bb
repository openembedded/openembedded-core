DESCRIPTION = "console-based exmap"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "GPL"
PR = "r8"
SRCDATE=20070105

# HACK -- I want the kernel module version label to include both the 
# exmap pacakge version and the kernel version, but it is not possible
# to use ${PV} in the definition of PV_kernel-module-exmap (complains
# about recursion, hence $MYPV 

PV = "0.4+svn${SRCDATE}"
MYPV = "0.4+svn${SRCDATE}"

SRC_URI = \
    "svn://svn.o-hand.com/repos/misc/trunk;module=exmap-console;proto=http"

inherit module-base
inherit autotools

PACKAGES += "exmap-server kernel-module-exmap"

FILES_${PN}= "${sbindir}"
PACKAGE_ARCH_exmap-console = "${TARGET_ARCH}"
RDEPENDS_exmap-console = "kernel-module-exmap"

FILES_exmap-server = "${bindir}"
PACKAGE_ARCH_exmap-server = "${TARGET_ARCH}"
RDEPENDS_exmap-server = "kernel-module-exmap"

FILES_kernel-module-exmap = "${base_libdir}"
PACKAGE_ARCH_kernel-module-exmap = "${MACHINE_ARCH}"
PV_kernel-module-exmap = "${MYPV}-${KERNEL_VERSION}"

S = "${WORKDIR}/exmap-console"

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

