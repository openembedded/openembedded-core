SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76dbddb73e547d9b3fa16909a98049c0 \
                    file://png.h;endline=15;md5=fd8f7ae7b090a168a9e023d0a5d68a3e \
                    file://png.h;beginline=229;endline=343;md5=44075eedf4763ada355d8f60ac3296c9"
DEPENDS = "zlib"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "

SRC_URI[md5sum] = "23b7286b5d4a86de950fd2ffc5cac742"
SRC_URI[sha256sum] = "42f754df633e4e700544e5913cbe2fd4928bbfccdc07708a5cf84e59827fbe60"

BINCONFIG = "${bindir}/libpng-config ${bindir}/libpng16-config"

inherit autotools binconfig-disabled pkgconfig

# Work around missing symbols
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains("TUNE_FEATURES", "neon", "--enable-arm-neon=on", "--enable-arm-neon=off" ,d)}"

PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "${bindir}/png-fix-itxt ${bindir}/pngfix"

BBCLASSEXTEND = "native nativesdk"
