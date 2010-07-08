DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://README;beginline=28;endline=34;md5=a4607efd4a6392017186d08099e7d546"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native"
PR = "r1"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2 \
           file://cross-build.patch"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

EXTRA_OEMAKE = " \
	BINDIR=${bindir} SBINDIR=${sbindir} LIBDIR=${libdir} \
	DATADIR=${datadir} MANDIR=${mandir} INCDIR=${includedir} \
"
# syslinux uses $LD for linking, strip `-Wl,' so it can work
export LDFLAGS = "`echo $LDFLAGS | sed 's/-Wl,//g'`"

do_configure() {
	# drop win32 targets or build fails
	sed -e 's,win32/\S*,,g' -i Makefile

	# clean installer executables included in source tarball
	oe_runmake clean
}

do_compile() {
	# Rebuild only the installer; keep precompiled bootloaders
	# as per author's request (doc/distrib.txt)
	oe_runmake CC="${CC}" installer
}

NATIVE_INSTALL_WORKS = "1"
do_install() {
	oe_runmake install INSTALLROOT="${D}"

	install -d ${D}${libdir}/syslinux/
	install -m 644 ${S}/core/ldlinux.sys ${D}${libdir}/syslinux/
	install -m 644 ${S}/core/ldlinux.bss ${D}${libdir}/syslinux/
}

BBCLASSEXTEND = "native"
