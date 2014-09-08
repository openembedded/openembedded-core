SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=46401ee4b225b9ec066cb96cf1025c0f \
                    file://png.h;endline=15;md5=8167a17735fc618d1df109f8b0f839a6 \
                    file://png.h;beginline=229;endline=343;md5=5cdf8564a14e2f00339e4437a83b4913"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "

SRC_URI[md5sum] = "9822c25466f060142359f80ed142c9e5"
SRC_URI[sha256sum] = "d9c8ce54a5fc8052ed794ca65b553384a74c0608b09ae163cbbb07176018e625"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
