# Ptest packages are built indirectly by a distro_feature,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

SUMMARY_${PN}-ptest ?= "${SUMMARY} - Package test files"
DESCRIPTION_${PN}-ptest ?= "${DESCRIPTION}  \
This package contains a test directory ${PTEST_PATH} for package test purposes."

PTEST_PATH ?= "${libdir}/${PN}/ptest"
FILES_${PN}-ptest = "${PTEST_PATH}/*"
SECTION_${PN}-ptest = "devel"
ALLOW_EMPTY_${PN}-ptest = "1"
PTEST_ENABLED = "${@base_contains("DISTRO_FEATURES", "ptest", "1", "0", d)}"
RDEPENDS_${PN}-ptest_virtclass-native = ""
RDEPENDS_${PN}-ptest_virtclass-nativesdk = ""

PACKAGES += "${PN}-ptest"

FILES_${PN}-dbg += "${PTEST_PATH}/*/.debug \
                    ${PTEST_PATH}/*/*/.debug \
                    ${PTEST_PATH}/*/*/*/.debug \
                    ${PTEST_PATH}/*/*/*/*/.debug \
                   "

ptest_do_install() {
    if [ "${PN}" = "${BPN}" -a ${PTEST_ENABLED} = "1" ]; then
        install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
        if grep -q install-ptest: Makefile; then
            oe_runmake DESTDIR=${D}${PTEST_PATH} install-ptest
        fi
    fi
}

EXPORT_FUNCTIONS ptest_do_install
