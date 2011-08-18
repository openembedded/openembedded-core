DESCRIPTION = "utilities for manipulating filesystem extended attributes"
HOMEPAGE = "http://savannah.nongnu.org/projects/attr/"
SECTION = "libs"

DEPENDS = "ncurses"

LICENSE = "LGPLv2.1+ & GPLv2+"
LICENSE_${PN} = "GPLv2+"
LICENSE_lib${BPN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=2d0aa14b3fce4694e4f615e30186335f \
                    file://attr/attr.c;endline=17;md5=be0403261f0847e5f43ed5b08d19593c \
                    file://libattr/libattr.c;endline=17;md5=7970f77049f8fa1199fff62a7ab724fb"

SRC_URI = "http://download.savannah.gnu.org/releases/attr/${BP}.src.tar.gz"

require ea-acl.inc

# libdir should point to .la
do_install_append() {
	sed -i ${D}${libdir}/libattr.la -e \
	    s,^libdir=\'${base_libdir}\'$,libdir=\'${libdir}\',
}
