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

DEPENDS = "tiff ${PN}-native"
DEPENDS_virtclass-native = ""

SRC_URI = "http://downloads.ghostscript.com/public/ghostscript-${PV}.tar.bz2 \
           file://ghostscript-9.02-prevent_recompiling.patch \
           "

SRC_URI[md5sum] = "f67151444bd56a7904579fc75a083dd6"
SRC_URI[sha256sum] = "03ea2cad13a36f8f9160912012b79619a826e7148fada6d3531feb25409ee05a"

EXTRA_OECONF = "--with-system-libtiff --without-jbig2dec --without-jasper --x-includes=${STAGING_DIR_HOST}/usr/include/X11 --x-libraries=${STAGING_DIR_HOST}/usr/lib"

inherit autotools

do_configure () {
    oe_runconf

    # copy tools from the native ghostscript build
    mkdir -p obj soobj
    for i in genarch genconf mkromfs echogs gendev genht; do
        cp ${STAGING_BINDIR_NATIVE}/ghostscript-${PV}/$i obj/$i
        cp ${STAGING_BINDIR_NATIVE}/ghostscript-${PV}/$i soobj/$i
    done
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
        oe_runmake obj/$i
    done
}

do_install_virtclass-native () {
    install -d ${D}${bindir}/ghostscript-${PV}
    for i in genarch genconf mkromfs echogs gendev genht; do
        install -m 755 obj/$i ${D}${bindir}/ghostscript-${PV}/$i
    done
}

BBCLASSEXTEND = "native"
