# note that we allow for us to use data later than our code version
#
SUMMARY = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD & BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=76ae2becfcb9a685041c6f166b44c2c2"

SRC_URI =" http://www.iana.org/time-zones/repository/releases/tzcode${PV}.tar.gz;name=tzcode \
           http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "527e73bc90c70746799d61006bcf038e"
SRC_URI[tzcode.sha256sum] = "e935c4fe78b5c5da3791f58f3ab7f07fb059a7c71d6b62b69ef345211ae5dfa7"
SRC_URI[tzdata.md5sum] = "f638ec0d4d7a17f001ce475860255c85"
SRC_URI[tzdata.sha256sum] = "6392091d92556a32de488ea06a055c51bc46b7d8046c8a677f0ccfe286b3dbdc"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
