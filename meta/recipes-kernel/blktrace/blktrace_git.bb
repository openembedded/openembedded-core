DESCRIPTION = "blktrace - generate traces of the I/O traffic on block devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

DEPENDS = "libaio"

SRCREV = "d6918c8832793b4205ed3bfede78c2f915c23385"

PR = "r4"
PV = "1.0.5+git${SRCPV}"

SRC_URI = "git://git.kernel.dk/blktrace.git;protocol=git \
           file://blktrace-makefile.patch"

S = "${WORKDIR}/git"

do_compile() { 
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" prefix=${prefix}
}

do_install() {
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" \
                   prefix=${prefix} DESTDIR=${D} install
}
