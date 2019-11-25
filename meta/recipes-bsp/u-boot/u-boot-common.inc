HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
DESCRIPTION = "U-Boot, a boot loader for Embedded boards based on PowerPC, \
ARM, MIPS and several other processors, which can be installed in a boot \
ROM and used to initialize and test the hardware or to download and run \
application code."
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

# Drop this line when updating to 2020.01 final
PV = "2020.01~rc3"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "d4a31e8ee5592072d8d5208b3e950cba2d89b6bd"

SRC_URI = "git://git.denx.de/u-boot.git \
          "

S = "${WORKDIR}/git"
