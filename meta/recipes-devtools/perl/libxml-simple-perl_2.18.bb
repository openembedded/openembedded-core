SECTION = "libs"
LICENSE = "Artistic"
LIC_FILES_CHKSUM = "file://README;beginline=70;md5=94aa5d46682b411a53a5494cfb22640e"
DEPENDS += "libxml-parser-perl"
PR = "r0"

SRC_URI = "http://www.cpan.org/modules/by-module/XML/XML-Simple-${PV}.tar.gz"

S = "${WORKDIR}/XML-Simple-${PV}"

inherit cpan
