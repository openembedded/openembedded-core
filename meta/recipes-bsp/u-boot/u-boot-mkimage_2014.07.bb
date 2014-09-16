SUMMARY = "U-Boot bootloader image creation tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"
SECTION = "bootloader"

# This revision corresponds to the tag "v2014.07"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "524123a70761110c5cf3ccc5f52f6d4da071b959"

PV = "v2014.07+git${SRCPV}"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC}" HOSTLD="${LD}" HOSTLDFLAGS="${LDFLAGS}" HOSTSTRIP=true'

do_compile () {
	# Make sure the recompile is OK
	rm -f ${B}/tools/.depend

	make HOSTCC="${BUILD_CC}" HOSTLD="${BUILD_LD}" HOSTLDFLAGS="${BUILD_LDFLAGS}" HOSTSTRIP=true dot-config=0 scripts_basic
	sed 's/^tools-only: scripts_basic /tools-only: /' -i Makefile
	oe_runmake tools-only
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
	ln -sf uboot-mkimage ${D}${bindir}/mkimage
}

BBCLASSEXTEND = "native nativesdk"
