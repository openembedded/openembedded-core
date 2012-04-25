DESCRIPTION = "U-boot bootloader fw_printenv/setenv utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
SECTION = "bootloader"

DEPENDS = "mtd-utils"

# This is needs to be validated among supported BSP's before we can
# make it default
DEFAULT_PREFERENCE = "-1"

# This revision corresponds to the tag "v2012.04.01"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "415d386877df49eb051b85ef74fa59a16dc17c7d"

PV = "v2012.04.01+git${SRCPV}"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'HOSTCC="${CC}"'

do_compile () {
  oe_runmake env
}

do_install () {
  install -d ${D}${base_sbindir}
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
  install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv
}
