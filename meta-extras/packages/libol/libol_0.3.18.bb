PR = "r7"

SRC_URI = "http://www.balabit.com/downloads/files/libol/0.3/${P}.tar.gz"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools binconfig

do_stage() {
        install -d ${STAGING_INCDIR}/libol
        install -m 0755 ${S}/src/.libs/libol.so.0.0.0 ${STAGING_LIBDIR}/
        ln -fs ${STAGING_LIBDIR}/libol.so.0.0.0 ${STAGING_LIBDIR}/libol.so.0
        install ${S}/src/*.h ${STAGING_INCDIR}/libol/
}
