DESCRIPTION = "C library and command-line tools for reading and writing tar, cpio, zip, ISO, and other archive formats"
HOMEPAGE = "http://code.google.com/p/libarchive/"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4255e2e6f0349a4ac8fbd68459296e46"
PR = "r0"

DEPENDS = "libxml2"

SRC_URI = "http://libarchive.googlecode.com/files/libarchive-${PV}.tar.gz \
           file://0001-Patch-from-upstream-revision-1990.patch \
           file://0002-Patch-from-upstream-revision-1991.patch \
           file://0003-Patch-from-upstream-rev-2516.patch \
           file://0004-Patch-from-upstream-rev-2514.patch \
           file://0005-Patch-from-upstream-rev-2520.patch \
           file://0006-Patch-from-upstream-rev-2521.patch \
           file://0007-Ignore-ENOSYS-error-when-setting-up-xattrs.-Closes-5.patch \
           "

SRC_URI[md5sum] = "83b237a542f27969a8d68ac217dc3796"
SRC_URI[sha256sum] = "86cffa3eaa28d3116f5d0b20284026c3762cf4a2b52b9844df2b494d4a89f688"

inherit autotools lib_package

BBCLASSEXTEND = "nativesdk"
