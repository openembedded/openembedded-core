PR="r1"

SRC_URI="${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/Old/syslinux-${PV}.tar.bz2 \
         file://edx_assume_zero.patch;patch=1"

require syslinux.inc

do_compile() {
	oe_runmake syslinux
}
