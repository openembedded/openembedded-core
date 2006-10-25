DESCRIPTION = "console-based exmap"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "GPL"
PR = "r1"
SRC_DATE=20061026

SRC_URI = \
    "svn://svn.o-hand.com/repos/misc/trunk;module=exmap-console;proto=http"

inherit module-base

S = "${WORKDIR}/exmap-console"

export KERNEL_PATH=${STAGING_KERNEL_DIR}

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

sbindir="/usr/sbin"

do_install() {
	install -d ${D}${sbindir}
	install -m 710 ${S}/src/exmap ${D}${sbindir}/exmap

	install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/misc
	install -m 644 ${S}/kernel/exmap${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/misc/
}

PACKAGES = "exmap-console"
FILES_exmap-console = "${sbindir}/ /lib/modules/${KERNEL_VERSION}/misc/"
