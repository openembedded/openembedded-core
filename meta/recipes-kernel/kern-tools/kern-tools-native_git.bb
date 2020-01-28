SUMMARY = "Tools for managing Yocto Project style branched kernels"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=9c30e971d435e249624278c3e343e501"

DEPENDS = "git-native"

SRCREV = "7465c8497893ef8f6503c918aeede969f7e9753b"
PR = "r12"
PV = "0.2+git${SRCPV}"

inherit native

SRC_URI = "git://git.yoctoproject.org/yocto-kernel-tools.git"
S = "${WORKDIR}"
UPSTREAM_CHECK_COMMITS = "1"

do_compile() { 
	:
}

do_install() {
	cd ${S}/git
	make DESTDIR=${D}${bindir} install
}
