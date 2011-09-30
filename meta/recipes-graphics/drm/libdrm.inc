SUMMARY = "Userspace interface to the kernel DRM services"
DESCRIPTION = "The runtime library for accessing the kernel DRM services.  DRM \
stands for \"Direct Rendering Manager\", which is the kernel portion of the \
\"Direct Rendering Infrastructure\" (DRI).  DRI is required for many hardware \
accelerated OpenGL drivers."
HOMEPAGE = "http://dri.freedesktop.org"
SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "http://dri.freedesktop.org/libdrm/libdrm-${PV}.tar.bz2"
PROVIDES = "drm"
DEPENDS = "libpthread-stubs udev cairo virtual/libx11"

#libpciaccess is required starting from libdrm 2.4.26
DEPENDS += " libpciaccess"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-tests ${PN}-drivers ${PN}-kms"
FILES_${PN}-tests = "${bindir}/dr* ${bindir}/mode*"
FILES_${PN}-drivers = "${libdir}/libdrm_*.so.*"
FILES_${PN}-kms = "${libdir}/libkms*.so.*"

