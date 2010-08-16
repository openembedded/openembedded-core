DESCRIPTION = "pax (Portable Archive eXchange) is the POSIX standard archive tool"
HOMEPAGE = "http://www.openbsd.org/cgi-bin/cvsweb/src/bin/pax/"
BUGTRACKER = "http://www.openbsd.org/query-pr.html"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4b0b674dfdc56daa3832d4069b820ea0 \
                    file://src/pax.h;endline=40;md5=309d3e241c1d82069228e5a51e9b8d60 \
                    file://src/cpio.h;endline=40;md5=c3b4bbff6133a83387968617bbae8ac4 \
                    file://lib/vis.h;endline=40;md5=b283f759abd4a5ad7e014b80f51fc053"

SECTION = "base"
PR = "r0"

SRC_URI = "ftp://ftp.suse.com/pub/people/kukuk/pax/pax-${PV}.tar.bz2"

inherit autotools
