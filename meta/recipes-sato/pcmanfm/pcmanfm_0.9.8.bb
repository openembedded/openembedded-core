DESCRIPTION = "Fast lightweight tabbed filemanager"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/pcmanfm.h;endline=22;md5=417b3855771a3a87f8ad753d994491f0 \
                    file://src/gseal-gtk-compat.h;endline=21;md5=46922c8691f58d124f9420fe16149ce2"

SECTION = "x11"
DEPENDS = "gtk+ startup-notification libfm"
DEPENDS_append_poky = " libowl"

PR = "r0"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|mips.*|powerpc.*)-(linux|freebsd.*)'

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/pcmanfm-${PV}.tar.gz \
	   file://gnome-fs-directory.png \
	   file://gnome-fs-regular.png \
	   file://gnome-mime-text-plain.png \
	   file://emblem-symbolic-link.png \
	   file://cross-compile-fix.patch" 

SRC_URI_append_poky = " file://owl-window-menu.patch"

SRC_URI[md5sum] = "4a7fdc0526ed14e3293d784c0ce27dea"
SRC_URI[sha256sum] = "a9335baf127d133da1b2873b3065545bd278fadd4b33e4b91e3d10b165961b68"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/${datadir}
	install -d ${D}/${datadir}/pixmaps/

	install -m 0644 ${WORKDIR}/*.png ${D}/${datadir}/pixmaps
}
