HOMEPAGE = "http://gitorious.org/opensuse/zypper"
DESCRIPTION  = "The ZYpp Linux Software management framework"

LICENSE  = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3201406e350b39e05a82e28b5020f413"

DEPENDS  = "libzypp augeas"
RRECOMMENDS_${PN} = "procps"
PR = "r3"

inherit cmake

SRC_URI = "git://gitorious.org/opensuse/zypper.git;protocol=git \
           file://cmake.patch \
           file://dso_linking_change_build_fix.patch \
           file://rpm5-flag.patch \
          "
S = "${WORKDIR}/git"

PV = "1.5.3-git${SRCPV}"
