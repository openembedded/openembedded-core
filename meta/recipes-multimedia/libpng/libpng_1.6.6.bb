SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2ebec5fb1db10234542f80fdd7117e25 \
                    file://png.h;endline=15;md5=41143e20c803af0a376888699ce0980e \
                    file://png.h;beginline=209;endline=323;md5=d30ef0b1dd12d869cb3c6bdb3678e5f4"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
          "

SRC_URI[md5sum] = "3a41dcd58bcac7cc191c2ec80c7fb2ac"
SRC_URI[sha256sum] = "0407965a0057af7a9e4cefe27c062f517d9ad701cdfd20aa91afb6a84f214e70"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
