DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=d7a19b8c6d8a28785c4cd04ff2e46d27"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013d.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "65b6818162230fc02f86f293376c73df"
SRC_URI[tzdata.sha256sum] = "cd1c96f0676e0edceebc6a418a2222ffb05becb41180dd9f847b9c7cef303b04"

SRC_URI[tzcode.md5sum] = "4616a9560270f180eeb9a08540636890"
SRC_URI[tzcode.sha256sum] = "2d9eb90c94644cddb74a490d1184ef9f88efcaa7a2b1bf88be0ee9eeeab707b6"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
