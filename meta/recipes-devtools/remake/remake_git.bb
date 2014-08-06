PR = "r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://tests/COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://glob/COPYING.LIB;md5=4a770b67e6be0f60da244beb2de0fce4"
require remake.inc

SRC_URI += "file://version-remake.texi.patch \
            file://remake-remove-errors-about-colophon-and-cygnus-comma.patch \
           "
SRCREV = "f05508e521987c8494c92d9c2871aec46307d51d"
S = "${WORKDIR}/git"

PV = "3.82+dbg-0.9+git${SRCPV}"

DEPENDS += "readline"
# Need to add "gettext-native" dependency to remake-native.
# By default only "gettext-minimal-native" is added
# when inherit gettext.
DEPENDS_class-native += "gettext-native"
PROVIDES += "make"

do_configure_prepend() {
    # remove the default LINGUAS since we are not going to generate languages
    rm ${S}/po/LINGUAS
    touch ${S}/po/LINGUAS
    # create config.rpath which required by configure.ac
    ( cd ${S}; autopoint || touch config.rpath )
}

BBCLASSEXTEND = "native"
