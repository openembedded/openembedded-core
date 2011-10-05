DESCRIPTION = "Userspace NFS server"
SECTION = "console/network"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

RDEPENDS_${PN} = "pseudo"
RDEPENDS_${PN}_virtclass-native = "pseudo-native"
RDEPENDS_${PN}_virtclass-nativesdk = "pseudo-nativesdk"
BASEPV = "2.2beta47"
PR = "r1"

SRC_URI = "ftp://linux.mathematik.tu-darmstadt.de/pub/linux/oldstuff/people/okir/nfs-server-${BASEPV}.tar.gz \
           file://001-2.2b47-2.2b51.patch \
           file://002-destdir.patch \
           file://003-manpages.patch \
           file://004-strsignal.patch \
           file://005-sys-time.patch \
           file://006-reiserfs.patch \
           file://007-map.patch \
           file://008-configure.patch \
           file://009-multirw.patch \
           file://010-realpath.patch \
           file://011-fno-strict-aliasing.patch \
           file://012-nostrip.patch \
           file://013-mntpathlen.patch \
           file://014-uninitialized.patch \
           file://015-setattr.patch \
           file://016-makefile.in.patch \
           file://017-wrs-dynamic-rpc.patch \
           file://018-remove-tcp-wrappers.patch \
           file://019-pid-before-fork.patch \
           file://020-undefined-chmod-fix.patch \
           file://021-nolibwrap.patch \
           file://022-add-close-on-exec-descriptors.patch \
           file://023-no-rpc-register.patch \
          "

SRC_URI[md5sum] = "79a29fe9f79b2f3241d4915767b8c511"
SRC_URI[sha256sum] = "7eeaf3cf0b9d96167a5ba03bf1046e39b4585de1339a55b285e673c06ba415cb"

S = "${WORKDIR}/nfs-server-${BASEPV}/"

inherit autotools

BBCLASSEXTEND = "native nativesdk"

CFLAGS = "-fPIE -fstack-protector-all"
LDFLAGS = "-pie"

EXTRA_OECONF = "--enable-ugid-dynamic \
                --enable-ugid-nis \
                --enable-host-access \
                --with-exports-uid=0 \
                --with-exports-gid=0 \
                --enable-mount-logging \
                --with-devtab=${DESTDIR}${base_prefix}/var/lib/nfs/devtab \
               "

do_configure_prepend () {
    # Remove pregenerated xdr functions. They use long
    # instead of u32, which produces incorrect code on
    # 64-bit architectures:
    rm -f *_xdr.c

    mv aclocal.m4 acinclude.m4
}

# This recipe is intended for -native and -nativesdk builds only,
# not target installs:
python __anonymous () {
    import re

    pn = bb.data.getVar("PN", d, 1)
    if not pn.endswith('-native') and not pn.endswith('-nativesdk'):
        raise bb.parse.SkipPackage("unfs-server is intended for native/nativesdk builds only")
}
