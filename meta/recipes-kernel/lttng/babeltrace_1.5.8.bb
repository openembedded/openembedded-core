SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://babeltrace.org/"
BUGTRACKER = "https://bugs.lttng.org/projects/babeltrace"
LICENSE = "MIT & GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

DEPENDS = "glib-2.0 util-linux popt bison-native flex-native"

SRC_URI = "git://git.linuxfoundation.org/diamon/babeltrace.git;branch=stable-1.5"
SRCREV = "054a54ae10b01a271afc4f19496c041b10fb414c"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)$"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-debug-info"

ASNEEDED = ""
