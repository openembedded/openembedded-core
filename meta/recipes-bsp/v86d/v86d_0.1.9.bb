DESCRIPTION = "User support binary for the uvesafb kernel module"
HOMEPAGE = "http://dev.gentoo.org/~spock/projects/uvesafb/"

# the copyright info is at the bottom of README, expect break
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=94ac1971e4f2309dc322d598e7b1f7dd"

DEPENDS = "virtual/kernel"
RRECOMMENDS_${PN} = "kernel-module-uvesafb"
PR = "r0"

SRC_URI = "http://dev.gentoo.org/~spock/projects/uvesafb/archive/v86d-${PV}.tar.bz2 \
           file://fbsetup"

SRC_URI[md5sum] = "ebbbc8e7013c9544b6ba6981add43831"
SRC_URI[sha256sum] = "8167dec4ff919cfd73f854bbd3244f05c2b867e014fa8298044ea7cfd66d18a8"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qemux86)"

INITSCRIPT_NAME = "fbsetup"
INITSCRIPT_PARAMS = "start 0 S ."

do_configure () {
	./configure --with-x86emu
}

do_compile () {
	KDIR="${STAGING_KERNEL_DIR}" make
}

do_install () {
	install -d ${D}${base_sbindir}
	install v86d ${D}${base_sbindir}/

        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/fbsetup ${D}${sysconfdir}/init.d/fbsetup
}

inherit update-rc.d
