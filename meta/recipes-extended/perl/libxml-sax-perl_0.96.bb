SUMMARY = "Perl module for using and building Perl SAX2 XML processors" 
DESCRIPTION = "XML::SAX consists of several framework classes for using and \
building Perl SAX2 XML parsers, filters, and drivers.  It is designed \ 
around the need to be able to "plug in" different SAX parsers to an \
application without requiring programmer intervention.  Those of you \
familiar with the DBI will be right at home.  Some of the designs \
come from the Java JAXP specification (SAX part), only without the \
javaness."

SECTION = "libs"
LICENSE = "Artistic|GPLv1+"
DEPENDS += "libxml-namespacesupport-perl-native"
RDEPENDS_${PN} += "libxml-namespacesupport-perl perl-module-file-temp"
PR = "r0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=65c4cd8f39c24c7135ed70dacbcb09e3"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GR/GRANTM/XML-SAX-${PV}.tar.gz"
SRC_URI[md5sum] = "bdcd4119a62505184e211e9dfaef0ab1"
SRC_URI[sha256sum] = "9bbef613afa42c46df008d537decc5a61df7e92d65463f3c900769f39e5c8e08"

S = "${WORKDIR}/XML-SAX-${PV}"

inherit cpan

BBCLASSEXTEND = "native"
