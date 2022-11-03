SUMMARY = "Brotli compression format"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/brotli-${PV}/LICENSE;md5=941ee9cd1609382f946352712a319b4b"

# pypi package does not have a valid license file
SRC_URI = "http://codeload.github.com/google/brotli/tar.gz/v${PV};downloadfilename=brotli-${PV}.tar.gz"

SRC_URI[md5sum] = "c2274f0c7af8470ad514637c35bcee7d"
SRC_URI[sha256sum] = "f9e8d81d0405ba66d181529af42a3354f838c939095ff99930da6aa9cdf6fe46"

S = "${WORKDIR}/brotli-${PV}"

inherit setuptools3

RDEPENDS_${PN} = "\
  ${PYTHON_PN}-cffi \
"

BBCLASSEXTEND = "native nativesdk"
