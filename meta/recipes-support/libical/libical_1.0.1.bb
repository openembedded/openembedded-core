SUMMARY = "iCal and scheduling (RFC 2445, 2446, 2447) library"
HOMEPAGE = "https://github.com/libical/libical"
BUGTRACKER = "https://github.com/libical/libical/issues"
LICENSE = "LGPLv2.1 | MPL-1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d4fc58309d8ed46587ac63bb449d82f8 \
                    file://LICENSE;md5=d1a0891cd3e582b3e2ec8fe63badbbb6"
SECTION = "libs"

SRC_URI = "https://github.com/${BPN}/${BPN}/archive/v${PV}.tar.gz \
           file://Remove-cmake-check-for-Perl.patch \
           file://Fix-x32-ABI-build.patch \
           file://Depend-on-headers-to-fix-parallel-build.patch \
           "
SRC_URI[md5sum] = "af91db06b22559f863869c5a382ad08a"
SRC_URI[sha256sum] = "7d5f613454ec6c7d1bcfb441c919215be53292aa15cd1cb14249d1413d6c610c"
UPSTREAM_CHECK_URI = "https://github.com/libical/libical/releases"

inherit cmake

FILES_${PN}-dev += "${libdir}/cmake/*"
