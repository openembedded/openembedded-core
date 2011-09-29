SUMMARY = "userspace utilities for kernel nfs"
DESCRIPTION = "The nfs-utils package provides a daemon for the kernel \
NFS server and related tools."
HOMEPAGE = "http://nfs.sourceforge.net/"
SECTION = "console/network"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

# util-linux for libblkid
DEPENDS = "libcap libnfsidmap libevent util-linux tcp-wrappers"
RDEPENDS_${PN} = "portmap python"
RRECOMMENDS_${PN} = "kernel-module-nfsd"

PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/nfs/nfs-utils-${PV}.tar.bz2 \
           file://nfs-utils-1.0.6-uclibc.patch \
           file://nfs-utils-1.2.3-uclibc-libio.h.patch \
           file://nfsserver"

SRC_URI[md5sum] = "1131dc5f27c4f3905a6e7ee0d594fd4d"
SRC_URI[sha256sum] = "5575ece941097cbfa67fbe0d220dfa11b73f5e6d991e7939c9339bd72259ff19"

PARALLEL_MAKE = ""

# Only kernel-module-nfsd is required here (but can be built-in)  - the nfsd module will
# pull in the remainder of the dependencies.

INITSCRIPT_NAME = "nfsserver"
# The server has no dependencies at the user run levels, so just put
# it in at the default levels.  It must be terminated before the network
# in the shutdown levels, but that works fine.
INITSCRIPT_PARAMS = "defaults"

inherit autotools update-rc.d

# --enable-uuid is need for cross-compiling
EXTRA_OECONF = "--with-statduser=nobody \
                --enable-nfsv41 \
                --enable-uuid \
                --disable-gss \
                --disable-tirpc \
                --with-statedir=/var/lib/nfs"

INHIBIT_AUTO_STAGE = "1"

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/nfsserver ${D}${sysconfdir}/init.d/nfsserver

	# the following are built by CC_FOR_BUILD
	rm -f ${D}${sbindir}/rpcdebug
	rm -f ${D}${sbindir}/rpcgen
	rm -f ${D}${sbindir}/locktest
}
