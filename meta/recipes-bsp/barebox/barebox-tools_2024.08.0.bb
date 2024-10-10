SUMMARY = "barebox bootloader tools"
HOMEPAGE = "https://barebox.org/"
SECTION = "bootloaders"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f5125d13e000b9ca1f0d3364286c4192"

DEPENDS = "bison-native flex-native libusb1"

SRC_URI = "https://barebox.org/download/barebox-${PV}.tar.bz2"
SRC_URI[sha256sum] = "b08a762da8d63dd18b4f2d9f5d0a8da001b6e608d1b3eff6dcebc6a2e575d535"

S = "${WORKDIR}/barebox-${PV}"
B = "${WORKDIR}/build"

inherit pkgconfig

EXTRA_OEMAKE = " \
  ARCH=sandbox \
  CROSS_COMPILE=${TARGET_PREFIX} -C ${S} O=${B} \
  CROSS_PKG_CONFIG=pkg-config \
  CC='${CC}' \
  LD='${LD}' \
  "

do_compile:class-target () {
    export userccflags="${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
    export userldflags="${TARGET_LDFLAGS}${TOOLCHAIN_OPTIONS}"
    oe_runmake targettools_defconfig
    oe_runmake scripts
}

do_compile:class-native () {
    oe_runmake hosttools_defconfig
    oe_runmake scripts
}

BAREBOX_TOOLS = " \
         bareboxenv \
         bareboxcrc32 \
         kernel-install \
         bareboximd \
         omap3-usb-loader \
         omap4_usbboot \
         imx/imx-usb-loader \
         "

BAREBOX_TOOLS_SUFFIX = ""
BAREBOX_TOOLS_SUFFIX:class-target = "-target"

do_install () {
	install -d ${D}${bindir}

	for tool in ${BAREBOX_TOOLS}; do
		install -m 0755 scripts/${tool}${BAREBOX_TOOLS_SUFFIX} ${D}${bindir}/${tool##*/}
	done
}

BBCLASSEXTEND = "native nativesdk"
