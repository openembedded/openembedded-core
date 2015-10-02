# note that we allow for us to use data later than our code version
#
DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"
LICENSE = "PD & BSD"

LIC_FILES_CHKSUM = "file://${WORKDIR}/README;md5=d0ff93a73dd5bc3c6e724bb4343760f6"

SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015g.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "a2c47d908a6426f530efb1393cf1cd06"
SRC_URI[tzcode.sha256sum] = "18e402ef24bfad2ded38643c9a7a9a580f940a729cb47d983052fc28ff0c7ec4"
SRC_URI[tzdata.md5sum] = "8d46e8b225b9a04c75f5c39636435ad6"
SRC_URI[tzdata.sha256sum] = "b923cdbf078491696b17bc8d069c74bce73fabc5774629da2f410c9b31576161"

S = "${WORKDIR}"

inherit native

do_install () {
        install -d ${D}${bindir}/
        install -m 755 zic ${D}${bindir}/
        install -m 755 zdump ${D}${bindir}/
        install -m 755 tzselect ${D}${bindir}/
}
