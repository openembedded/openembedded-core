HOMEPAGE = "http://www.kernel.org/pub/linux/libs/pam/"
PR = "r7"

SRC_URI = "http://www.kernel.org/pub/linux/libs/pam/library/Linux-PAM-1.0.2.tar.bz2 \
           file://disable_crossbinary.patch;patch=1 "

EXTRA_OECONF = "--with-db-uniquename=_pam \
                 --includedir=${includedir}/security \
		 --libdir=/lib"
DEPENDS = "bison flex"
CFLAGS_append = " -fPIC "

S = "${WORKDIR}/Linux-PAM-${PV}"

inherit autotools_stage pkgconfig

FILES_${PN}-dbg += "${base_libdir}/security/.debug"
FILES_${PN}-dbg += "${base_libdir}/security/pam_filter/.debug"
FILES_${PN} += "${base_libdir}/security/*.so"
FILES_${PN} += "${base_libdir}/security/pam_filter/upperLOWER"
FILES_${PN} += "${base_libdir}/security/*.so"
FILES_${PN}-dev += "${base_libdir}/security/*.la"
FILES_${PN}-dev += "${base_libdir}/*.la"
FILES_${PN} += "${base_libdir}/*.so*"
