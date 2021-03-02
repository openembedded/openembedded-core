SUMMARY = "in-depth comparison of files, archives, and directories"
DESCRIPTION = "Tries to get to the bottom of what makes files or directories \
different. It will recursively unpack archives of many kinds and transform \
various binary formats into more human-readable form to compare them. \
It can compare two tarballs, ISO images, or PDF just as easily."
HOMEPAGE = "https://diffoscope.org/"
BUGTRACKER = "https://salsa.debian.org/reproducible-builds/diffoscope/-/issues"
LICENSE = "GPL-3.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PYPI_PACKAGE = "diffoscope"

inherit pypi setuptools3

SRC_URI[md5sum] = "c84d8d308a40176ba2f5dc4abdbf6f73"
SRC_URI[sha256sum] = "0d6486d6eb6e0445ba21fee2e8bdd3a366ce786bfac98e00e5a95038b7815f15"

RDEPENDS_${PN} += "binutils vim squashfs-tools python3-libarchive-c python3-magic"

# Dependencies don't build for musl
COMPATIBLE_HOST_libc-musl = 'null'

BBCLASSEXTEND = "native"
