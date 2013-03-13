DESCRIPTION = "U-boot bootloader mkimage tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
SECTION = "bootloader"

# This is needs to be validated among supported BSP's before we can
# make it default
DEFAULT_PREFERENCE = "-1"

# This revision corresponds to the tag "v2013.01.01"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "e8ae0fa5edd152b2b29c470b88429be4cdcd2c46"

PV = "v2013.01.01+git${SRCPV}"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC}" HOSTLD="${LD}" HOSTLDFLAGS="${LDFLAGS}" HOSTSTRIP=true'

do_compile () {
  oe_runmake tools
}

do_install () {
  install -d ${D}${bindir}
  install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
  ln -sf uboot-mkimage ${D}${bindir}/mkimage
}

BBCLASSEXTEND = "native nativesdk"
