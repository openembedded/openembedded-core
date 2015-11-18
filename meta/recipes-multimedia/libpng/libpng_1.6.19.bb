SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=586a8dac4d039e74af7b68ff2bc5fc41 \
                    file://png.h;endline=17;md5=a38a64f8b5cf0ea4e2d4c2cb47150151 \
                    file://png.h;beginline=19;endline=111;md5=d3e773acb87d7a35863203538167a776"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "
SRC_URI[md5sum] = "1e6a458429e850fc93c1f3b6dc00a48f"
SRC_URI[sha256sum] = "311c5657f53516986c67713c946f616483e3cdb52b8b2ee26711be74e8ac35e8"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
