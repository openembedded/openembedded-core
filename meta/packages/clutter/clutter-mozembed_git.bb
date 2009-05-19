require clutter-mozembed.inc

PV = "0.8.0+git${SRCREV}"
PR = "r2"

SRC_URI = "git://git.clutter-project.org/clutter-mozembed.git;protocol=git \
           file://link-with-g++.patch;patch=1"

S = "${WORKDIR}/git"

do_configure_prepend () {
	touch ${S}/cluttermozembed/dummy.cpp
	touch ${S}/cluttermozembed/dummy2.cpp
}