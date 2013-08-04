SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a42d0002ce5c11e4a529c29f78d13dd \
                    file://png.h;beginline=207;endline=321;md5=de933190ded7f9d6c22110c8509ea85a"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
          "
SRC_URI[md5sum] = "9307f8ab04f4be3c1889ffb504f0fe5c"
SRC_URI[sha256sum] = "c5ecc0d38663b944a828145ed59cf15dfbad2217512875bc05ecf9b92afa0f45"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
