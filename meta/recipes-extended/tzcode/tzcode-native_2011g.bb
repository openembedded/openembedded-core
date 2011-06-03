DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD"
PR = "r0"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=3ae8198f82258417ce29066d3b034035"

SRC_URI = "ftp://elsie.nci.nih.gov/pub/tzcode${PV}.tar.gz;name=tzcode \
           ftp://elsie.nci.nih.gov/pub/tzdata2011g.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "ecb564279b28c5b184421c525d997d6c"
SRC_URI[tzcode.sha256sum] = "636c735d8df1276cc8ab88bc31bb36a21f91ed34e26d181303ecd8fe48021bc2"
SRC_URI[tzdata.md5sum] = "a068c27e7e426fdb12ab0c88506df20d"
SRC_URI[tzdata.sha256sum] = "01a2a189eeda63baacc5e68e6c13afffc7edce182102fffc53acbf35e8703d3c"


S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
