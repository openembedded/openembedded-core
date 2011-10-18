SUMMARY = "The GPL Ghostscript PostScript/PDF interpreter"
DESCRIPTION = "Ghostscript is used for PostScript/PDF preview and printing.  Usually as \
a back-end to a program such as ghostview, it can display PostScript and PDF \
documents in an X11 environment. \
\
Furthermore, it can render PostScript and PDF files as graphics to be printed \
on non-PostScript printers. Supported printers include common \
dot-matrix, inkjet and laser models. \
\
Package gsfonts contains a set of standard fonts for Ghostscript. \
"
HOMEPAGE = "http://www.ghostscript.com"
SECTION = "console/utils"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d151214b3131251dfc9d858593acbd24"

PR = "r0"

DEPENDS = "ghostscript-native tiff jpeg fontconfig cups"
DEPENDS_virtclass-native = ""

SRC_URI_BASE = "http://downloads.ghostscript.com/public/ghostscript-${PV}.tar.gz"

SRC_URI = "${SRC_URI_BASE} \
           file://ghostscript-9.02-prevent_recompiling.patch \
           file://ghostscript-9.02-genarch.patch \
           file://objarch.h \
           file://ghostscript-9.02-parallel-make.patch \
           "

SRC_URI_virtclass-native = "${SRC_URI_BASE}"

SRC_URI[md5sum] = "9c2fb4af1eb609d09dba5bb0fa76173a"
SRC_URI[sha256sum] = "f1e333738c41c3bf2b47ceb9806abb8045bcdc7353002c32736150425a7c1ef4"

EXTRA_OECONF = "--without-x --with-system-libtiff --without-jbig2dec --without-jasper --with-fontpath=${datadir}/fonts"

# This has been fixed upstream but for now we need to subvert the check for time.h
# http://bugs.ghostscript.com/show_bug.cgi?id=692443
# http://bugs.ghostscript.com/show_bug.cgi?id=692426
CFLAGS += "-DHAVE_SYS_TIME_H=1"

inherit autotools

do_configure () {
    mkdir -p obj
    mkdir -p soobj
    cp ${WORKDIR}/objarch.h obj/arch.h

    oe_runconf

    # copy tools from the native ghostscript build
    mkdir -p obj/aux soobj
    for i in genarch genconf mkromfs echogs gendev genht; do
        cp ${STAGING_BINDIR_NATIVE}/ghostscript-${PV}/$i obj/aux/$i
    done
}

do_install_append () {
    mkdir -p ${D}${datadir}/ghostscript/${PV}/
    cp -r Resource ${D}${datadir}/ghostscript/${PV}/
    cp -r iccprofiles ${D}${datadir}/ghostscript/${PV}/

    if [ -f ${D}${sysconfdir}/cups ]; then
        chown -R root:lp ${D}${sysconfdir}/cups
    fi
}

python do_patch_virtclass-native () {
    pass
}

do_configure_virtclass-native () {
    oe_runconf
}

do_compile_virtclass-native () {
    mkdir -p obj
    for i in genarch genconf mkromfs echogs gendev genht; do
        oe_runmake obj/aux/$i
    done
}

do_install_virtclass-native () {
    install -d ${D}${bindir}/ghostscript-${PV}
    for i in genarch genconf mkromfs echogs gendev genht; do
        install -m 755 obj/aux/$i ${D}${bindir}/ghostscript-${PV}/$i
    done
}

BBCLASSEXTEND = "native"

# Ghostscript install tool 'instcopy' tries to remove already created
# directories during install and parallel make causes problems.
PARALLEL_MAKEINST=""
