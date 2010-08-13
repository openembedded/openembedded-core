DESCRIPTION = "Convert::ASN1 - ASN.1 Encode/Decode library"
SECTION = "libs"
LICENSE = "Artistic|GPL"
PR = "r0"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GB/GBARR/Convert-ASN1-${PV}.tar.gz"

S = "${WORKDIR}/Convert-ASN1-${PV}"

inherit cpan

BBCLASSEXTEND="native"

PACKAGE_ARCH = "all"
