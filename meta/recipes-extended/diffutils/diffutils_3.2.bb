LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

require diffutils.inc

PR = "${INC_PR}.0"

SRC_URI += "file://remove-gets.patch"

SRC_URI[md5sum] = "22e4deef5d8949a727b159d6bc65c1cc"
SRC_URI[sha256sum] = "2aaaebef615be7dc365306a14caa5d273a4fc174f9f10abca8b60e082c054ed3"
