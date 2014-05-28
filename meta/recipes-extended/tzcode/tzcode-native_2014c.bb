DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=0b7570113550eb5d30aa4bd220964b8f"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014c.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "657636f201ae8b561225afde49d274af"
SRC_URI[tzcode.sha256sum] = "1461e6f6973797ccf516421b272a28082fe26754e8ae94967d8f931a4c8f011b"
SRC_URI[tzdata.md5sum] = "fa7f3dc1c6f6238253a6f2b850467e2e"
SRC_URI[tzdata.sha256sum] = "aa2924e012644de3a6cc3160ffff690cb3afecf350af435daefa43bb669fb8f1"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
