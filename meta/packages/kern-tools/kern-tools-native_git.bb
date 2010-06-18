DESCRIPTION = "Scripts and utilities for managing Wind River kernels."
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=e2bf4415f3d843f43d2e22b0d91a6fee"

DEPENDS = "git-native"

PR = r4
PV = "0.1+git${SRCPV}"

# needed until the native do_stage goes away
NATIVE_INSTALL_WORKS=1

inherit native

SRC_URI = "git://git.pokylinux.org/wr-kernel-tools.git;protocol=git"
S = "${WORKDIR}"

kern_tools_LIST = kgit kgit-init kgit-meta \
                  kgit-checkpoint kgit-clean \
                  generate_cfg kconf_check configme

do_compile() { 
	:
}

do_install() {
	install -d ${D}${bindir}
	for s in ${kern_tools_LIST}; do
	    install -m 0755 ${S}/git/tools/$s ${D}${bindir}
	done
}
