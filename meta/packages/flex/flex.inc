DESCRIPTION = "Flex is a tool for generating programs that recognize lexical patterns in text."
HOMEPAGE = "http://sourceforge.net/projects/flex/"

SECTION = "devel"
LICENSE = "BSD"
DEPENDS = "gettext"

SRC_URI = "${SOURCEFORGE_MIRROR}/flex/flex-${PV}.tar.bz2 "

inherit autotools

