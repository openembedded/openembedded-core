require libpcap.inc

PR = "${INC_PR}.0"

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch \
           "

SRC_URI[md5sum] = "56e88a5aabdd1e04414985ac24f7e76c"
SRC_URI[sha256sum] = "7c6a2a4f71e8ab09804e6b4fb3aff998c5583108ac42c0e2967eee8e1dbc7406"

#
# make install doesn't cover the shared lib
# make install-shared is just broken (no symlinks)
#

do_configure_prepend () {
    #remove hardcoded references to /usr/include
    sed 's|\([ "^'\''I]\+\)/usr/include/|\1${STAGING_INCDIR}/|g' -i ${S}/configure.in
}

do_install_prepend () {
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    oe_runmake install-shared DESTDIR=${D}
    oe_libinstall -a -so libpcap ${D}${libdir}
}
