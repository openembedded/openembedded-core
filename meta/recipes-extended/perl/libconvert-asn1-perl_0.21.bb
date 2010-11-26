DESCRIPTION = "Convert::ASN1 - ASN.1 Encode/Decode library"
SECTION = "libs"
LICENSE = "Artistic|GPLv1+"
LIC_FILES_CHKSUM = "file://README;beginline=10;endline=12;md5=a64b291b13ffddeef33b14f047ee8b26"
PR = "r0"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GB/GBARR/Convert-ASN1-${PV}.tar.gz"

S = "${WORKDIR}/Convert-ASN1-${PV}"

inherit cpan

BBCLASSEXTEND="native"

PACKAGE_ARCH = "all"
