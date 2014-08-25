DESCRIPTION = "md5deep"
AUTHOR = "Jesse Kornblum, Simson L. Garfinkel"
HOMEPAGE = "http://md5deep.sourceforge.net"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=9190f660105b9a56cdb272309bfd5491"

inherit autotools

SRC_URI = "git://github.com/jessek/hashdeep.git \
        file://wrong-variable-expansion.patch \
        "

# Release 4.4
SRCREV = "cd2ed7416685a5e83eb10bb659d6e9bec01244ae"

S = "${WORKDIR}/git"
