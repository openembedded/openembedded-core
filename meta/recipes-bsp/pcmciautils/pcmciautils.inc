DESCRIPTION = "Linux Kernel 2.6 Userland Utilities for the PCMCIA Subsystem"
HOMEPAGE = "http://kernel.org/pub/linux/utils/kernel/pcmcia/pcmcia.html"
SECTION = "kernel/userland"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "sysfsutils flex-native"
RDEPENDS_${PN} = "udev module-init-tools"
RCONFLICTS_${PN} += "pcmcia-cs"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/pcmcia/pcmciautils-${PV}.tar.bz2"

S = "${WORKDIR}/pcmciautils-${PV}"

export HOSTCC = "${BUILD_CC}"
export etcdir = "${sysconfdir}"
export sbindir = "${base_sbindir}"
export pcmciaconfdir = "${sysconfdir}/pcmcia"
export udevrulesdir = "${sysconfdir}/udev/rules.d"
export UDEV = "1"
LD = "${CC}"
CFLAGS =+ "-I${S}/src"
CFLAGS =+ "-DPCMCIAUTILS_VERSION=\'${PV}\'"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "-e 'STRIP=echo' 'LIB_OBJS=-lc -lsysfs' 'LEX=flex'"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}

CONFFILES_${PN} += "${sysconfdir}/pcmcia/config.opts"
