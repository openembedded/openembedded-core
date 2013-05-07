DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=d7a19b8c6d8a28785c4cd04ff2e46d27"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013b.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "69d333d829802af4475707e32fa01681"
SRC_URI[tzcode.sha256sum] = "e46ee931927273108db1c6b5ab86c37210e903536a910b35a5699a08799bd6f0"
SRC_URI[tzdata.md5sum] = "489dbca77d1f2e287a3987ca047bb246"
SRC_URI[tzdata.sha256sum] = "70d589d0e76a3749403d2bb404e9214c2520dda4c13e4b07b1b945ed2c64edb2"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
