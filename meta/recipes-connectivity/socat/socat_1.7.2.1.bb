DESCRIPTION = "Socat is a relay for bidirectional data \
transfer between two independent data channels."
HOMEPAGE = "http://www.dest-unreach.org/socat/"

SECTION = "console/network"

DEPENDS = "openssl readline"

LICENSE = "GPL-2.0+-with-OpenSSL-exception"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://README;beginline=252;endline=282;md5=79246f11a1db0b6ccec54d1fb711c01e"


PR = "r0"
SRC_URI = "http://www.dest-unreach.org/socat/download/socat-${PV}.tar.bz2;name=src \
           file://compile.patch \
           file://fix-xxx_SHIFT-autoheader.patch"

EXTRA_OECONF += "ac_cv_have_z_modifier=yes sc_cv_sys_crdly_shift=9 \
        sc_cv_sys_tabdly_shift=11 sc_cv_sys_csize_shift=4 \
        ac_cv_ispeed_offset=13 \
"

SRC_URI[src.md5sum] = "7ddfea7e9e85f868670f94d3ea08358b"
SRC_URI[src.sha256sum] = "faea2ed6c63bb97a59237fd43b7c35ad248317297e8bfeb2e6f2ec1e6bc58277"

inherit autotools

do_configure_prepend() {
    sed '/AC_DEFINE_UNQUOTED(ISPEED_OFFSET/a\AC_DEFINE(OSPEED_OFFSET,\
(ISPEED_OFFSET+1)\ ,\ [have\ ospeed\ offset])' -i ${S}/configure.in
}

do_install_prepend () {
    mkdir -p ${D}${bindir}
    install -d ${D}${bindir} ${D}${mandir}/man1
}
