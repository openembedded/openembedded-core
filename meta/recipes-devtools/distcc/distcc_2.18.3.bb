SUMMARY = "A parallel build system"
DESCRIPTION = "distcc is a parallel build system that distributes \
compilation of C/C++/ObjC code across machines on a network."
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r7"

DEPENDS = "avahi ${GTKDEP}"
GTKDEP_libc-uclibc = ""
GTKDEP = "gtk+"

RRECOMMENDS_${PN} = "avahi-daemon"

# Upstream change this patch periodically so store locally
# http://0pointer.de/public/distcc-avahi.patch
SRC_URI = "http://distcc.googlecode.com/files/${BPN}-${PV}.tar.bz2 \
           file://distcc-avahi.patch \	  
           file://makefile-param-order.patch \
	   file://default \
	   file://distccmon-gnome.desktop \
	   file://distcc"

SRC_URI[md5sum] = "0d6b80a1efc3a3d816c4f4175f63eaa2"
SRC_URI[sha256sum] = "6500f1bc2a30b1f044ebed79c6ce15457d1712263e65f0db7d6046af262ba434"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "distcc"

EXTRA_OECONF = "--with-gtk"
EXTRA_OECONF_libc-uclibc = "--without-gtk --without-gnome"
do_install_append() {
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/default
    install -m 0755 ${WORKDIR}/distcc ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/default ${D}${sysconfdir}/default/distcc
    ${DESKTOPINSTALL}
}
DESKTOPINSTALL = ""
DESKTOPINSTALL_libc-glibc () {
    install -d ${D}${datadir}/distcc/
    install -m 0644 ${WORKDIR}/distccmon-gnome.desktop ${D}${datadir}/distcc/
}
PACKAGES += "distcc-distmon-gnome"

FILES_${PN} = " ${sysconfdir} \
		${bindir}/distcc \
		${bindir}/distccd \
		${bindir}/distccmon-text"
FILES_distcc-distmon-gnome = "  ${bindir}/distccmon-gnome \
				${datadir}/distcc"

pkg_postinst_${PN} () {
	if test "x$D" != "x"; then
		exit 1
	else
		grep distcc /etc/passwd || adduser --system --home /dev/null --no-create-home --empty-password --ingroup nogroup distcc
	fi
}
