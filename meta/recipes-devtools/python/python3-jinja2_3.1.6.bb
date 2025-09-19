DESCRIPTION = "Python Jinja2: A small but fast and easy to use stand-alone template engine written in pure python."
HOMEPAGE = "https://pypi.org/project/Jinja2/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5dc88300786f1c214c1e9827a5229462"

SRC_URI[sha256sum] = "0137fb05990d35f1275a587e9aee6d56da821fc83491a0fb838183be43f66d6d"

PYPI_PACKAGE = "jinja2"

CVE_PRODUCT = "jinja2 jinja"

CLEANBROKEN = "1"

inherit pypi python_flit_core
inherit ${@bb.utils.filter('DISTRO_FEATURES', 'ptest', d)}

SRC_URI += " \
	file://run-ptest \
"

do_install_ptest() {
    install -d ${D}${PTEST_PATH}/tests
    cp -rf ${S}/tests/* ${D}${PTEST_PATH}/tests/

    # test_async items require trio module
    rm -f ${D}${PTEST_PATH}/tests/test_async.py ${D}${PTEST_PATH}/tests/test_async_filters.py
}

RDEPENDS:${PN}-ptest += " \
	${PYTHON_PN}-pytest \
        ${PYTHON_PN}-toml \
	${PYTHON_PN}-unixadmin \
"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-asyncio \
    ${PYTHON_PN}-crypt \
    ${PYTHON_PN}-io \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-markupsafe \
    ${PYTHON_PN}-math \
    ${PYTHON_PN}-netclient \
    ${PYTHON_PN}-numbers\
    ${PYTHON_PN}-pickle \
    ${PYTHON_PN}-pprint \
    ${PYTHON_PN}-shell \
    ${PYTHON_PN}-threading \
"

BBCLASSEXTEND = "native nativesdk"
