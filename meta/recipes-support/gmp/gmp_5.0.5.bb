require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"

SRC_URI_append = " file://use-includedir.patch \
                   file://gmp_fix_for_x32.patch \
                   file://gmp_fix_for_automake-1.12.patch \
                   "

SRC_URI[md5sum] = "041487d25e9c230b0c42b106361055fe"
SRC_URI[sha256sum] = "1f588aaccc41bb9aed946f9fe38521c26d8b290d003c5df807f65690f2aadec9"
