SUMMARY = "Timezone zoneinfo utils (zic, zdump, tzselect)"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=0b7570113550eb5d30aa4bd220964b8f"

# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2013i.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "86154c8b0cfb47f4e2027ecaa2ec547a"
SRC_URI[tzcode.sha256sum] = "63bba6790afc1cb2ea0af2b998a35fc237e247608140dbe4e08b4216a8c31358"

SRC_URI[tzdata.md5sum] = "8bc69eb75bea496ebe1d5a9ab576702d"
SRC_URI[tzdata.sha256sum] = "1cd56a6ee964143ef0c65229968a5093988335b95e9115235b5e7b7e45e67dda"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
