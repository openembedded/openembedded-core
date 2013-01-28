DESCRIPTION = "libnl is a library for applications dealing with netlink sockets."
HOMEPAGE = "http://www.infradead.org/~tgr/libnl/"
SECTION = "libs/network"

PE = "1"
PR = "r0"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "flex-native bison-native"

SRC_URI = "http://www.infradead.org/~tgr/${BPN}/files/${BP}.tar.gz \
           file://fix-pktloc_syntax_h-race.patch \
           file://fix-pc-file.patch "

SRC_URI[md5sum] = "6fe7136558a9071e70673dcda38545b3"
SRC_URI[sha256sum] = "2dc80f043116ec8610d9d418f5f5e9283f3d9a3a48b8d51a8cb0146b3f279604"

inherit autotools pkgconfig

FILES_${PN} = "${libdir}/libnl-3.so.* \
               ${libdir}/libnl.so.* \
               ${sysconfdir}"
RREPLACES_${PN} = "libnl2"
RCONFLICTS_${PN} = "libnl2"
FILES_${PN}-dbg += "${libdir}/libnl/cli/*/.debug"
FILES_${PN}-dev += "${libdir}/libnl/cli/*/*.so \
                    ${libdir}/libnl/cli/*/*.la"
FILES_${PN}-staticdev += "${libdir}/libnl/cli/*/*.a"

PACKAGES += "${PN}-cli ${PN}-route ${PN}-nf ${PN}-genl"
FILES_${PN}-cli   = "${libdir}/libnl-cli-3.so.* \
                     ${libdir}/libnl/cli/*/*.so.* \
                     ${sbindir}/nl-*"
FILES_${PN}-route = "${libdir}/libnl-route-3.so.*"
FILES_${PN}-nf    = "${libdir}/libnl-nf-3.so.*"
FILES_${PN}-genl  = "${libdir}/libnl-genl-3.so.* \
                     ${libdir}/libnl-genl.so.* \
                     ${sbindir}/genl-ctrl-list"
RREPLACES_${PN}-genl = "libnl-genl2 libnl-genl-3-200"
RCONFLICTS_${PN}-genl = "libnl-genl2 libnl-genl-3-200"
