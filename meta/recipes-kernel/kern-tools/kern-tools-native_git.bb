DESCRIPTION = "Scripts and utilities for managing Yocto branched kernels."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=d8d1d729a70cd5f52972f8884b80743d"

DEPENDS = "git-native guilt-native"

SRCREV = "6e92eca3bd30d73222dfeaae6d2d1d9764a5a3f8"
PR = "r12"
PV = "0.1+git${SRCPV}"

inherit native

SRC_URI = "git://git.yoctoproject.org/yocto-kernel-tools.git;protocol=git"
S = "${WORKDIR}"

do_compile() { 
	:
}

do_install() {
	cd ${S}/git
	make DESTDIR=${D}${bindir} install
}
