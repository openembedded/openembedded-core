# note that we allow for us to use data later than our code version
#
SUMMARY = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD & BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=76ae2becfcb9a685041c6f166b44c2c2"

SRC_URI =" http://www.iana.org/time-zones/repository/releases/tzcode${PV}.tar.gz;name=tzcode \
           http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "b93618bb84e38dee102e0e41ec9d13e2"
SRC_URI[tzcode.sha256sum] = "72325f384490a310eeb2ea0fab7e6f011a5be19adab2ff9d83bf9d1993b066ed"

SRC_URI[tzdata.md5sum] = "b20b3c1618db1984aac685e763de001d"
SRC_URI[tzdata.sha256sum] = "ed8c951008d12f1db55a11e96fc055718c6571233327d9de16a7f8475e2502b0"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
