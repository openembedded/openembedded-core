DESCRIPTION = "Fast lightweight tabbed filemanager"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/pcmanfm.h;endline=22;md5=0fa9129ee918f493e573154f6ec43fb7 \
                    file://src/find-files.c;endline=26;md5=9a92e8f329c97de94e90976a37dde5a5"

SECTION = "x11"
PRIORITY = "optional"
DEPENDS = "gtk+ startup-notification"
DEPENDS_append_poky = " libowl"

PR = "r0"

COMPATIBLE_HOST = '(x86_64|i.86.*|arm.*|mips.*|powerpc.*)-(linux|freebsd.*)'

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/pcmanfm-${PV}.tar.gz \
	   file://gnome-fs-directory.png \
	   file://gnome-fs-regular.png \
	   file://gnome-mime-text-plain.png \
	   file://emblem-symbolic-link.png \
	   file://desktop.patch;patch=1 \
	   file://no-warnings.patch;patch=1 \
	   file://pcmanfm-mips-fix.patch;patch=1"

SRC_URI_append_poky = " file://owl-window-menu.patch;patch=1"

EXTRA_OECONF = "--enable-inotify --disable-hal"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/${datadir}
	install -d ${D}/${datadir}/pixmaps/

	install -m 0644 ${WORKDIR}/*.png ${D}/${datadir}/pixmaps
}

FILES_${PN} += "${datadir}/pixmaps/*.png"
