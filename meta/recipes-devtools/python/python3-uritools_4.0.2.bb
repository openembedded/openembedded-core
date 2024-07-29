SUMMARY = "URI parsing, classification and composition"
HOMEPAGE = "https://github.com/tkem/uritools/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ec55353c80c662e4255f8889a0ca558"

SRC_URI[sha256sum] = "04df2b787d0eb76200e8319382a03562fbfe4741fd66c15506b08d3b8211d573"

SRC_URI += "file://run-ptest"

inherit pypi python_setuptools_build_meta ptest

do_install_ptest() {
	cp -rf ${S}/tests/* ${D}${PTEST_PATH}/
}

RDEPENDS:${PN}-ptest += " \
	python3-pytest \
	python3-unittest-automake-output \
"

BBCLASSEXTEND = "native nativesdk"
