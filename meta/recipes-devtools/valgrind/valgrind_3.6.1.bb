DESCRIPTION = "Valgrind memory debugger"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2 & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=b4765f122b7672cdf9b2e8fd75a33172 \
                    file://include/valgrind.h;beginline=1;endline=56;md5=13a71cedba99112334d8596162aec37e \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"
DEPENDS = "virtual/libx11"
PR = "r1"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
	   file://fix_issue_caused_by_ccache.patch \
           file://fix_unsupporting_kernel_3.patch \
           file://fixed-perl-path.patch"

SRC_URI[md5sum] = "2c3aa122498baecc9d69194057ca88f5"
SRC_URI[sha256sum] = "49bdcc4fbcf060049b5f0dcfd8a187a6e90e0b0e57309f633b64e44430726a0e"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

inherit autotools

EXTRA_OECONF = "--enable-tls"
EXTRA_OEMAKE = "-w"
PARALLEL_MAKE = ""

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"
