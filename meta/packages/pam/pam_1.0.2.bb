DESCRIPTION = "Linux-PAM (Pluggable Authentication Modules for Linux), Basically, it is a flexible mechanism for authenticating users"
HOMEPAGE = "http://www.kernel.org/pub/linux/libs/pam/"
BUGTRACKER = "http://sourceforge.net/projects/pam/support"
# PAM allows dual licensed under GPL and BSD
LICENSE = "GPLv2+ | BSD"
PR = "r8"

SRC_URI = "http://www.kernel.org/pub/linux/libs/pam/library/Linux-PAM-1.0.2.tar.bz2 \
           file://disable_crossbinary.patch"

EXTRA_OECONF = "--with-db-uniquename=_pam \
                 --includedir=${includedir}/security \
		 --libdir=/lib"
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
