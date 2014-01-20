require libpcap.inc

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch \
           "
SRC_URI[md5sum] = "7e7321fb3aff2f2bb05c8229f3795d4a"
SRC_URI[sha256sum] = "9ae92159c1060f15e6a90f2c4ad227268b6aaa382c316fa49a31c496b9979e93"

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
