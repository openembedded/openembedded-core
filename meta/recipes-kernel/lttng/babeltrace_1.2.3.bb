SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "https://bugs.lttng.org/projects/babeltrace"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

inherit autotools pkgconfig

DEPENDS = "glib-2.0 util-linux popt bison-native flex-native"

SRCREV = "753a40ead9fa4f6bd5e29d5765e2021122df9c1c"
PV = "1.2.3+git${SRCPV}"

SRC_URI = "git://git.efficios.com/babeltrace.git;branch=stable-1.2"

S = "${WORKDIR}/git"

do_configure_prepend () {
	( cd ${S}; ${S}/bootstrap )
}
