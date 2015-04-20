SUMMARY = "Valgrind memory debugger and instrumentation framework"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2 & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=e7071929a50d4b0fc27a3014b315b0f7 \
                    file://include/valgrind.h;beginline=1;endline=56;md5=92df8a1bde56fe2af70931ff55f6622f \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"

X11DEPENDS = "virtual/libx11"
DEPENDS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"
PR = "r8"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://fixed-perl-path.patch \
           file://Added-support-for-PPC-instructions-mfatbu-mfatbl.patch \
           file://sepbuildfix.patch \
           file://glibc-2.20.patch \
           file://force-nostabs.patch \
           file://remove-arm-variant-specific.patch \
           file://remove-ppc-tests-failing-build.patch \
           file://enable.building.on.4.x.kernel.patch \
           file://add-ptest.patch \
           file://pass-maltivec-only-if-it-supported.patch \
           file://run-ptest \
          "

SRC_URI[md5sum] = "0947de8112f946b9ce64764af7be6df2"
SRC_URI[sha256sum] = "e6af71a06bc2534541b07743e1d58dc3caf744f38205ca3e5b5a0bdf372ed6f0"

COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64).*-linux'
COMPATIBLE_HOST_armv7a = 'arm.*-linux'

inherit autotools ptest

EXTRA_OECONF = "--enable-tls --without-mpicc"
EXTRA_OECONF_armv7a = "--enable-tls -host=armv7-none-linux-gnueabi --without-mpicc"
EXTRA_OEMAKE = "-w"
PARALLEL_MAKE = ""

do_install_append () {
    install -m 644 ${B}/default.supp ${D}/${libdir}/valgrind/
}

RDEPENDS_${PN} += "perl"

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"

# valgrind needs debug information for ld.so at runtime in order to
# redirect functions like strlen.
RRECOMMENDS_${PN} += "${TCLIBC}-dbg"

RDEPENDS_${PN}-ptest += " sed perl glibc-utils"

do_compile_ptest() {
    oe_runmake check
}


do_install_ptest() {
    chmod +x ${B}/tests/vg_regtest

    # The test application binaries are not automatically installed.
    # Grab them from the build directory.
    #
    # The regression tests require scripts and data files that are not
    # copied to the build directory.  They must be copied from the
    # source directory. 
    saved_dir=$PWD
    for parent_dir in ${S} ${B} ; do
        cd $parent_dir

        # exclude shell or the package won't install
        rm -rf none/tests/shell* 2>/dev/null

        subdirs="tests cachegrind/tests callgrind/tests drd/tests helgrind/tests massif/tests memcheck/tests none/tests"

        # Get the vg test scripts, filters, and expected files
        for dir in $subdirs ; do
            find $dir | cpio -pvdu ${D}${PTEST_PATH}
        done
        cd $saved_dir
    done

    # clean out build artifacts before building the rpm
    find ${D}${PTEST_PATH} \
         \( -name "Makefile*" \
        -o -name "*.o" \
        -o -name "*.c" \
        -o -name "*.S" \
        -o -name "*.h" \) \
        -exec rm {} \;

    # needed by massif tests
    cp ${B}/massif/ms_print ${D}${PTEST_PATH}/massif/ms_print

    # handle multilib
    sed -i s:@libdir@:${libdir}:g ${D}${PTEST_PATH}/run-ptest
}

