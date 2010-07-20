DESCRIPTION = "PowerTOP, a tool that helps you find what software is using the most power."
HOMEPAGE = "http://www.linuxpowertop.org/"
BUGTRACKER = "http://bugzilla.lesswatts.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "virtual/libintl ncurses"

PR = "r0"

SRC_URI = "http://www.lesswatts.org/projects/powertop/download/powertop-${PV}.tar.gz"

CFLAGS += "${LDFLAGS}"

do_configure() {
    # We do not build ncurses with wide char support
    sed -i -e "s/lncursesw/lncurses/" ${S}/Makefile
}

do_install() {
    oe_runmake install DESTDIR=${D}
}
