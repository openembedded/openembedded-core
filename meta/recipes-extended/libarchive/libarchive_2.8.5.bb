DESCRIPTION = "C library and command-line tools for reading and writing tar, cpio, zip, ISO, and other archive formats"
HOMEPAGE = "http://code.google.com/p/libarchive/"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4255e2e6f0349a4ac8fbd68459296e46"
PR = "r0"

DEPENDS = "libxml2"

# We need to repack the tarball due undistributable content on the upstream one.
# More details at http://code.google.com/p/libarchive/issues/detail?id=162
SRC_URI = "http://libarchive.googlecode.com/files/libarchive-${PV}.tar.gz \
           file://0001-Patch-from-upstream-revision-1990.patch \
           file://0002-Patch-from-upstream-revision-1991.patch \
           file://0004-Patch-from-upstream-rev-2514.patch \
           file://0005-Patch-from-upstream-rev-2520.patch \
           file://0006-Patch-from-upstream-rev-2521.patch \
           file://0007-Ignore-ENOSYS-error-when-setting-up-xattrs.-Closes-5.patch \
           "

SRC_URI[md5sum] = "9caf51dcf6213e9c9f5a1c27448b9c90"
SRC_URI[sha256sum] = "13993e0ffbd121ccda46ea226b1f8eac218de0fa8da7d8b1f998093d5c32a72d"

inherit autotools lib_package

BBCLASSEXTEND = "nativesdk"

