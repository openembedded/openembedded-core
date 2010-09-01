require at.inc

LICENSE="GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4325afd396febcb659c36b49533135d4"

PR = "r1"

SRC_URI = "${DEBIAN_MIRROR}/main/a/at/at_${PV}.orig.tar.gz \
    file://configure.patch \
    file://nonrootinstall.patch \
    file://use-ldflags.patch \
    file://posixtm.c \
    file://posixtm.h \
    file://file_replacement_with_gplv2.patch"

do_compile_prepend () {
       mv ${WORKDIR}/posixtm.[ch] ${S}
}

