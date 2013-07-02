SUMMARY = "Linux Trace Toolkit Userspace Tracer 2.0"
DESCRIPTION = "The LTTng UST 2.0 package contains the userspace tracer library to trace userspace codes."
HOMEPAGE = "http://lttng.org/ust"
BUGTRACKER = "https://bugs.lttng.org/projects/lttng-ust"

LICENSE = "LGPLv2.1+ & BSD & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=c963eb366b781252b0bf0fdf1624d9e9 \
                    file://snprintf/snprintf.c;endline=32;md5=d3d544959d8a3782b2e07451be0a903c \
                    file://snprintf/various.h;endline=31;md5=89f2509b6b4682c4fc95255eec4abe44"

inherit autotools

DEPENDS = "liburcu util-linux"

# For backwards compatibility after rename
RPROVIDES_${PN} = "lttng2-ust"
RREPLACES_${PN} = "lttng2-ust"
RCONFLICTS_${PN} = "lttng2-ust"

SRCREV = "cfaee541fea7f5760b5501913c6902f5e4da9dba"
PV = "2.2.0"
PE = "2"

SRC_URI = "git://git.lttng.org/lttng-ust.git;protocol=git \
	   file://depends-liblttng-ust-tracepoin.patch \
	   file://build-Fix-out-of-tree-build.patch \
	   "

S = "${WORKDIR}/git"

do_configure_prepend () {
	( cd ${S}; ${S}/bootstrap )
}
