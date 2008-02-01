SECTION = "libs"
LICENSE = "Artistic"
DEPENDS += "libxml-parser-perl"
PR = "r0"

SRC_URI = "http://www.cpan.org/modules/by-module/XML/XML-Simple-${PV}.tar.gz"

S = "${WORKDIR}/XML-Simple-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan
