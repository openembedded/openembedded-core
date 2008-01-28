DESCRIPTION = "Gives a fake root environment"
HOMEPAGE = "http://joostje.op.het.net/fakeroot/index.html"
SECTION = "base"
LICENSE = "GPL"
# fakeroot needs getopt which is provided by the util-linux package
RDEPENDS = "util-linux"
PR = "r2"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakeroot/fakeroot_${PV}.tar.gz \
           file://autofoo.patch;patch=1"

inherit autotools

# The fakeroot script requires the .so link to be present
FILES_${PN} =+ "${libdir}/libfakeroot-*.so ${libdir}/libfakeroot.so"

do_stage() {
        install -d ${STAGING_INCDIR}/fakeroot
        install -m 644 *.h ${STAGING_INCDIR}/fakeroot
        autotools_stage_all
}
