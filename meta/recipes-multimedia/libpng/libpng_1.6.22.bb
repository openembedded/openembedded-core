SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=327e99f9f6299935eab27262a3d0d97c \
                    file://png.h;endline=149;md5=5b1126380a15a28a275c88bf13fe39c8"
DEPENDS = "zlib"

SRC_URI = "${GENTOO_MIRROR}/libpng-${PV}.tar.xz \
          "
SRC_URI[md5sum] = "f4ca54a3d945b8678fafed168c503f3b"
SRC_URI[sha256sum] = "6b5a6ad5c5801ec4d24aacc87a0ed7b666cd586478174f69368a1d7747715226"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
