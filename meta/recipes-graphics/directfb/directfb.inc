SUMMARY = "Graphics abstraction library for the Linux Framebuffer Device"
DESCRIPTION = "DirectFB is a thin library that provides developers \
with hardware graphics acceleration, input device handling and \
abstraction, an integrated windowing system with support for \
translucent windows and multiple display layers on top of the \
Linux framebuffer device."
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=dcf3c825659e82539645da41a7908589"

HOMEPAGE = "http://directfb.org"
DEPENDS = "jpeg libpng freetype zlib tslib"

SRC_URI = "http://www.directfb.org/downloads/Core/DirectFB-1.7/DirectFB-${PV}.tar.gz \
           file://configurefix.patch \
           file://fusion.patch"

S = "${WORKDIR}/DirectFB-${PV}"

LDFLAGS_append =" -lts -lm"

inherit autotools binconfig pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[jpeg2000] = "--enable-jpeg2000,--disable-jpeg2000,jasper"

EXTRA_OECONF = "\
  --with-gfxdrivers=none \
  --enable-libmpeg3=no \
  --enable-freetype=yes \
  --enable-sdl=no \
  --enable-vnc=no \
  --disable-x11 \
  --disable-mesa \
"

#Once -fno-omit-frame-pointer option of gcc is added into TARGET_CLFAGS as default
#this will cause directfb build failure on x86 arch, so filter out it.
TARGET_CFLAGS_x86 := "${@oe_filter_out('-fno-omit-frame-pointer', '${TARGET_CFLAGS}', d)}"

#PACKAGES_DYNAMIC += "^directfb-inputdrivers-.*"
#
#python populate_packages_prepend () {
#    inputdrivers_libdir = d.expand('${libdir}/directfb-${RV}/inputdrivers')
#    do_split_packages(d, inputdrivers_libdir, '*.so$', 'directfb-inputdrivers-%s', 'Directfb plugin for %s')
#}

# NOTE: monolithic packaging for now, should improve that eventually



FILES_${PN}-dbg += "\
  ${libdir}/directfb-${RV}/*/*/.debug/*.so \
  ${libdir}/directfb-${RV}/*/.debug/*.so \
"

FILES_${PN}-dev += "\
  ${bindir}/directfb-config \
  ${libdir}/directfb-${RV}/systems/*.la \
  ${libdir}/directfb-${RV}/inputdrivers/*.la \
  ${libdir}/directfb-${RV}/interfaces/*/*.la \
  ${libdir}/directfb-${RV}/wm/*.la \
"

FILES_${PN} += "\
  ${libdir}/directfb-${RV}/systems/*.so \
  ${libdir}/directfb-${RV}/inputdrivers/*.so \
  ${libdir}/directfb-${RV}/interfaces/*/*.so \
  ${libdir}/directfb-${RV}/wm/*.so \
  ${datadir}/directfb-${PV} \
"
