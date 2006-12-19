DESCRIPTION = "console-based exmap"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "GPL"
PR = "r7"
PV = "0.3.1+svn${SRCDATE}"

SRC_URI = \
    "svn://svn.o-hand.com/repos/misc/trunk;module=exmap-console;proto=http"

inherit module-base
inherit autotools

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

FILES_${PN}="${sbindir} ${base_libdir}"
