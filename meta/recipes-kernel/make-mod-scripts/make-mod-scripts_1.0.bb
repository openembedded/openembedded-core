SUMMARY = "Build tools needed by external modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit kernel-arch
inherit pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_compile[depends] += "virtual/kernel:do_compile_kernelmodules"

# Build some host tools under work-shared.  CC, LD, and AR are probably
# not used, but this is the historical way of invoking "make scripts".
#
do_configure() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	make CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
	           -C ${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} scripts
}


# There is no reason to build this on its own.
#
EXCLUDE_FROM_WORLD = "1"

