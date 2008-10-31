HOMEPAGE = "http://www.kernel.org/pub/linux/libs/pam/"
PR = "r4"

SRC_URI = "http://www.kernel.org/pub/linux/libs/pam/library/Linux-PAM-1.0.2.tar.bz2"

EXTRA_OECONF = "--with-db-uniquename=_pam \
                 --includedir=${includedir}/security"
DEPENDS = "bison flex"
CFLAGS_append = " -fPIC "

S = "${WORKDIR}/Linux-PAM-${PV}"

inherit autotools pkgconfig


do_stage() {
        autotools_stage_all
}
