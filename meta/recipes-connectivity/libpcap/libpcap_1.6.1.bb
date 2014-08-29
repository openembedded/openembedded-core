require libpcap.inc

SRC_URI += "file://aclocal.patch"
SRC_URI[md5sum] = "5eb05edf6b6c6e63d536d1c9fbfb2f7c"
SRC_URI[sha256sum] = "116cbb3ac9e96d5dd7b39638a2f894a67fa3dcf06d794e6dae2b9a942ad13476"

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
