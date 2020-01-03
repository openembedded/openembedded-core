SUMMARY = "U-Boot libraries and tools to access environment"
DEPENDS += "mtd-utils"

DESCRIPTION = "This package contains tools and libraries to read \
and modify U-Boot environment. \
It provides a hardware-independent replacement for fw_printenv/setenv utilities \
provided by U-Boot"

HOMEPAGE = "https://github.com/sbabic/libubootenv"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c"
SECTION = "libs"

SRC_URI = "git://github.com/sbabic/libubootenv;protocol=https"
SRCREV = "bf6ff631c0e38cede67268ceb8bf1383b5f8848e"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

PROVIDES += "u-boot-fw-utils"
RPROVIDES_${PN} += "u-boot-fw-utils"

BBCLASSEXTEND = "native"
