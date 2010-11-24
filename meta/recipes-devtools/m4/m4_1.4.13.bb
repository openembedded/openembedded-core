DESCRIPTION = "GNU m4 is an implementation of the traditional Unix macro processor."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI = "${GNU_MIRROR}/m4/m4-${PV}.tar.gz \
           file://ac_config_links.patch;patch=1"
PR = "r2"

inherit autotools

EXTRA_OEMAKE += "'infodir=${infodir}'"
