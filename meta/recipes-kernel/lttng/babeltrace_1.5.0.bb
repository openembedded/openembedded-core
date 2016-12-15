SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "https://bugs.lttng.org/projects/babeltrace"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

DEPENDS = "glib-2.0 util-linux popt bison-native flex-native"

inherit autotools pkgconfig

SRC_URI = "http://www.efficios.com/files/babeltrace/babeltrace-${PV}.tar.bz2 \
"

EXTRA_OECONF = "--disable-debug-info"

SRC_URI[md5sum] = "43696383e44d1b85173db7cbd0335f06"
SRC_URI[sha256sum] = "354e75d74562f5228ab89e5fa16a3b4dffa95e7230c5086b74ffcf11fef60353"

