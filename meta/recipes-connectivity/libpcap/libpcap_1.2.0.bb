require libpcap.inc

PR = "${INC_PR}.0"

SRC_URI += "file://aclocal.patch \
            file://ieee80215-arphrd.patch"

SRC_URI[md5sum] = "dfb8aa690b7a29821bfa183025436569"
SRC_URI[sha256sum] = "702ac51cfaa5c17d6b92771b22835d58eda4dc9e1f596c80a0b031e4c45c07d6"

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
