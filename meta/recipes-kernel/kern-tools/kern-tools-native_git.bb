DESCRIPTION = "Scripts and utilities for managing Yocto branched kernels."
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=e2bf4415f3d843f43d2e22b0d91a6fee"

DEPENDS = "git-native guilt-native"

SRCREV = "ecbdf3cdf6705a63dcb3c50bb24c311046f0b35c"
PR = r10
PV = "0.1+git${SRCPV}"

inherit native

SRC_URI = "git://git.yoctoproject.org/yocto-kernel-tools.git;protocol=git"
S = "${WORKDIR}"

kern_tools_LIST = kgit kgit-init kgit-meta \
                  kgit-checkpoint kgit-clean \
                  generate_cfg kconf_check configme \
		  createme updateme patchme get_defconfig scc

do_compile() { 
	:
}

do_install() {
	install -d ${D}${bindir}
	for s in ${kern_tools_LIST}; do
	    install -m 0755 ${S}/git/tools/$s ${D}${bindir}
	done
}
