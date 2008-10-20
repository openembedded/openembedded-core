DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPL"
SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2"
PR = "r1"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native"

S = "${WORKDIR}/syslinux-${PV}"

do_configure() {
	sed -i ${S}/Makefile ${S}/*/Makefile -e 's/\(CC[\t ]*\)=/\1?=/'
}

STAGE_TEMP = "${WORKDIR}/stage_temp"

do_stage() {
	install -d ${STAGE_TEMP}
	oe_runmake install INSTALLROOT="${STAGE_TEMP}"
	
	# When building media, the syslinux binary isn't nearly as useful
	# as the DOS data files, so we copy those into a special location
	# for usage during a image build stage
	    
	install -d ${STAGING_DATADIR}/syslinux
	install -m 0644 ${STAGE_TEMP}/usr/lib/syslinux/isolinux.bin ${STAGING_DATADIR}/syslinux/isolinux.bin
	install -m 644 ${S}/ldlinux.sys ${STAGING_DATADIR}/syslinux/ldlinux.sys
	install -m 644 ${S}/ldlinux.bss ${STAGING_DATADIR}/syslinux/ldlinux.bss
}
