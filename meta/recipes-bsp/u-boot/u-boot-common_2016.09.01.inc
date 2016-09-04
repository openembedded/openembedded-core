HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "f3363c060497515ca8b71451cb56f3ec0abacaa9"

SRC_URI = "git://git.denx.de/u-boot.git;branch=u-boot-2016.09.y"

S = "${WORKDIR}/git"
