DESCRIPTION = "Xorg minimal fonts data"
HOMEPAGE = "http://www.x.org"
BUGTRACKER = "n/a"

SECTION = "x11/fonts"
LICENSE = "MIT-X"

SRC_URI = "file://misc"

PE = "1"
PR = "r0"

PACKAGES = "${PN}"
PACKAGE_ARCH = "all"
FILES_${PN} = "${libdir}/X11/ ${datadir}/fonts/X11/"

do_install() {
	install -d ${D}/${datadir}/fonts/X11/misc
	install -m 0644 ${WORKDIR}/misc/* ${D}/${datadir}/fonts/X11/misc/
	install -d ${D}/${libdir}/X11
	ln -sf ${datadir}/fonts/X11/ ${D}/${libdir}/X11/fonts -s
}


