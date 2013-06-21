DESCRIPTION = "a line-oriented text editor"
HOMEPAGE = "http://www.gnu.org/software/ed/"
BUGTRACKER = ""

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://ed.h;endline=20;md5=375a20cc2545ac1115eeb7b323c60ae3 \
                    file://main.c;endline=24;md5=1b31246da5e3864d7b30094ff76bf7ed"

SECTION = "base"
PR = "r0"

# LSB states that ed should be in /bin/
bindir = "${base_bindir}"

SRC_URI = "${GNU_MIRROR}/ed/ed-${PV}.tar.gz"

SRC_URI[md5sum] = "2268d2344b3c52d23730acb2e3c942fe"
SRC_URI[sha256sum] = "64c138d33b1ea4b9daa88e045da0619e2a43cb99a9d378417d20163f410a7273"

do_configure() {
	${S}/configure
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
