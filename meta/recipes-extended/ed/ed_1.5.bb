DESCRIPTION = "a line-oriented text editor"
HOMEPAGE = "http://www.gnu.org/software/ed/"
BUGTRACKER = ""

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://ed.h;endline=20;md5=238781cd8e436dabb0e9f18443400066 \
                    file://main.c;endline=24;md5=177fe82d0649e545591a13fdce5733e8"

SECTION = "base"
PR = "r1"

# LSB states that ed should be in /bin/
bindir = "${base_bindir}"

SRC_URI = "${GNU_MIRROR}/ed/ed-${PV}.tar.gz \
           file://ed-1.2-build.patch"

SRC_URI[md5sum] = "e66c03d7e4c67b025d5b6093ec678267"
SRC_URI[sha256sum] = "97dd34a49ebc9c97e414b90a087d63eafc41377a340848c97e75a9cba187fba1"

do_configure() {
	${S}/configure
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
