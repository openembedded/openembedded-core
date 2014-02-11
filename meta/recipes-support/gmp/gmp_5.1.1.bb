require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"

SRC_URI_append = " file://use-includedir.patch \
                   file://append_user_provided_flags.patch \
                   "

SRC_URI[md5sum] = "2fa018a7cd193c78494525f236d02dd6"
SRC_URI[sha256sum] = "a0d4779f48b36519dfaceb5f987a7c76fcac223258bebea3bb2244310970afad"
