DESCRIPTION = "LIRC is a package that allows you to decode and send infra-red signals of many commonly used remote controls."
SECTION = "console/network"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
DEPENDS = "virtual/kernel x11 xau libsm ice"
PR = "r5"

SRC_URI = "${SOURCEFORGE_MIRROR}/lirc/lirc-${PV}.tar.gz \
      file://lircd.init file://lircmd.init"
S = "${WORKDIR}/lirc-${PV}"

inherit autotools module-base update-rc.d

INITSCRIPT_NAME = "lircd"
INITSCRIPT_PARAMS = "defaults 20"

include lirc-config.inc

EXTRA_OEMAKE = 'SUBDIRS="daemons tools"'

do_stage() {
        oe_libinstall -so -C tools liblirc_client ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/lirc/
	install -m 0644 tools/lirc_client.h ${STAGING_INCDIR}/lirc/
}

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install ${WORKDIR}/lircd.init ${D}${sysconfdir}/init.d/lircd

        install -d ${D}${datadir}/lirc/
        cp -pPR ${S}/remotes ${D}${datadir}/lirc/
}

PACKAGES =+ "lirc-x"

FILES_lirc-x = "${bindir}/irxevent ${bindir}/xmode2"
