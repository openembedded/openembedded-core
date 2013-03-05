require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"

SRC_URI_append = " file://use-includedir.patch \
                   file://gmp_fix_for_x32.patch \
                   file://187b7b1646ee.patch \
                   file://obsolete_automake_macros.patch \
                   "

PR = "r2"                   

SRC_URI[md5sum] = "cf6d7cb5915f29ce0fc41d042205c080"
SRC_URI[sha256sum] = "ed5239a62aeaba6cfc8d50ec36fb59215618f98c248d4bb05ca9bccd990794dc"
