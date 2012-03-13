SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0"
LIC_FILES_CHKSUM = "file://README;beginline=70;md5=94aa5d46682b411a53a5494cfb22640e"
DEPENDS += "libxml-parser-perl"
PR = "r5"

SRC_URI = "http://www.cpan.org/modules/by-module/XML/XML-Simple-${PV}.tar.gz"

SRC_URI[md5sum] = "593aa8001e5c301cdcdb4bb3b63abc33"
SRC_URI[sha256sum] = "a54967c188cda3e20f496c83be4de3f1740eeaa83c0380712ecd969ad8766826"

S = "${WORKDIR}/XML-Simple-${PV}"

EXTRA_PERLFLAGS = "-I ${STAGING_LIBDIR_NATIVE}/perl-native/perl/${@get_perl_version(d)}"

inherit cpan

BBCLASSEXTEND = "native"
