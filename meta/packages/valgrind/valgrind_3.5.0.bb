DESCRIPTION = "Valgrind memory debugger"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2+ & BSD & FDLv1.2"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=db4e6fac5f1db2d462f2100c9874297b \
                    file://include/valgrind.h;beginline=1;endline=56;md5=432729351dcaf9c2f772316c0ea47343 \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"
DEPENDS = "virtual/libx11"
PR = "r2"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://configurefix.patch"

COMPATIBLE_HOST = 'i.86.*-linux'

inherit autotools

EXTRA_OECONF = "--enable-tls"

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"
