SUMMARY = "A pure-Python git object database"
HOMEPAGE = "http://github.com/gitpython-developers/gitdb"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=59e5ecb13339a936eedf83282eaf4528"

inherit pypi

SRC_URI[md5sum] = "44e4366b8bdfd306b075c3a52c96ae1a"
SRC_URI[sha256sum] = "a3ebbc27be035a2e874ed904df516e35f4a29a778a764385de09de9e0f139658"

DEPENDS = "${PYTHON_PN}-async ${PYTHON_PN}-smmap"

RDEPENDS_${PN} += "${PYTHON_PN}-async \
                   ${PYTHON_PN}-compression \
                   ${PYTHON_PN}-crypt \
                   ${PYTHON_PN}-io \
                   ${PYTHON_PN}-lang \
                   ${PYTHON_PN}-mmap \
                   ${PYTHON_PN}-shell \
                   ${PYTHON_PN}-smmap \
"
BBCLASSEXTEND = "nativesdk"
