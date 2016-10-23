SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ce8d0bd8303802e5492f064ddddef0e7 \
                    file://png.h;endline=149;md5=8cca27e71fd88e1fe1adad7d9e841b5f"
DEPENDS = "zlib"

SRC_URI = "${GENTOO_MIRROR}/libpng-${PV}.tar.xz \
          "
SRC_URI[md5sum] = "faed9bb495d2e12dd0c9ec561ca60cd8"
SRC_URI[sha256sum] = "266743a326986c3dbcee9d89b640595f6b16a293fd02b37d8c91348d317b73f9"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix ${bindir}/pngcp"

BBCLASSEXTEND = "native nativesdk"
