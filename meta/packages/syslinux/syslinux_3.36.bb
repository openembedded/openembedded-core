DESCRIPTION="A multi-purpose linux bootloader"
HOMEPAGE="http://syslinux.zytor.com/"
LICENSE="GPL"
SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2 "

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS="nasm-native"

S="${WORKDIR}/syslinux-${PV}"

do_configure() {
	sed -i ${S}/Makefile ${S}/*/Makefile -e 's/\(CC[\t ]*\)=/\1?=/'
}
