DESCRIPTION = "Screen is a full-screen window manager \
that multiplexes a physical terminal between several \
processes, typically interactive shells."
HOMEPAGE = "http://www.gnu.org/software/screen/"
BUGTRACKER = "https://savannah.gnu.org/bugs/?func=additem&group=screen"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0774d66808b0f602e94448108f59448b \
                    file://screen.h;endline=23;md5=9a7ae69a2aafed891bf7c38ddf9f6b7d"

SECTION = "console/utils"
DEPENDS = "ncurses"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/screen/screen-${PV}.tar.gz \
           ${DEBIAN_MIRROR}/main/s/screen/screen_4.0.3-11+lenny1.diff.gz \
           file://configure.patch"

inherit autotools

EXTRA_OECONF = "--with-pty-mode=0620 --with-pty-group=5"

