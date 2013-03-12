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

SRC_URI[md5sum] = "0aa4e2428e325203d0d7c3e86c961b1c"
SRC_URI[sha256sum] = "894241019a5ff2b7816d20c5bd5a7048fb8f336ca52e97b3cc88d45b16472031"

do_configure() {
	${S}/configure
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
