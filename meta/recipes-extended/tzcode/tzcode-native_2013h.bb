SUMMARY = "Timezone zoneinfo utils (zic, zdump, tzselect)"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=0b7570113550eb5d30aa4bd220964b8f"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013h.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "d310abe42cbe87e76ceb69e2c7003c92"
SRC_URI[tzdata.sha256sum] = "6b9e17e823eec0e09e12f74b452a70be4face1ef14c2fb1917b7c7e60564de27"

SRC_URI[tzcode.md5sum] = "14250703b253e1cfdf97f1e928541508"
SRC_URI[tzcode.sha256sum] = "e14addfc4e0da3cf17ccc1c08cb8094a2a0d3ae9524f565b74e6373c9b83ca0e"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
