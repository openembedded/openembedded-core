DESCRIPTION = "A (de)compression library for the ZIP format"
HOMEPAGE = "http://www.info-zip.org"
SECTION = "console/utils"
LICENSE = "Info-ZIP"
LIC_FILES_CHKSUM = "file://LICENSE;md5=94caec5a51ef55ef711ee4e8b1c69e29"
PE = "1"
PR = "r0"

SRC_URI = "ftp://ftp.info-zip.org/pub/infozip/src/unzip60.tgz"
S = "${WORKDIR}/unzip60"

export LD = "${CC}"
LD_virtclass-native = "${CC}"

do_compile() {
        oe_runmake -f unix/Makefile generic
}

do_install() {
        oe_runmake -f unix/Makefile install prefix=${D}${prefix}
	install -d ${D}${mandir}
	mv ${D}${prefix}/man/* ${D}${mandir}
}

BBCLASSEXTEND = "native"
NATIVE_INSTALL_WORKS = "1"
