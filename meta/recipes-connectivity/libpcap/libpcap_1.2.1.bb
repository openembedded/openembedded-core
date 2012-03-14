require libpcap.inc

PR = "${INC_PR}.0"

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch"

SRC_URI[md5sum] = "06046e0e81efc60566daf1cc96c77d46"
SRC_URI[sha256sum] = "a135a6ef7e539729a57c7ed345bdb9b64159e13404174006a7972eb33f00debd"

#
# make install doesn't cover the shared lib
# make install-shared is just broken (no symlinks)
#
do_install_prepend () {
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    oe_runmake install-shared DESTDIR=${D}
    oe_libinstall -a -so libpcap ${D}${libdir}
}
