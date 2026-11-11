SUMMARY = "log4cplus provides a simple C++ logging API for log management"
SECTION = "libs"
HOMEPAGE = "https://github.com/log4cplus/log4cplus"
DESCRIPTION = "log4cplus is a simple to use C++ logging API providing thread-safe, flexible, and arbitrarily granular control over log management and configuration. It is modelled after the Java log4j API."
BUGTRACKER = "https://github.com/log4cplus/log4cplus/issues"

LICENSE = "Apache-2.0 AND BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41e8e060c26822886b592ab4765c756b"

# Replace dots with underscores to match GitHub's tag format (e.g., 2.2.0.1 -> REL_2_2_0_1)
GITHUB_TAG = "REL_${@d.getVar('PV').replace('.', '_')}"

SRC_URI = "https://github.com/log4cplus/log4cplus/releases/download/${GITHUB_TAG}/log4cplus-${PV}.tar.gz"
SRC_URI[sha256sum] = "e19d9b294f8c4c8f60467e0f456e1a347c81d479369d5840afc80c1137b7db6c"

UPSTREAM_CHECK_URI = "https://github.com/log4cplus/log4cplus/releases"
UPSTREAM_CHECK_REGEX = "log4cplus-(?P<pver>\d+(\.\d+)+)\.tar"

inherit autotools pkgconfig ptest

SRC_URI += "file://run-ptest"

RDEPENDS:${PN}-ptest += "bash diffutils gawk grep sed ${PN}"

log4cplus_ptest_tests() {
    progs=$(makefile-getvar ${B}/Makefile noinst_PROGRAMS)
    for at in $(makefile-getvar ${B}/Makefile TESTSUITE_AT); do
        name=$(basename $at .at)
        for p in $progs; do
            [ "$p" = "$name" ] && echo "$name"
        done
    done
}

do_compile_ptest() {
    rm -f ${S}/tests/testsuite
    oe_runmake -C ${B} tests/testsuite
    for t in $(log4cplus_ptest_tests); do
        oe_runmake -C ${B} $t ${t}U
    done
}

do_install_ptest() {
    install -d ${D}${PTEST_PATH}/tests

    install -m 0755 ${S}/tests/testsuite ${D}${PTEST_PATH}/tests/

    install -m 0644 ${B}/tests/atconfig ${D}${PTEST_PATH}/tests/
    sed -i "/^abs_/d" ${D}${PTEST_PATH}/tests/atconfig
    cat >> ${D}${PTEST_PATH}/tests/atconfig <<EOF
abs_builddir='${PTEST_PATH}/tests'
abs_srcdir='${PTEST_PATH}/tests'
abs_top_builddir='${PTEST_PATH}'
abs_top_srcdir='${PTEST_PATH}'
EOF

    enable_threads=$(sed -n "s|^: \${ENABLE_THREADS='\(.*\)'}|\1|p" ${B}/tests/atlocal)
    wchar_support=$(sed -n "s|^: \${BUILD_WITH_WCHAR_T_SUPPORT='\(.*\)'}|\1|p" ${B}/tests/atlocal)
    cat > ${D}${PTEST_PATH}/tests/atlocal <<EOF
: \${GREP='grep'}
: \${AWK='gawk'}
: \${SED='sed'}
: \${ENABLE_THREADS='$enable_threads'}
: \${BUILD_WITH_WCHAR_T_SUPPORT='$wchar_support'}
EOF

    for t in $(log4cplus_ptest_tests); do
        ${B}/libtool --mode=install install -m 0755 ${B}/$t ${D}${PTEST_PATH}/
        [ -e ${B}/${t}U ] && ${B}/libtool --mode=install install -m 0755 ${B}/${t}U ${D}${PTEST_PATH}/
        install -d ${D}${PTEST_PATH}/tests/$t
        for f in expout experr log4cplus.properties.1 log4cplus.properties.2; do
            [ -e ${S}/tests/$t/$f ] && install -m 0644 ${S}/tests/$t/$f ${D}${PTEST_PATH}/tests/$t/
        done
        for f in ${B}/tests/$t/*.properties; do
            [ -e $f ] && install -m 0644 $f ${D}${PTEST_PATH}/tests/$t/
        done
    done

    sed -i "s#@PTEST_PATH@#${PTEST_PATH}#g" ${D}${PTEST_PATH}/run-ptest
}

BBCLASSEXTEND = "native"
