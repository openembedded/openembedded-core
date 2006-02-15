LICENSE = "GPL"
DESCRIPTION = "procfs tools"
SECTION = "x11"
PRIORITY = "optional"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DEPENDS = "gtk+"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/pcmanfm-${PV}.tar.gz \
	   file://pcmanfm.desktop \
	   file://pcmanfm.png \
	   file://gnome-fs-directory.png \
	   file://gnome-fs-regular.png \
	   file://gnome-mime-text-plain.png \
	   file://emblem-symbolic-link.png \
	   file://no-fam-gtk2.6.patch;patch=1;pnum=1"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/${datadir}
	install -d ${D}/${datadir}/applications
	install -d ${D}/${datadir}/pixmaps/

	install -m 0644 ${WORKDIR}/*.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/pcmanfm.desktop ${D}/${datadir}/applications
}

FILES_${PN} += "${datadir}/applications/pcmanfm.desktop ${datadir}/pixmaps/*.png"

