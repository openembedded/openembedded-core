SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=44bc22578be94b6536c8bdc3a01e5db9 \
                    file://png.h;endline=15;md5=aa42e53406a5ebb9500bae72fa314b6d \
                    file://png.h;beginline=209;endline=323;md5=83e13159b5bf0a4d9fb634acbda7cb5b"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
           file://0001-configure-lower-automake-requirement.patch \
          "

SRC_URI[md5sum] = "51ce71a1642cdde1f4485a7ff82193c0"
SRC_URI[sha256sum] = "24f73d8b5e1d74a9482c81b65c3f93f96c7da7ed0417b8a948a75d2d99133081"

inherit autotools binconfig pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_arm = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
