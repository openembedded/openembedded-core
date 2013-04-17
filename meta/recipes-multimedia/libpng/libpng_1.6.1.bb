SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8273188b2e21c831f5a09fd9285db62f \
                    file://png.h;beginline=207;endline=321;md5=de107fb61766e9d826943f3b6a354fc9"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
           file://transform.patch \
          "
SRC_URI[md5sum] = "93fc0b0841ce2db0e6756673e22dafc3"
SRC_URI[sha256sum] = "5ef57f8b9ef591c8504e2a8f78d31779f0c8f2b34b34d01d533360d2483c8946"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

BBCLASSEXTEND = "native nativesdk"
