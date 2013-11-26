SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=243135ddedf702158f9170807cbcfb66 \
                    file://png.h;endline=15;md5=cb4981447519fe0bb87fbda5e8cd50be \
                    file://png.h;beginline=209;endline=323;md5=f6b7821e0ddefaa4d71fdfa1f5a91fb7"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
          "

SRC_URI[md5sum] = "7023a9eacd7b6a3eb95761a2f574d456"
SRC_URI[sha256sum] = "13c9c853a9a600218fff9961658dc4f485ad2ef9b862315b434dd2fdbbe1f945"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
