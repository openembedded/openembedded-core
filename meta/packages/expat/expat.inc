DESCRIPTION = "Expat is an XML parser library written in C. It is a stream-oriented parser in which an application registers handlers for things the parser might find in the XML document (like start tags)"
HOMEPAGE = "http://expat.sourceforge.net/"
SECTION = "libs"
LICENSE = "MIT"

SRC_URI = "${SOURCEFORGE_MIRROR}/expat/expat-${PV}.tar.gz"

inherit autotools

