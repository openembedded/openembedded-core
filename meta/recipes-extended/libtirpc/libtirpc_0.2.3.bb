SUMMARY = "Transport-Independent RPC library"
DESCRIPTION = "Libtirpc is a port of Suns Transport-Independent RPC library to Linux"
SECTION = "libs/network"
HOMEPAGE = "http://sourceforge.net/projects/libtirpc/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=183075&atid=903784"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=f835cce8852481e4b2bbbdd23b5e47f3 \
                    file://src/netname.c;beginline=1;endline=27;md5=f8a8cd2cb25ac5aa16767364fb0e3c24"
PR = "r0"

DEPENDS += "xz-native"
PROVIDES = "virtual/librpc"

SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.bz2;name=libtirpc \
           ${GENTOO_MIRROR}/${BPN}-glibc-nfs.tar.xz;name=glibc-nfs \
           file://libtirpc-0.2.1-fortify.patch \
           file://obsolete_automake_macros.patch \
          "

SRC_URI_append_libc-uclibc = " file://remove-des-uclibc.patch"

SRC_URI[libtirpc.md5sum] = "b70e6c12a369a91e69fcc3b9feb23d61"
SRC_URI[libtirpc.sha256sum] = "4f29ea0491b4ca4c29f95f3c34191b857757873bbbf4b069f9dd4da01a6a923c"
SRC_URI[glibc-nfs.md5sum] = "5ae500b9d0b6b72cb875bc04944b9445"
SRC_URI[glibc-nfs.sha256sum] = "2677cfedf626f3f5a8f6e507aed5bb8f79a7453b589d684dbbc086e755170d83"
inherit autotools pkgconfig

do_configure_prepend () {
        cp -r ${S}/../tirpc ${S}
}

do_install_append() {
        chown root:root ${D}${sysconfdir}/netconfig
}
