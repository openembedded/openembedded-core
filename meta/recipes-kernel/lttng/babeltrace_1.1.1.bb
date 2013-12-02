SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "https://bugs.lttng.org/projects/babeltrace"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

inherit autotools

DEPENDS = "glib-2.0 util-linux popt"

SRCREV = "a386e24609752742b100120db6798d311fab512a"
PV = "1.1.1+git${SRCPV}"

SRC_URI = "git://git.efficios.com/babeltrace.git"

S = "${WORKDIR}/git"

do_configure_prepend () {
	( cd ${S}; ${S}/bootstrap )
}
