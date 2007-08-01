SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"
PR = "r2"
DEPENDS = "gtk+ pango popt"

ALTNAME = "LinuxTraceToolkitViewer-${PV}-13062007"

SRC_URI = "http://ltt.polymtl.ca/packages/${ALTNAME}.tar.gz"
S = "${WORKDIR}/${ALTNAME}"

inherit autotools

LEAD_SONAME = "liblttvtraceread*"

FILES_${PN} += "\
    ${libdir}/lttv/plugins/*.so \
    ${datadir}/LinuxTraceToolkitViewer/facilities/* \
    ${datadir}/LinuxTraceToolkitViewer/pixmaps/* "
FILES_${PN}-dbg += "${libdir}/lttv/plugins/.debug/"
