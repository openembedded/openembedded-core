DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=d7a19b8c6d8a28785c4cd04ff2e46d27"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013b.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "c8bb91ce60092ef61d628d104ad3dab1"
SRC_URI[tzcode.sha256sum] = "fda9a8bd15f06939f0ecd1edba17d5e66d4d526632145609574459b250ab0efb"
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
