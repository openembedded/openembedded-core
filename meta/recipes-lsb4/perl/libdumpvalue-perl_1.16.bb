SUMMARY = "Perl module for provides screen dump of Perl data."
DESCRIPTION = "Perl module for provides screen dump of Perl data."

HOMEPAGE = "http://search.cpan.org/~flora/Dumpvalue/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
PR = "r0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=31013c0a67276e1ca20f358868cf99ab"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/F/FL/FLORA/Dumpvalue-${PV}.tar.gz"

SRC_URI[md5sum] = "bef29bf25717db250929bf2b065c4183"
SRC_URI[sha256sum] = "8204ea8f95dc6b137e225702e9bacaa23646ffa8c1c9eef45fa06a7f19f338f2"

S = "${WORKDIR}/Dumpvalue-${PV}"

inherit cpan

BBCLASSEXTEND = "native"
