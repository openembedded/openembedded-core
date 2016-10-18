HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

# This revision corresponds to the tag "v2016.03"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "df61a74e6845ec9bdcdd48d2aff5e9c2c6debeaa"

PV = "v2016.03+git${SRCPV}"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master"

S = "${WORKDIR}/git"
