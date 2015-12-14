SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=55b60b5bb3ed92fbf9197e565a7fac02 \
                    file://png.h;endline=112;md5=a6352a338642f0dbd2fc280ae060d3e9"
DEPENDS = "zlib"

SRC_URI = "${GENTOO_MIRROR}/libpng-${PV}.tar.xz \
          "
SRC_URI[md5sum] = "3968acb7c66ef81a9dab867f35d0eb4b"
SRC_URI[sha256sum] = "55c5959e9f3484d96141a3226c53bc9da42a4845e70879d3e1d6e94833d1918b"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
