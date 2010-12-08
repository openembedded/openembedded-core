DESCRIPTION = "Convert::ASN1 - ASN.1 Encode/Decode library"
SECTION = "libs"
LICENSE = "Artistic|GPLv1+"
LIC_FILES_CHKSUM = "file://README;beginline=10;endline=12;md5=a64b291b13ffddeef33b14f047ee8b26"
PR = "r0"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GB/GBARR/Convert-ASN1-${PV}.tar.gz"

SRC_URI[md5sum] = "6f5c45724db1b09911e489275d19d0f5"
SRC_URI[sha256sum] = "94e37fcb52148355cf9a0e96518c82bbb80b0b97adcce88bdb87766ca1cf9e45"

S = "${WORKDIR}/Convert-ASN1-${PV}"

inherit cpan

BBCLASSEXTEND="native"

PACKAGE_ARCH = "all"
