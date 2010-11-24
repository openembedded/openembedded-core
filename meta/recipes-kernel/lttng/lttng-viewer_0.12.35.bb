SUMMARY = "Linux Trace Toolkit Viewer"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
HOMEPAGE = "http://lttng.org/content/download"
BUGTRACKER = "n/a"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f650d5f5af1e9648fe0b40e290d3adbb \
                    file://ltt/ltt.h;beginline=2;endline=18;md5=8b7da9190028c50396d97fc85bad0da9 \
                    file://lttv/lttv/traceset.c;beginline=2;endline=17;md5=bcab42863b64b41d153bf81bbe2490a6"
PR = "r0"
DEPENDS = "gtk+ pango popt"

SECTION = "devel"

ALTNAME = "lttv-${PV}-20082010"

SRC_URI = "http://lttng.org/files/packages/${ALTNAME}.tar.gz"

SRC_URI[md5sum] = "ebcbc4e21184b4b0dcb0611b4977dad8"
SRC_URI[sha256sum] = "d258b13cd76c37e4f8395c06159986dd45517bef312895ffa59722087a2879e7"
S = "${WORKDIR}/${ALTNAME}"

inherit autotools

LEAD_SONAME = "liblttvtraceread*"

FILES_${PN} += "\
    ${libdir}/lttv/plugins/*.so \
    ${datadir}/LinuxTraceToolkitViewer/facilities/* \
    ${datadir}/LinuxTraceToolkitViewer/pixmaps/* "
FILES_${PN}-dbg += "${libdir}/lttv/plugins/.debug/"

