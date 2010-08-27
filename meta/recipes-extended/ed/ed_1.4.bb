DESCRIPTION = "a line-oriented text editor"
HOMEPAGE = "http://www.gnu.org/software/ed/"
BUGTRACKER = ""

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://ed.h;endline=20;md5=294d5da73d15cd444ca2260fa2538296 \
                    file://main.c;endline=24;md5=122be7f2751ae819c803441972c7f45b"

SECTION = "base"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/ed/ed-${PV}.tar.gz \
           file://ed-1.2-build.patch"

do_configure() {
	${S}/configure
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
