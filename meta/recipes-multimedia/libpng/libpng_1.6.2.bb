SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b7c6b3f443bd29d45da5575f36293606 \
                    file://png.h;beginline=207;endline=321;md5=f8e34d28a077eca674b739ded0de063c"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
          "
SRC_URI[md5sum] = "9d838f6fca9948a9f360a0cc1b516d5f"
SRC_URI[sha256sum] = "1c97a90bc22107e50f04f77a0115f4ec890d5c6a373ac4c560e8fb87259e92de"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

BBCLASSEXTEND = "native nativesdk"
