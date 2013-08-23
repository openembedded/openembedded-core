DESCRIPTION = "U-boot bootloader mkimage tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb \
                    file://README;beginline=1;endline=22;md5=78b195c11cb6ef63e6985140db7d7bab"
SECTION = "bootloader"

SRC_URI = "ftp://ftp.denx.de/pub/u-boot/u-boot-${PV}.tar.bz2"

SRC_URI[md5sum] = "8445162690052e6afd4b8f87af2bb557"
SRC_URI[sha256sum] = "2bbd440c1b29928d230604eac372fda5152d3ae23f52b1910c77fc5bf531250b"

S = "${WORKDIR}/u-boot-${PV}"

BBCLASSEXTEND = "native nativesdk"

EXTRA_OEMAKE = 'HOSTCC="${CC}" HOSTLD="${LD}" HOSTLDFLAGS="${LDFLAGS}" HOSTSTRIP=true'

do_compile () {
  oe_runmake tools
}

do_install () {
  install -d ${D}${bindir}
  install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
  ln -sf uboot-mkimage ${D}${bindir}/mkimage
}

