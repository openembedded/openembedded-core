require libpcap.inc

PR = "${INC_PR}.0"

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch \
           "

SRC_URI[md5sum] = "33ba2f10f3a402cb5d34f5e2a904794a"
SRC_URI[sha256sum] = "806d4ba23b126476d39a458ad1468f73dfe63c92f9586208f7e4e18c13e52ddd"

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
