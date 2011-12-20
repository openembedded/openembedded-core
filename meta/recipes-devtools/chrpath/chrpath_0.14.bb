SUMMARY = "Tool to edit rpath in ELF binaries"
DESCRIPTION = "chrpath allows you to change the rpath (where the \
application looks for libraries) in an application. It does not \
(yet) allow you to add an rpath if there isn't one already."
HOMEPAGE = "http://alioth.debian.org/projects/chrpath/"
BUGTRACKER = "http://alioth.debian.org/tracker/?atid=412807&group_id=31052"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
PR = "r0"

SRC_URI = "http://alioth.debian.org/frs/download.php/3648/chrpath-${PV}.tar.gz \
           file://standarddoc.patch"

SRC_URI[md5sum] = "ea6b212b23393bf58b0ef9bcf6491b86"
SRC_URI[sha256sum] = "a1bc9abc42d2b97efc3a0ced7c5dbed37d5debff600386193750315fa5823eaa"

inherit autotools

# We don't have a staged chrpath-native for ensuring our binary is
# relocatable, so use the one we've just built
CHRPATH_BIN_virtclass-native = "${S}/chrpath"

BBCLASSEXTEND = "native nativesdk"
