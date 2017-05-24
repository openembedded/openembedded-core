HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "64c4ffa9fa223f7ae8640f9c8f3044bfa0e3bfda"

SRC_URI = "git://git.denx.de/u-boot.git"

S = "${WORKDIR}/git"
