DESCRIPTION = "User support binary for the uvesafb kernel module"
SRC_URI = "http://dev.gentoo.org/~spock/projects/uvesafb/archive/v86d-0.1.3.tar.bz2"
DEPENDS = "virtual/kernel"
LICENSE = "GPLv2"

RRECOMMENDS_${PN} = "kernel-module-uvesafb"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "qemux86"

do_configure () {
	./configure --default
}

do_compile () {
	KDIR="${STAGING_KERNEL_DIR}" make
}

do_install () {
	install -d ${D}${base_sbindir}
	install v86d ${D}${base_sbindir}/
}