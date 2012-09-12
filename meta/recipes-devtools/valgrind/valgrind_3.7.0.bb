DESCRIPTION = "Valgrind memory debugger"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2 & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=0ef036a7ddce4cdc738d65d63b3e8153 \
                    file://include/valgrind.h;beginline=1;endline=56;md5=aee56014c1dd64260a59fd4df38752f6 \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"

X11DEPENDS = "virtual/libx11"
DEPENDS = "${@base_contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"
PR = "r7"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
	   file://fix_issue_caused_by_ccache.patch \
           file://fix_unsupporting_kernel_3.patch \
           file://fixed-perl-path.patch \
           file://fix_for_automake_1.11.2.patch \
           file://configure-fix.patch \
           file://Added-support-for-PPC-instructions-mfatbu-mfatbl.patch"

SRC_URI_append_powerpc = " file://valgrind-3.7.0-fix-error-of-reading-debug-info.patch"
SRC_URI_append_powerpc64 = " file://valgrind-3.7.0-fix-error-of-reading-debug-info.patch"

SRC_URI[md5sum] = "a855fda56edf05614f099dca316d1775"
SRC_URI[sha256sum] = "5d62c0330f1481fe2c593249192fa68ff454c19c34343978cc9ce91aa324cbf6"

COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64).*-linux'

inherit autotools

EXTRA_OECONF = "--enable-tls"
EXTRA_OEMAKE = "-w"
PARALLEL_MAKE = ""

do_install_append () {
    install -m 644 ${S}/default.supp ${D}/${libdir}/valgrind/
}

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"
RRECOMMENDS_${PN}_powerpc += "${TCLIBC}-dbg"
RRECOMMENDS_${PN}_powerpc64 += "${TCLIBC}-dbg"
