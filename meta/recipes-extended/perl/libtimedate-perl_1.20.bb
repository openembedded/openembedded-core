DESCRIPTION = "TimeDate - parser for absolute times"
SECTION = "libs"
# You can redistribute it and/or modify it under the same terms as Perl itself.
LICENSE = "Artistic|GPLv1+"
LIC_FILES_CHKSUM = "file://README;beginline=21;md5=576b7cb41e5e821501a01ed66f0f9d9e"
PR = "r3"


SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GB/GBARR/TimeDate-${PV}.tar.gz"

S = "${WORKDIR}/TimeDate-${PV}"

inherit cpan allarch

BBCLASSEXTEND = "native"

RDEPENDS_${PN}_virtclass-native = ""
RDEPENDS_${PN} += "perl-module-carp perl-module-exporter perl-module-strict perl-module-time-local"

SRC_URI[md5sum] = "7da7452bce4c684e4238e6d09b390200"
SRC_URI[sha256sum] = "f8251a791f6692c69952b4af697c01df93981ad1ab133279d034656a03cd3755"
