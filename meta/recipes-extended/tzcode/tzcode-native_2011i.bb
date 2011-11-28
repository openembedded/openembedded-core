DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD"
PR = "r1"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=3ae8198f82258417ce29066d3b034035"

SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2011n.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "cf7f4335b7c8682899fa2814e711c1b2"
SRC_URI[tzcode.sha256sum] = "f0dd991de3f8d6c599c104e294377c9befa1ef40aa5a1d09e2e295a453f3c1ec"
SRC_URI[tzdata.md5sum] = "20dbfb28efa008ddbf6dd34601ea40fa"
SRC_URI[tzdata.sha256sum] = "a343e542486b2b8ebdeca474eed79f1c04f69420ca943c2b9bdea1d2385e38cd"


S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
