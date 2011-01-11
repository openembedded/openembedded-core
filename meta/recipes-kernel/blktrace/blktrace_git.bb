DESCRIPTION = "blktrace - generate traces of the I/O traffic on block devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

DEPENDS = "libaio"

PR = r2
PV = "1.0.1+git${SRCPV}"

SRC_URI = "git://git.kernel.dk/blktrace.git;protocol=git \
           file://blktrace-makefile.patch \
           file://dso_linking_change_build_fix.patch"

SRC_URI[md5sum] = "588aa9ab8a14d1766ab3f061d728ed89"
SRC_URI[sha256sum] = "f3cb6a3df1b3dc67369c7c4da3444a7897aa8a18e55937b368e0a6aa2f64b3aa"

S = "${WORKDIR}/git"

do_compile() { 
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" prefix=${prefix}
}

do_install() {
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" \
                   prefix=${prefix} DESTDIR=${D} install
}
