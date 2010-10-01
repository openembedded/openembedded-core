DESCRIPTION = "Augeas configuration API"
HOMEPAGE = "http://augeas.net/"
BUGTRACKER = "https://fedorahosted.org/augeas/report/1"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbb461211a33b134d42ed5ee802b37ff"

SRC_URI = "http://augeas.net/download/${BP}.tar.gz"

DEPENDS = "readline"

inherit autotools

PACKAGES =+ "${PN}-lenses lib${PN} lib${PN}-dev lib${PN}-dbg"

FILES_${PN}-lenses = "${datadir}/augeas/lenses"
FILES_lib${PN} = "${libdir}/*.so.*"
FILES_lib${PN}-dev = "${libdir}/*.so ${libdir}/*.a ${libdir}/*.la ${includedir} ${libdir}/pkgconfig"
FILES_lib${PN}-dbg = "${libdir}/.debug"

RDEPENDS_lib${PN} += "${PN}-lenses"
RRECOMMENDS_lib${PN} += "${PN}"

LEAD_SONAME = "libaugeas.so"

do_install_append() {
	rm -fr ${D}${datadir}/vim
}
