DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPL"
SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2"
PR = "r3"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native"

do_configure() {
	sed -i ${S}/Makefile ${S}/*/Makefile -e 's/\(CC[\t ]*\)=/\1?=/'
}

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

do_compile_virtclass-native () {
	oe_runmake installer
}

NATIVE_INSTALL_WORKS = "1"
do_install() {
	oe_runmake install INSTALLROOT="${D}"

	install -d ${D}${libdir}/syslinux/
	install -m 644 ${S}/ldlinux.sys ${D}${libdir}/syslinux/
	install -m 644 ${S}/ldlinux.bss ${D}${libdir}/syslinux/
}

BBCLASSEXTEND = "native"
