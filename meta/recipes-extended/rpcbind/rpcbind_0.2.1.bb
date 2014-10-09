SUMMARY = "Universal Addresses to RPC Program Number Mapper"
DESCRIPTION = "The rpcbind utility is a server that converts RPC \
               program numbers into universal addresses."
SECTION = "console/network"
HOMEPAGE = "http://sourceforge.net/projects/rpcbind/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=201237&atid=976751"
DEPENDS = "libtirpc quota"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=b46486e4c4a416602693a711bb5bfa39 \
                    file://src/rpcinfo.c;beginline=1;endline=27;md5=f8a8cd2cb25ac5aa16767364fb0e3c24"

SRC_URI = "${SOURCEFORGE_MIRROR}/rpcbind/rpcbind-${PV}.tar.bz2 \
           file://0001-rpcbind-rpcuser-not-being-set-in-Makefile.am.patch \
           file://init.d \
           ${UCLIBCPATCHES} \
           file://rpcbind.conf \
           file://rpcbind.socket \
           file://rpcbind.service \
          "

UCLIBCPATCHES_libc-uclibc = "file://0001-uclibc-nss.patch \
                             file://0002-uclibc-rpcsvc-defines.patch \
                            "
UCLIBCPATCHES ?= ""

SRC_URI[md5sum] = "0a5f9c2142af814c55d957aaab3bcc68"
SRC_URI[sha256sum] = "da169ff877a5a07581fad50a9a808ac6e96f0c277a3df49a7ef005778428496e"

inherit autotools update-rc.d systemd pkgconfig

PACKAGECONFIG ??= "tcp-wrappers"
PACKAGECONFIG[tcp-wrappers] = "--enable-libwrap,--disable-libwrap,tcp-wrappers"

INITSCRIPT_NAME = "rpcbind"
INITSCRIPT_PARAMS = "start 12 2 3 4 5 . stop 60 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "rpcbind.service rpcbind.socket"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --no-create-home --home-dir / \
                       --shell /bin/false --user-group rpc"

EXTRA_OECONF += " --enable-warmstarts --with-rpcuser=rpc"

do_install_append () {
	mv ${D}${bindir} ${D}${sbindir}

	install -d ${D}${sysconfdir}/init.d
	sed -e 's,/etc/,${sysconfdir}/,g' \
		-e 's,/sbin/,${sbindir}/,g' \
		${WORKDIR}/init.d > ${D}${sysconfdir}/init.d/rpcbind
	chmod 0755 ${D}${sysconfdir}/init.d/rpcbind

	install -m 0755 ${WORKDIR}/rpcbind.conf ${D}${sysconfdir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/rpcbind.socket ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/rpcbind.service ${D}${systemd_unitdir}/system
	sed -i -e 's,@SBINDIR@,${sbindir},g' \
		-e 's,@SYSCONFDIR@,${sysconfdir},g' \
		${D}${systemd_unitdir}/system/rpcbind.service
}
