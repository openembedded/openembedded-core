require libpcap.inc

PR = "r0"

SRC_URI += "file://config-fixes.patch;patch=1 \
            file://aclocal.patch;patch=1 \
            file://ieee80215-arphrd.patch;patch=1 \
            file://ldflags.patch;patch=1"

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
