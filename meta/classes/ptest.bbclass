# Ptest packages are built indirectly by a distro_feature,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

SUMMARY_${PN}-ptest ?= "${SUMMARY} - Package test files"
DESCRIPTION_${PN}-ptest ?= "${DESCRIPTION}  \
This package contains a test directory ${PTEST_PATH} for package test purposes."

PTEST_PATH ?= "${libdir}/${PN}/ptest"
FILES_${PN}-ptest = "${PTEST_PATH}"
SECTION_${PN}-ptest = "devel"
ALLOW_EMPTY_${PN}-ptest = "1"
PTEST_ENABLED = "${@base_contains("DISTRO_FEATURES", "ptest", "1", "0", d)}"
RDEPENDS_${PN}-ptest_virtclass-native = ""
RDEPENDS_${PN}-ptest_virtclass-nativesdk = ""

PACKAGES =+ "${@base_contains('DISTRO_FEATURES', 'ptest', '${PN}-ptest', '', d)}"

do_configure_ptest_base() {
    if [ ${PTEST_ENABLED} = 1 ]; then
        if [ a$(type -t do_configure_ptest) = afunction ]; then
            do_configure_ptest
        fi
    fi
}

do_compile_ptest_base() {
    if [ ${PTEST_ENABLED} = 1 ]; then
        if [ a$(type -t do_compile_ptest) = afunction ]; then
            do_compile_ptest
        fi
    fi
}

do_install_ptest_base() {
    if [ ${PTEST_ENABLED} = 1 ]; then
        if [ -f ${WORKDIR}/run-ptest ]; then
            install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
            if grep -q install-ptest: Makefile; then
                oe_runmake DESTDIR=${D}${PTEST_PATH} install-ptest
            fi
            if [ a$(type -t do_install_ptest) = afunction ]; then
                do_install_ptest
            fi
        fi
    fi
}

addtask configure_ptest_base after do_configure before do_compile
addtask compile_ptest_base   after do_compile   before do_install
addtask install_ptest_base   after do_install   before do_package
