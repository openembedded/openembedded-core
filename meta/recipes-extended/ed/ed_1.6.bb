DESCRIPTION = "a line-oriented text editor"
HOMEPAGE = "http://www.gnu.org/software/ed/"
BUGTRACKER = ""

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://ed.h;endline=20;md5=c708cda1b2e8d723d458690b7db03878 \
                    file://main.c;endline=24;md5=1bd039d59e04ee5f82adcc970144a2c3"

SECTION = "base"
PR = "r0"

# LSB states that ed should be in /bin/
bindir = "${base_bindir}"

SRC_URI = "${GNU_MIRROR}/ed/ed-${PV}.tar.gz \
           file://ed-1.2-build.patch"

SRC_URI[md5sum] = "9a78593decccaa889523aa4bb555ed4b"
SRC_URI[sha256sum] = "211c67b0c4aae277d34b1c5f842db1952e468e5905142868e4718ac838f08a65"

do_configure() {
	${S}/configure
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
