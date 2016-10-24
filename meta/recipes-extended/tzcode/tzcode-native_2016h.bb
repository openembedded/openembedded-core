# note that we allow for us to use data later than our code version
#
SUMMARY = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD & BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=ef1a352b901ee7b75a75df8171d6aca7"

SRC_URI =" http://www.iana.org/time-zones/repository/releases/tzcode${PV}.tar.gz;name=tzcode \
           http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "00c20689d996dea4cf5b45504724ce8f"
SRC_URI[tzcode.sha256sum] = "30e62f0b86a78fb020d378b950930da023ca31b1a58f08d8fb2066627c4d6566"
SRC_URI[tzdata.md5sum] = "878f0ec3fd9e4026ea11dd1b649a315a"
SRC_URI[tzdata.sha256sum] = "da1b74fc2dec2ce8b64948dafb0bfc2f923c830d421a7ae4d016226135697a64"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
