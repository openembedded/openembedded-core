SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cb7a834ba2891bc30b8577e49963a435 \
                    file://png.h;endline=15;md5=853b11c4a19ec66decd641efd756bc2c \
                    file://png.h;beginline=209;endline=323;md5=cface34a7db6b71eaa828fe934951f81"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "

SRC_URI[md5sum] = "5f414b20f683b1d96b163c89e3eff768"
SRC_URI[sha256sum] = "4003f0fd0e36110a2b742fc5b9e1ab93ed7a7ab57ae8dc65f0e8101458775a56"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
