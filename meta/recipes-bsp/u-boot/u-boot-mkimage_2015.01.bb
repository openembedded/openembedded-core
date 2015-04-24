SUMMARY = "U-Boot bootloader image creation tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"
SECTION = "bootloader"

DEPENDS = "openssl"

# This revision corresponds to the tag "v2015.01"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "92fa7f53f1f3f03296f8ffb14bdf1baefab83368"

PV = "v2015.01+git${SRCPV}"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master \
           file://gcc5.patch \
	   "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'CROSS_COMPILE="${TARGET_PREFIX}" CC="${CC} ${CFLAGS} ${LDFLAGS}" STRIP=true V=1'

do_compile () {
	oe_runmake sandbox_defconfig
	oe_runmake cross_tools NO_SDL=1
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
	ln -sf uboot-mkimage ${D}${bindir}/mkimage
}

BBCLASSEXTEND = "native nativesdk"
