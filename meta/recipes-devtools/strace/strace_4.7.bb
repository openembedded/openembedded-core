DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=124500c21e856f0912df29295ba104c7"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://strace-eglibc-2.16.patch \
           file://strace-x32.patch \
          "

SRC_URI[md5sum] = "6054c3880a00c6703f83b57f15e04642"
SRC_URI[sha256sum] = "c49cd98873c119c5f201356200a9b9687da1ceea83a05047e2ae0a7ac1e41195"
inherit autotools

export INCLUDES = "-I. -I./linux"

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

BBCLASSEXTEND = "native"
