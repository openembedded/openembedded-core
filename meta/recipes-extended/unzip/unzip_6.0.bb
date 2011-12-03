DESCRIPTION = "A (de)compression library for the ZIP format"
HOMEPAGE = "http://www.info-zip.org"
SECTION = "console/utils"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=94caec5a51ef55ef711ee4e8b1c69e29"
PE = "1"
PR = "r2"

SRC_URI = "ftp://ftp.info-zip.org/pub/infozip/src/unzip60.tgz \
	file://avoid-strip.patch"

SRC_URI[md5sum] = "62b490407489521db863b523a7f86375"
SRC_URI[sha256sum] = "036d96991646d0449ed0aa952e4fbe21b476ce994abc276e49d30e686708bd37"
S = "${WORKDIR}/unzip60"

EXTRA_OEMAKE += "STRIP=true LF2=''"

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
