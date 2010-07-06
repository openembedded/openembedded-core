DESCRIPTION = "User support binary for the uvesafb kernel module"
HOMEPAGE = "http://dev.gentoo.org/~spock/projects/uvesafb/"

LICENSE = "GPLv2"

DEPENDS = "virtual/kernel"
RRECOMMENDS_${PN} = "kernel-module-uvesafb"
PR = "r0"

SRC_URI = "http://dev.gentoo.org/~spock/projects/uvesafb/archive/v86d-${PV}.tar.bz2 \
           file://fbsetup"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qemux86|bootcdx86)"

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
