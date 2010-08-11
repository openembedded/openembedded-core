DESCRIPTION = "Linux-PAM (Pluggable Authentication Modules for Linux), Basically, it is a flexible mechanism for authenticating users"
HOMEPAGE = "http://www.kernel.org/pub/linux/libs/pam/"
BUGTRACKER = "http://sourceforge.net/projects/pam/support"
# PAM allows dual licensed under GPL and BSD
LICENSE = "GPLv2+ | BSD"
PR = "r2"

SRC_URI = "http://www.kernel.org/pub/linux/libs/pam/library/Linux-PAM-${PV}.tar.bz2 \
           file://disable_crossbinary.patch \
           file://99_pam"

EXTRA_OECONF = "--with-db-uniquename=_pam \
                --includedir=${includedir}/security \
                --libdir=${base_libdir} \
                --disable-regenerate-docu"
DEPENDS = "bison flex"
CFLAGS_append = " -fPIC "

S = "${WORKDIR}/Linux-PAM-${PV}"

inherit autotools pkgconfig gettext

FILES_${PN}-dbg += "${base_libdir}/security/.debug"
FILES_${PN}-dbg += "${base_libdir}/security/pam_filter/.debug"
FILES_${PN} += "${base_libdir}/security/*.so"
FILES_${PN} += "${base_libdir}/security/pam_filter/upperLOWER"
FILES_${PN} += "${base_libdir}/security/*.so"
FILES_${PN}-dev += "${base_libdir}/security/*.la"
FILES_${PN}-dev += "${base_libdir}/*.la"
FILES_${PN} += "${base_libdir}/*.so*"

do_install() {
	autotools_do_install

	# don't install /var/run when populating rootfs. Do it through volatile
	rm -rf ${D}/var
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_pam ${D}/etc/default/volatiles
}
