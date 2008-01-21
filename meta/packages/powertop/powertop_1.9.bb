DESCRIPTION = "PowerTOP, a tool that helps you find what software is using the most power."
HOMEPAGE = "http://www.linuxpowertop.org/"
LICENSE = "GPLv2"
DEPENDS = "ncurses"

SRC_URI = "http://www.linuxpowertop.org/download/powertop-${PV}.tar.gz"

CFLAGS += "${LDFLAGS}"

do_configure() {
    # We do not build ncurses with wide char support
    sed -i -e "s/lncursesw/lncurses/" ${S}/Makefile
}

do_install() {
    oe_runmake install DESTDIR=${D}
}
