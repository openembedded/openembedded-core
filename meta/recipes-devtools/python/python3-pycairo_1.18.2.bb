SUMMARY = "Python bindings for the Cairo canvas library"
HOMEPAGE = "http://cairographics.org/pycairo"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "python-devel"
LICENSE = "LGPLv2.1 & MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f2e071ab72978431b294a0d696327421 \
                    file://COPYING-LGPL-2.1;md5=fad9b3332be894bab9bc501572864b29 \
                    file://COPYING-MPL-1.1;md5=bfe1f75d606912a4111c90743d6c7325"

# cairo >= 1.14
DEPENDS = "cairo python3"

SRC_URI = "https://github.com/pygobject/pycairo/releases/download/v${PV}/pycairo-${PV}.tar.gz"
UPSTREAM_CHECK_URI = "https://github.com/pygobject/pycairo/releases/"

SRC_URI[md5sum] = "be2ba51f234270dec340f28f1695a95e"
SRC_URI[sha256sum] = "dcb853fd020729516e8828ad364084e752327d4cff8505d20b13504b32b16531"

S = "${WORKDIR}/pycairo-${PV}"

inherit meson pkgconfig

CFLAGS += "-fPIC"

BBCLASSEXTEND = "native"

FILES_${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
