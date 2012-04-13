DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
HOMEPAGE = "http://wiki.openmoko.org/wiki/Opkg"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://opkg.py;beginline=1;endline=18;md5=15917491ad6bf7acc666ca5f7cc1e083"
RDEPENDS_${PN} = "python"
RDEPENDS_${PN}_virtclass-native = ""
SRCREV = "16665959c330b5958c0f0f4624a9ca7f823f98de"
PV = "0.1.8+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.yoctoproject.org/opkg-utils;protocol=git \
           file://index-ignore-filenotfound.patch \
           file://mtime-int.patch \
           file://add-license-field.patch \
           file://arfile_header_split.patch \
           file://shebang.patch \
           "

S = "${WORKDIR}/git"

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES_virtclass-native = ""

do_install() {
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
TARGET_CC_ARCH += "${LDFLAGS}"
