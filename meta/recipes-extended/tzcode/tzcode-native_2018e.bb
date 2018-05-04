# note that we allow for us to use data later than our code version
#
SUMMARY = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD & BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=c679c9d6b02bc2757b3eaf8f53c43fba"

SRC_URI =" http://www.iana.org/time-zones/repository/releases/tzcode${PV}.tar.gz;name=tzcode \
           http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=tzdata \
           "

UPSTREAM_CHECK_URI = "http://www.iana.org/time-zones"

SRC_URI[tzdata.md5sum] = "97d654f4d7253173b3eeb76a836dd65e"
SRC_URI[tzdata.sha256sum] = "6b288e5926841a4cb490909fe822d85c36ae75538ad69baf20da9628b63b692e"
SRC_URI[tzcode.md5sum] = "c4d7df0fff7ba5588b32c5f27e2caf97"
SRC_URI[tzcode.sha256sum] = "ca340cf20e80b699d6e5c49b4ba47361b3aa681f06f38a0c88a8e8308c00ebce"

S = "${WORKDIR}"

inherit native

EXTRA_OEMAKE += "cc='${CC}'"

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
