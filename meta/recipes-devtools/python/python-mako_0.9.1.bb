SUMMARY = "Templating library for Python"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da2a9d126b93cab0996a8287dacc480b"

SRC_URI = "https://pypi.python.org/packages/source/M/Mako/Mako-${PV}.tar.gz"
SRC_URI[md5sum] = "fe3f394ef714776d09ec6133923736a7"
SRC_URI[sha256sum] = "ed74d72b720a97a51590dfa839f2048ceeb76cc80d1d9ea5731a5262384316ae"

S = "${WORKDIR}/Mako-${PV}"

inherit setuptools

RDEPENDS_${PN} = "python-threading \
                  python-netclient \
                  python-html \
"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
