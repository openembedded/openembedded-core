SECTION = "libs"
SRC_URI = "http://www.cpan.org/modules/by-module/XML/XML-Parser-2.34.tar.gz"
LICENSE = "Artistic"
S = "${WORKDIR}/XML-Parser-${PV}"

inherit cpan
