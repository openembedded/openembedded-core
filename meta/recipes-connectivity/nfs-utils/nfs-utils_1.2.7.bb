SUMMARY = "userspace utilities for kernel nfs"
DESCRIPTION = "The nfs-utils package provides a daemon for the kernel \
NFS server and related tools."
HOMEPAGE = "http://nfs.sourceforge.net/"
SECTION = "console/network"

LICENSE = "MIT & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=95f3a93a5c3c7888de623b46ea085a84"

# util-linux for libblkid
DEPENDS = "libcap libnfsidmap libevent util-linux tcp-wrappers sqlite3"
RDEPENDS_${PN} = "rpcbind"
RRECOMMENDS_${PN} = "kernel-module-nfsd"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/nfs-utils/${PV}/nfs-utils-${PV}.tar.bz2 \
           file://nfs-utils.1.2.8.rc3.patch \
           file://nfs-utils-1.0.6-uclibc.patch \
           file://nfs-utils-1.2.3-uclibc-libio.h.patch \
           file://nfs-utils-1.2.3-sm-notify-res_init.patch \
           file://nfsserver"

SRC_URI[md5sum] = "3b5ca797197765dc0c3a4122720c7716"
SRC_URI[sha256sum] = "7ef8e0a8b22cd7ff33f3afd28e770d45643fae303468a180640c2967833fe75e"

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
                --enable-mountconfig \
                --enable-libmount-mount \
                --disable-nfsv41 \
                --enable-uuid \
                --disable-gss \
                --disable-tirpc \
                --disable-nfsdcltrack \
                --with-statdpath=/var/lib/nfs/statd \
               "

INHIBIT_AUTO_STAGE = "1"

PACKAGES =+ "${PN}-client ${PN}-stats"
FILES_${PN}-client = "${base_sbindir}/*mount.nfs*"
FILES_${PN}-stats = "${sbindir}/mountstats ${sbindir}/nfsiostat"
RDEPENDS_${PN}-stats = "python"

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${localstatedir}/lib/nfs/statd
	install -m 0755 ${WORKDIR}/nfsserver ${D}${sysconfdir}/init.d/nfsserver
	# kernel code as of 3.8 hard-codes this path as a default
	install -d ${D}/var/lib/nfs/v4recovery

	# the following are built by CC_FOR_BUILD
	rm -f ${D}${sbindir}/rpcdebug
	rm -f ${D}${sbindir}/rpcgen
	rm -f ${D}${sbindir}/locktest
}
