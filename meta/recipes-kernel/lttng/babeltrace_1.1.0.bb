SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "https://bugs.lttng.org/projects/babeltrace"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

inherit autotools

DEPENDS = "glib-2.0 util-linux popt"

SRCREV = "c99b1910bea848e8f0ae5641bb63b8f4f84f3ec0"
PV = "1.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.efficios.com/babeltrace.git;protocol=git"

S = "${WORKDIR}/git"

do_configure_prepend () {
	( cd ${S}; ${S}/bootstrap )
}
