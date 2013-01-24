DESCRIPTION = "Libcgroup"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

PR = "r2"

inherit autotools pkgconfig

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libcg/${BPN}/v${PV}/${BPN}-${PV}.tar.bz2"
SRC_URI[md5sum] = "24a41b18de112e8d085bb1f7d9e82af7"
SRC_URI[sha256sum] = "0b08632de5d3641aa554b422d02a77d9367e57933328a8765204ad9588cd5c0d"

EXTRA_OECONF = "${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam-module-dir=${base_libdir}/security --enable-pam=yes', '--enable-pam=no', d)}"

# http://www.mail-archive.com/openembedded-devel@lists.openembedded.org/msg21444.html
PARALLEL_MAKE = ""

PACKAGES =+ "cgroups-pam-plugin"
FILES_cgroups-pam-plugin = "${base_libdir}/security/pam_cgroup.so*"
FILES_${PN}-dbg += "${base_libdir}/security/.debug"
FILES_${PN}-dev += "${base_libdir}/security/*.la"

do_install_append() {
	# Moving libcgroup to base_libdir
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mkdir -p ${D}/${base_libdir}/
		mv -f ${D}${libdir}/libcgroup.so.* ${D}${base_libdir}/
		ln -sf ${D}${base_libdir}/libcgroup.so.1 ${D}${libdir}/libcgroup.so
	fi
	# pam modules in ${base_libdir}/security/ should be binary .so files, not symlinks.
	if [ -f ${D}${base_libdir}/security/pam_cgroup.so.0.0.0 ]; then
		mv -f ${D}${base_libdir}/security/pam_cgroup.so.0.0.0 ${D}${base_libdir}/security/pam_cgroup.so
		rm -f ${D}${base_libdir}/security/pam_cgroup.so.*
	fi
}
