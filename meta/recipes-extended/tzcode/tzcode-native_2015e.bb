# note that we allow for us to use data later than our code version
#
DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=d0ff93a73dd5bc3c6e724bb4343760f6"

SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015e.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "fb62eb6daf3ddb8c41fd40be05ec657e"
SRC_URI[tzcode.sha256sum] = "b5a217b55847fb56f470a7738939c36f3a520d6cc12342d965cfcf848e59ada0"
SRC_URI[tzdata.md5sum] = "36f9056efb432ca945c73397acfce0d4"
SRC_URI[tzdata.sha256sum] = "ffc9b5d38abda8277aa479e3f75aa7668819d0977cd1a0c8ef3b09128334ba6f"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
