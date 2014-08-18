SUMMARY = "Multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://README;beginline=35;endline=41;md5=558f2c71cb1fb9ba511ccd4858e48e8a"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native util-linux"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/boot/syslinux/6.xx/syslinux-${PV}.tar.bz2 \
           file://0001-movebits-Add-SMT_TERMINAL-a-last-resort-region-type.patch \
           file://0002-memscan-build-a-linked-list-of-memory-scanners.patch \
           file://0003-PXELINUX-Add-bios-memscan-function.patch \
           file://0004-pxe-use-bios_fbm-and-real_base_mem-to-calculate-free.patch \
           file://syslinux-fix-parallel-building-issue.patch \
           file://isohybrid-fix-overflow-on-32-bit-system.patch \
           file://syslinux-libupload-depend-lib.patch \
           "

SRC_URI[md5sum] = "6945ee89e29119d459baed4937bbc534"
SRC_URI[sha256sum] = "83a04cf81e6a46b80ee5a321926eea095af3498b04317e3674b46c125c7a5b43"

COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'
# Don't let the sanity checker trip on the 32 bit real mode BIOS binaries
INSANE_SKIP_${PN}-misc = "arch"
INSANE_SKIP_${PN}-chain = "arch"

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
	oe_runmake clean firmware="efi32" EFIINC="${includedir}"
	# NOTE: There is a temporary work around above to specify
	#	the efi32 as the firmware else the pre-built bios
	#	files get erased contrary to the doc/distib.txt
	#	In the future this should be "bios" and not "efi32".
}

do_compile() {
	# Make sure the recompile is OK.
	# Though the ${B} should always exist, still check it before find and rm.
	[ -d "${B}" ] && find ${B} -name '.*.d' -type f -exec rm -f {} \;

	# Rebuild only the installer; keep precompiled bootloaders
	# as per author's request (doc/distrib.txt)
	oe_runmake CC="${CC} ${CFLAGS}" LDFLAGS="${LDFLAGS}" firmware="bios" installer
}

do_install() {
	oe_runmake CC="${CC} ${CFLAGS}" install INSTALLROOT="${D}" firmware="bios"

	install -d ${D}${datadir}/syslinux/
	install -m 644 ${S}/bios/core/ldlinux.sys ${D}${datadir}/syslinux/
	install -m 644 ${S}/bios/core/ldlinux.bss ${D}${datadir}/syslinux/
}

PACKAGES += "${PN}-extlinux ${PN}-mbr ${PN}-chain ${PN}-pxelinux ${PN}-isolinux ${PN}-misc"

RDEPENDS_${PN} += "mtools"
RDEPENDS_${PN}-misc += "perl"

FILES_${PN} = "${bindir}/syslinux"
FILES_${PN}-extlinux = "${sbindir}/extlinux"
FILES_${PN}-mbr = "${datadir}/${BPN}/mbr.bin"
FILES_${PN}-chain = "${datadir}/${BPN}/chain.c32"
FILES_${PN}-isolinux = "${datadir}/${BPN}/isolinux.bin"
FILES_${PN}-pxelinux = "${datadir}/${BPN}/pxelinux.0"
FILES_${PN}-dev += "${datadir}/${BPN}/com32/lib*${SOLIBS} ${datadir}/${BPN}/com32/include ${datadir}/${BPN}/com32/com32.ld"
FILES_${PN}-staticdev += "${datadir}/${BPN}/com32/lib*.a ${libdir}/${BPN}/com32/lib*.a"
FILES_${PN}-misc = "${datadir}/${BPN}/* ${libdir}/${BPN}/* ${bindir}/*"

BBCLASSEXTEND = "native nativesdk"
