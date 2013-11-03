DESCRIPTION = "Scripts and utilities for managing Yocto branched kernels."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=d8d1d729a70cd5f52972f8884b80743d"

DEPENDS = "git-native guilt-native"

SRCREV = "2700c5d1c214766f01e68bdfa25049fa1a5960bc"
PR = "r12"
PV = "0.1+git${SRCPV}"

inherit native

SRC_URI = "git://git.yoctoproject.org/yocto-kernel-tools.git;protocol=git;branch=dylan"
S = "${WORKDIR}"

do_compile() { 
	:
}

do_install() {
	cd ${S}/git
	make DESTDIR=${D}${bindir} install
}
