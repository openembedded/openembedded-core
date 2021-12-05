SUMMARY = "Tool to allow editing of RPATH and interpreter fields in ELF binaries"
DESCRIPTION = "PatchELF is a simple utility for modifying existing ELF executables and libraries."
HOMEPAGE = "https://github.com/NixOS/patchelf"

LICENSE = "GPLv3"

SRC_URI = "git://github.com/NixOS/patchelf;protocol=https;branch=master \
           file://handle-read-only-files.patch \
           "
SRCREV = "2ac5798fdc79762ceee520b2b3b4758f7f97b17e"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

inherit autotools

BBCLASSEXTEND = "native nativesdk"
