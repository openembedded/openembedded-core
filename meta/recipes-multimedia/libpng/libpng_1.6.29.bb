SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f9acafc1325f8f8da80aeee94dbc1bda \
                    file://png.h;endline=144;md5=0b2aa20c7ff55d9411108317df8ff0c9"
DEPENDS = "zlib"

SRC_URI = "${GENTOO_MIRROR}/libpng-${PV}.tar.xz \
          "
SRC_URI[md5sum] = "3245dbd76ea91e1437507357b858ec97"
SRC_URI[sha256sum] = "4245b684e8fe829ebb76186327bb37ce5a639938b219882b53d64bd3cfc5f239"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix ${bindir}/pngcp"

BBCLASSEXTEND = "native nativesdk"
