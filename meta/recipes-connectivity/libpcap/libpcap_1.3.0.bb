require libpcap.inc

PR = "${INC_PR}.0"

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch \
            file://0001-Fix-disable-canusb.patch \
            file://0001-The-leading-comma-looked-weird-remove-it.patch \
            file://0001-canusb-needs-lpthread.patch \
           "

SRC_URI[md5sum] = "f78455a92622b7a3c05c58b6ad1cec7e"
SRC_URI[sha256sum] = "41cbd9ed68383afd9f1fda279cb78427d36879d9e34ee707e31a16a1afd872b9"

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
