DESCRIPTION = "Gives a fake root environment"
HOMEPAGE = "http://joostje.op.het.net/fakeroot/index.html"
SECTION = "base"
LICENSE = "GPL"
# fakeroot needs getopt which is provided by the util-linux package
RDEPENDS = "util-linux"
PR = "r1"

SRC_URI = "http://openzaurus.org/mirror/fakeroot_${PV}.tar.gz"

inherit autotools

do_stage() {
        install -d ${STAGING_INCDIR}/fakeroot
        install -m 644 *.h ${STAGING_INCDIR}/fakeroot
        autotools_stage_all
}
