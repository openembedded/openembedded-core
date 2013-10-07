DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=0b7570113550eb5d30aa4bd220964b8f"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013g.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "cc2a52297310ba1a673dc60973ea3ad8"
SRC_URI[tzcode.sha256sum] = "c7c358b459bb65cfab7b7bdd6a9689233fc393f1f9fdf4f0f46ca7dac5a9587b"

SRC_URI[tzdata.md5sum] = "76dbc3b5a81913fc0d824376c44a5d15"
SRC_URI[tzdata.sha256sum] = "b6cdd3998dcc732a6ae5e101e1394f9d4d6dff68bd48a8fb78c44c2b997d3a4f"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
