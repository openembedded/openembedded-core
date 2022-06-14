SUMMARY = "Check that create_cmdline_shebang works"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI += "file://test.awk"

EXCLUDE_FROM_WORLD = "1"
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/test.awk ${D}${bindir}/test
    sed -i -e 's|@AWK_BIN@|${bindir}/awk|g' ${D}${bindir}/test
    create_cmdline_shebang_wrapper ${D}${bindir}/test
    if [ $(${D}${bindir}/test) != "Don't Panic!" ]; then
        bbfatal "Wrapper is broken"
    else
        bbnote "Wrapper is good"
    fi
}

BBCLASSEXTEND = "native"
