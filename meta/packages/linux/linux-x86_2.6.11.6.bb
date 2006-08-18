DESCRIPTION = "Linux Kernel for x86 compatible machines"
SECTION = "kernel"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "ftp://ftp.kernel.org/pub/linux/kernel/v2.6/linux-${PV}.tar.bz2 \
           file://defconfig"

S = "${WORKDIR}/linux-${PV}"

inherit kernel

COMPATIBLE_HOST = "i.86.*-linux"
KERNEL_IMAGETYPE = "bzImage"

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config
}

