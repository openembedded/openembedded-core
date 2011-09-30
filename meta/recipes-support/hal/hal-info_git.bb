require hal-info.inc

PV = "${SRCDATE}+git"
PR = "r1"

SRC_URI = "git://anongit.freedesktop.org/hal-info/;protocol=git;rev=HAL_INFO_20091130"
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"
