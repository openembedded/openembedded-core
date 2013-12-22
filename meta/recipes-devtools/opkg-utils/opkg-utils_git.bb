SUMMARY = "Additional utilities for the opkg package manager"
SECTION = "base"
HOMEPAGE = "http://code.google.com/p/opkg/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://opkg.py;beginline=1;endline=18;md5=15917491ad6bf7acc666ca5f7cc1e083"

SRCREV = "757a1664a440c60e8126443bf984e4bdf374c327"
PV = "0.1.8+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/opkg-utils"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install() {
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES_class-native = ""

RDEPENDS_${PN} = "python python-shell python-io python-math python-crypt python-logging python-fcntl python-subprocess python-pickle python-compression python-textutils python-stringold"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
