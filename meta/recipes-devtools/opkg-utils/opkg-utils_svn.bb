DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
HOMEPAGE = "http://wiki.openmoko.org/wiki/Opkg"
PRIORITY = "optional"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://opkg.py;beginline=1;endline=18;md5=15917491ad6bf7acc666ca5f7cc1e083"
RDEPENDS_${PN} = "python"
RDEPENDS_${PN}_virtclass-native = ""
PV = "0.1.8+svnr${SRCPV}"
PR = "r2"

SRC_URI = "svn://svn.openmoko.org/trunk/src/host/;module=opkg-utils;proto=http \
           file://index-ignore-filenotfound.patch"

S = "${WORKDIR}/opkg-utils"

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES_virtclass-native = ""

do_install() {
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
