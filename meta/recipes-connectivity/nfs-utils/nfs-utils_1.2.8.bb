SUMMARY = "userspace utilities for kernel nfs"
DESCRIPTION = "The nfs-utils package provides a daemon for the kernel \
NFS server and related tools."
HOMEPAGE = "http://nfs.sourceforge.net/"
SECTION = "console/network"

LICENSE = "MIT & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=95f3a93a5c3c7888de623b46ea085a84"

# util-linux for libblkid
DEPENDS = "libcap libnfsidmap libevent util-linux sqlite3"
RDEPENDS_${PN} = "rpcbind bash"
RRECOMMENDS_${PN} = "kernel-module-nfsd"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/nfs-utils/${PV}/nfs-utils-${PV}.tar.bz2 \
           file://nfs-utils-1.0.6-uclibc.patch \
           file://nfs-utils-1.2.3-sm-notify-res_init.patch \
           file://nfsserver \
           file://nfs-utils.conf \
           file://nfs-server.service \
           file://nfs-mountd.service \
           file://nfs-statd.service "

SRC_URI[md5sum] = "6e7d97de51e428a0b8698c16ca23db77"
SRC_URI[sha256sum] = "1cc8f02a633eddbf0a1d93421f331479c4cdab4c5ab33b8bf8c7c369f9156ac6"

PARALLEL_MAKE = ""

# Only kernel-module-nfsd is required here (but can be built-in)  - the nfsd module will
# pull in the remainder of the dependencies.

INITSCRIPT_NAME = "nfsserver"
# The server has no dependencies at the user run levels, so just put
# it in at the default levels.  It must be terminated before the network
# in the shutdown levels, but that works fine.
INITSCRIPT_PARAMS = "defaults"

inherit autotools update-rc.d systemd

SYSTEMD_SERVICE_${PN} = "nfs-server.service nfs-mountd.service nfs-statd.service"
SYSTEMD_AUTO_ENABLE = "disable"

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

PACKAGECONFIG ??= "tcp-wrappers"
PACKAGECONFIG[tcp-wrappers] = "--with-tcp-wrappers,--without-tcp-wrappers,tcp-wrappers"

INHIBIT_AUTO_STAGE = "1"

PACKAGES =+ "${PN}-client ${PN}-stats"
FILES_${PN}-client = "${base_sbindir}/*mount.nfs*"
FILES_${PN}-stats = "${sbindir}/mountstats ${sbindir}/nfsiostat"
RDEPENDS_${PN}-stats = "python"

# Make clean needed because the package comes with
# precompiled 64-bit objects that break the build
do_compile_prepend() {
	make clean
}

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${localstatedir}/lib/nfs/statd
	install -m 0755 ${WORKDIR}/nfsserver ${D}${sysconfdir}/init.d/nfsserver

	install -m 0755 ${WORKDIR}/nfs-utils.conf ${D}${sysconfdir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/nfs-server.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/nfs-mountd.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/nfs-statd.service ${D}${systemd_unitdir}/system/
	sed -i -e 's,@SBINDIR@,${sbindir},g' \
		-e 's,@SYSCONFDIR@,${sysconfdir},g' \
		${D}${systemd_unitdir}/system/*.service

	# kernel code as of 3.8 hard-codes this path as a default
	install -d ${D}/var/lib/nfs/v4recovery

	# the following are built by CC_FOR_BUILD
	rm -f ${D}${sbindir}/rpcdebug
	rm -f ${D}${sbindir}/rpcgen
	rm -f ${D}${sbindir}/locktest
}
