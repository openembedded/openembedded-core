DESCRIPTION = "Python Imaging Library"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README;beginline=92;endline=120;md5=c4371af4579f1e489cf881c1443dd4ec"
DEPENDS = "freetype jpeg tiff"
RDEPENDS_${PN} = "python-lang python-stringold"
SRCNAME = "Imaging"
PR = "ml1"

SRC_URI = "http://effbot.org/downloads/Imaging-${PV}.tar.gz \
           file://path.patch"

SRC_URI[md5sum] = "fc14a54e1ce02a0225be8854bfba478e"
SRC_URI[sha256sum] = "895bc7c2498c8e1f9b99938f1a40dc86b3f149741f105cf7c7bd2e0725405211"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

do_compile() {
    export STAGING_LIBDIR=${STAGING_LIBDIR}
    export STAGING_INCDIR=${STAGING_INCDIR}
    distutils_do_compile
}

do_install() {
    export STAGING_LIBDIR=${STAGING_LIBDIR}
    export STAGING_INCDIR=${STAGING_INCDIR}
    distutils_do_install
    install -d ${D}${datadir}/doc/${PN}/html/
    install -m 0644 ${S}/README ${D}${datadir}/doc/${PN}/
    install -m 0644 ${S}/Docs/* ${D}${datadir}/doc/${PN}/html/

}
