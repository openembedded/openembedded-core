DESCRIPTION = "PowerTOP, a tool that helps you find what software is using the most power."
HOMEPAGE = "http://www.linuxpowertop.org/"
LICENSE = "GPLv2"
DEPENDS = "ncurses"

PR="r1"

SRC_URI = "http://www.linuxpowertop.org/download/powertop-${PV}.tar.gz \
        file://dump-fixes.patch;patch=1"

CFLAGS += "${LDFLAGS}"

do_configure() {
    # We do not build ncurses with wide char support
    sed -i -e "s/lncursesw/lncurses/" ${S}/Makefile
}

do_install() {
    oe_runmake install DESTDIR=${D}
}
