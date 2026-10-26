SUMMARY = "Resolve JSON Pointers in Python"
HOMEPAGE = "https://github.com/stefankoegl/python-json-pointer"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=32b15c843b7a329130f4e266a281ebb3"

inherit pypi ptest setuptools3

SRC_URI[sha256sum] = "81f28618a019cf1b011a6cf757439737972de1ccbeca2e1f86dfb4d661800792"

SRC_URI += " \
	file://run-ptest \
"

do_install_ptest() {
	install -m 0644 ${S}/tests.py ${D}${PTEST_PATH}/
	install -d ${D}${PTEST_PATH}/bin
	install -m 0755 ${S}/bin/jsonpointer ${D}${PTEST_PATH}/bin/
}

RDEPENDS:${PN} += " \
    python3-json \
"

RDEPENDS:${PN}-ptest += " \
	python3-doctest \
	python3-unittest \
	python3-unittest-automake-output \
"

BBCLASSEXTEND = "native nativesdk"
