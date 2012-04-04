DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://README;beginline=28;endline=34;md5=a4607efd4a6392017186d08099e7d546"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native"
PR = "r8"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/boot/syslinux/4.xx/syslinux-${PV}.tar.bz2 \
           file://cross-build.patch \
           file://no-strip.patch \
           file://libinstaller-Avoid-using-linux-ext2_fs.h.patch"

SRC_URI[md5sum] = "a7ca38a0a5786b6efae8fb01a1ae8070"
SRC_URI[sha256sum] = "c65567e324f9d1f7f794ae8f9578a0292bbd47d7b8d895a004d2f0152d0bda38"

COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'

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

do_install() {
	oe_runmake install INSTALLROOT="${D}"

	install -d ${D}${libdir}/syslinux/
	install -m 644 ${S}/core/ldlinux.sys ${D}${libdir}/syslinux/
	install -m 644 ${S}/core/ldlinux.bss ${D}${libdir}/syslinux/
}

PACKAGES += "${PN}-extlinux ${PN}-mbr ${PN}-chain ${PN}-pxelinux ${PN}-isolinux ${PN}-misc"

RDEPENDS_${PN} += "mtools"

FILES_${PN} = "${bindir}/syslinux"
FILES_${PN}-extlinux = "${sbindir}/extlinux"
FILES_${PN}-mbr = "${libdir}/${PN}/mbr.bin"
FILES_${PN}-chain = "${libdir}/${PN}/chain.c32"
FILES_${PN}-isolinux = "${libdir}/${PN}/isolinux.bin"
FILES_${PN}-pxelinux = "${libdir}/${PN}/pxelinux.0"
FILES_${PN}-dev += "${datadir}/${PN}/com32/lib*${SOLIBS} ${datadir}/${PN}/com32/include ${datadir}/${PN}/com32/com32.ld"
FILES_${PN}-staticdev += "${datadir}/${PN}/com32/lib*.a ${libdir}/${PN}/com32/lib*.a"
FILES_${PN}-misc = "${libdir}/${PN}/* ${bindir}/*"

BBCLASSEXTEND = "native"
