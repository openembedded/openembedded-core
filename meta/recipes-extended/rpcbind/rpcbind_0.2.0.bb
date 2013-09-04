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
           file://init.d \
           file://fix_host_path.patch \
           file://obsolete_automake_macros.patch \
           ${UCLIBCPATCHES} \
           file://rpcbind.conf \
           file://rpcbind.service \
          "

UCLIBCPATCHES_libc-uclibc = "file://0001-uclibc-nss.patch \
                             file://0002-uclibc-rpcsvc-defines.patch \
                            "
UCLIBCPATCHES ?= ""

SRC_URI[md5sum] = "1a77ddb1aaea8099ab19c351eeb26316"
SRC_URI[sha256sum] = "c92f263e0353887f16379d7708ef1fb4c7eedcf20448bc1e4838f59497a00de3"

PR = "r4"

inherit autotools update-rc.d systemd

PACKAGECONFIG ??= "tcp-wrappers"
PACKAGECONFIG[tcp-wrappers] = "--enable-libwrap,--disable-libwrap,tcp-wrappers"

INITSCRIPT_NAME = "rpcbind"
INITSCRIPT_PARAMS = "start 43 S . start 32 0 6 . stop 81 1 ."

SYSTEMD_SERVICE_${PN} = "rpcbind.service"
SYSTEMD_AUTO_ENABLE = "disable"

EXTRA_OECONF += " --enable-warmstarts "

do_install_append () {
	mv ${D}${bindir} ${D}${sbindir}

	install -d ${D}${sysconfdir}/init.d
	sed -e 's,/etc/,${sysconfdir}/,g' \
		-e 's,/sbin/,${sbindir}/,g' \
		${WORKDIR}/init.d > ${D}${sysconfdir}/init.d/rpcbind
	chmod 0755 ${D}${sysconfdir}/init.d/rpcbind

	install -m 0755 ${WORKDIR}/rpcbind.conf ${D}${sysconfdir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/rpcbind.service ${D}${systemd_unitdir}/system
	sed -i -e 's,@SBINDIR@,${sbindir},g' \
		-e 's,@SYSCONFDIR@,${sysconfdir},g' \
		${D}${systemd_unitdir}/system/rpcbind.service
}
