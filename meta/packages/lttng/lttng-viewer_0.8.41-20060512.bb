SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"
PR = "r1"
DEPENDS = "gtk+ pango popt"

ALTNAME = "LinuxTraceToolkitViewer-0.8.41-12052006"

SRC_URI = "http://ltt.polymtl.ca/packages/${ALTNAME}.tar.gz "
S = "${WORKDIR}/${ALTNAME}"

inherit autotools

LEAD_SONAME = "liblttvtraceread*"

FILES_${PN} += "\
    ${libdir}/lttv/plugins/* \
    ${datadir}/LinuxTraceToolkitViewer/facilities/* \
    ${datadir}/LinuxTraceToolkitViewer/pixmaps/* "
FILES_${PN}-dbg += "${libdir}/lttv/plugins/.debug/"
