require patch.inc
LICENSE = "GPLv3"

SRC_URI += "file://patch-CVE-2015-1196.patch"

SRC_URI[md5sum] = "95dd8d7e41dcbcecdd5cd88ef915378d"
SRC_URI[sha256sum] = "c05f28668c3474bc63adcd48abae921d15e71c254fbebdbaeda40456d64039d5"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

acpaths = "-I ${S}/m4 "

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'xattr', 'xattr', '', d)}"
PACKAGECONFIG[xattr] = "--enable-xattr,--disable-xattr,attr," 

