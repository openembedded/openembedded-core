DESCRIPTION = "Convert::ASN1 - ASN.1 Encode/Decode library"
SECTION = "libs"
LICENSE = "Artistic|GPLv1+"
LIC_FILES_CHKSUM = "file://README;beginline=10;endline=12;md5=a64b291b13ffddeef33b14f047ee8b26"
PR = "r2"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GB/GBARR/Convert-ASN1-${PV}.tar.gz"

SRC_URI[md5sum] = "64a555e44adc79d92072b9fc7a6779c4"
SRC_URI[sha256sum] = "be63d5cc715d7306e54b41d3c68c3617ca306289cff619a2ca43505e35f2f6ee"

S = "${WORKDIR}/Convert-ASN1-${PV}"

inherit cpan allarch

EXTRA_PERLFLAGS = "-I ${STAGING_LIBDIR_NATIVE}/perl-native/perl/${@get_perl_version(d)}"

BBCLASSEXTEND = "native"
