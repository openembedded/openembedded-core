DESCRIPTION = "Libcgroup"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

PR = "r1"

inherit autotools pkgconfig

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libcg/${BPN}/v.038/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "f0f7d4060bf36ccc19d75dbf4f1695db"
SRC_URI[sha256sum] = "5d36d1a48b95f62fe9fcdf74a5a4089512e5e43e6011aa1504fd6f2a0909867f"

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
		rel_lib_prefix=`echo ${libdir} | sed 's,\(^/\|\)[^/][^/]*,..,g'`
		ln -sf ${rel_lib_prefix}${base_libdir}/libcgroup.so.1 ${D}${libdir}/libcgroup.so
	fi
	# pam modules in ${base_libdir}/security/ should be binary .so files, not symlinks.
	if [ -f ${D}${base_libdir}/security/pam_cgroup.so.0.0.0 ]; then
		mv -f ${D}${base_libdir}/security/pam_cgroup.so.0.0.0 ${D}${base_libdir}/security/pam_cgroup.so
		rm -f ${D}${base_libdir}/security/pam_cgroup.so.*
	fi
}
