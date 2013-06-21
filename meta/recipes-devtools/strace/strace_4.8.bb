DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=124500c21e856f0912df29295ba104c7"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://git-version-gen"

SRC_URI[md5sum] = "c575ef43829586801f514fd91bfe7575"
SRC_URI[sha256sum] = "f492291f07a7c805c07a8395cce1ea054a6401ad414f4cc12185672215e1d7f8"
inherit autotools

export INCLUDES = "-I. -I./linux"

do_configure_prepend() {
	cp ${WORKDIR}/git-version-gen ${S}
}

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

BBCLASSEXTEND = "native"
