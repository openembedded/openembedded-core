require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"

SRC_URI_append = " file://use-includedir.patch \
                   file://gmp_fix_for_x32.patch \
                   file://187b7b1646ee.patch \
                   "

SRC_URI[md5sum] = "362cf515aff8dc240958ce47418e4c78"
SRC_URI[sha256sum] = "dfd9aba98fe5caa54a715b4584c7d45eb0ee0c8be9a3181164ad2fad5eefc796"
