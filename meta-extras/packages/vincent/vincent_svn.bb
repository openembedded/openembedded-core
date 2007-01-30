DESCRIPTION = "Vincent OpenGL-ES library "
HOMEPAGE = "http://sourceforge.net/projects/ogl-es/"
LICENSE = "BSD"

PV = "0.0+svn${SRCDATE}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=ogles;proto=http"
S = "${WORKDIR}/ogles/"

inherit autotools pkgconfig

# PACKAGES = ${PN}

do_stage() {
        install -d ${STAGING_INCDIR}/GLES

        install -m 0644 ${S}/include/ug.h ${STAGING_INCDIR}
        install -m 0644 ${S}/include/GLES/* ${STAGING_INCDIR}/GLES/

        oe_libinstall -so libvincent ${STAGING_LIBDIR}
}
