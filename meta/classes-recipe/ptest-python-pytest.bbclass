#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

inherit ptest

FILESEXTRAPATHS:prepend := "${COREBASE}/meta/files:"

SRC_URI:append = "\
    file://ptest-python-pytest/run-ptest \
"

# Overridable configuration for the directory within the source tree
# containing the pytest files
PTEST_PYTEST_DIR ?= "tests"

do_install_ptest() {
	if [ ! -f ${D}${PTEST_PATH}/run-ptest ]; then
		install -m 0755 ${UNPACKDIR}/ptest-python-pytest/run-ptest ${D}${PTEST_PATH}
	fi
    if [ -d "${S}/${PTEST_PYTEST_DIR}" ]; then
        install -d ${D}${PTEST_PATH}/${PTEST_PYTEST_DIR}
        cp -rf ${S}/${PTEST_PYTEST_DIR}/* ${D}${PTEST_PATH}/${PTEST_PYTEST_DIR}/
    fi
}

FILES:${PN}-ptest:prepend = "${PTEST_PATH}/*"

RDEPENDS:${PN}-ptest:prepend = "python3-pytest python3-unittest-automake-output"
