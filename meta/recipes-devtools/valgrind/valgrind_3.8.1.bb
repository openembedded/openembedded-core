DESCRIPTION = "Valgrind memory debugger"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2 & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=6b18ba0139d10678ce3a9969f68e4c6d \
                    file://include/valgrind.h;beginline=1;endline=56;md5=b6bb5ab625a759823e17197ec3e2ee83 \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"

X11DEPENDS = "virtual/libx11"
DEPENDS = "${@base_contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"
PR = "r8"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
	   file://fix_issue_caused_by_ccache.patch \
           file://fix_unsupporting_kernel_3.patch \
           file://fixed-perl-path.patch \
           file://Added-support-for-PPC-instructions-mfatbu-mfatbl.patch \
           file://configure-with-newer-glibc.patch \
           file://sepbuildfix.patch \
          "

SRC_URI[md5sum] = "288758010b271119a0ffc0183f1d6e38"
SRC_URI[sha256sum] = "473be00576bed311a662b277a2bfbe97d9cca4058e68619a0e420c9fc19958db"

COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64).*-linux'
COMPATIBLE_HOST_armv7a = 'arm.*-linux'

inherit autotools

EXTRA_OECONF = "--enable-tls --without-mpicc"
EXTRA_OECONF_armv7a = "--enable-tls -host=armv7-none-linux-gnueabi --without-mpicc"
EXTRA_OEMAKE = "-w"
PARALLEL_MAKE = ""

do_install_append () {
    install -m 644 ${B}/default.supp ${D}/${libdir}/valgrind/
}

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"
RRECOMMENDS_${PN}_powerpc += "${TCLIBC}-dbg"
RRECOMMENDS_${PN}_powerpc64 += "${TCLIBC}-dbg"
RRECOMMENDS_${PN}_armv7a += "${TCLIBC}-dbg"
