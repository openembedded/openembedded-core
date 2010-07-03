require cairo.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7d7cc3410ae869ed913ebd30d7f45941"

PR = "r0"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://hardcoded_libtool.patch"
