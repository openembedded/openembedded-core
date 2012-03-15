SUMMARY = "Xmu and Xmuu: X Miscellaneous Utility libraries"

DESCRIPTION = "The Xmu Library is a collection of miscellaneous (some \
might say random) utility functions that have been useful in building \
various applications and widgets. This library is required by the Athena \
Widgets. A subset of the functions that do not rely on the Athena \
Widgets (libXaw) or X Toolkit Instrinsics (libXt) are provided in a \
second library, libXmuu."


require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=15f1d981860c677503992da79140669c"

DEPENDS += "libxt libxext"
PROVIDES = "xmu"

PR = "r0"
PE = "1"

XORG_PN = "libXmu"

LEAD_SONAME = "libXmu"

PACKAGES =+ "libxmuu libxmuu-dev"

FILES_libxmuu = "${libdir}/libXmuu.so.*"
FILES_libxmuu-dev = "${libdir}/libXmuu.so"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "a4efff8de85bd45dd3da124285d10c00"
SRC_URI[sha256sum] = "709081c550cc3a866d7c760a3f97384a1fe16e27fc38fe8169b8db9f33aa7edd"
