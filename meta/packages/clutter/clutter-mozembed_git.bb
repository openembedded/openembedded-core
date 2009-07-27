require clutter-mozembed.inc

PV = "0.8.0+git${SRCPV}"
PR = "r3"

SRC_URI = "git://git.clutter-project.org/clutter-mozembed.git;protocol=git \
           file://link-with-g++-da7632f3e2c8d1a70ab01cc7adb63760d8718b41.patch;patch=1;rev=da7632f3e2c8d1a70ab01cc7adb63760d8718b41 \
           file://link-with-g++.patch;patch=1;notrev=da7632f3e2c8d1a70ab01cc7adb63760d8718b41"
S = "${WORKDIR}/git"

do_configure_prepend () {
	touch ${S}/cluttermozembed/dummy.cpp
	touch ${S}/cluttermozembed/dummy2.cpp
}