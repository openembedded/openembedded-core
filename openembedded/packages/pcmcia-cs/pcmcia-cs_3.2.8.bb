DESCRIPTION = "Utilities and system configuration files for the Linux PCMCIA card services"
SECTION = "base"
PRIORITY = "required"
LICENSE = "GPL"
DEPENDS = "virtual/kernel"
PR = "r27"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmcia-cs/pcmcia-cs-${PV}.tar.gz \
	   file://busybox.patch;patch=1 \
	   file://network.patch;patch=1 \
	   file://pcic-extra.patch;patch=1 \
	   file://automount.patch;patch=1 \
	   file://ratoc-cfu1u.patch;patch=1 \
	   file://no-hostap-cards.patch;patch=1 \
	   file://gcc4_fixes.patch;patch=1 \
	   file://pcmcia \
	   file://ide.opts \
	   file://wireless.opts \
	   file://network.conf \
	   file://wnv.conf"

SRC_URI_append_spitz = " file://nocleanup.patch;patch=1"
S = "${WORKDIR}/pcmcia-cs-${PV}"

INITSCRIPT_NAME = "pcmcia"
INITSCRIPT_PARAMS = "defaults"

inherit update-rc.d module-base

export KERNEL_SOURCE = ${@base_read_file('${STAGING_KERNEL_DIR}/kernel-source')}

sbindir = "/sbin"

do_configure() {
	touch .prereq.ok
	touch config.out
	cat >config.mk <<EOF
UCC=${CC}
UFLAGS=${CFLAGS} -I${S}/include
HAS_WORDEXP=y
SYSV_INIT=y
RC_DIR=/etc
CONFIG_PNP_BIOS=n
ARCH=${ARCH}
CONFIG_CARDBUS=n
CONFIG_PCMCIA=y
CONFIG_INET=y
CONFIG_SCSI=y
DO_IDE=y
EOF
	cat >include/pcmcia/autoconf.h <<EOF
#define HAS_WORDEXP 1
EOF
}

do_compile() {
        oe_runmake all HAS_XPM= FLIBS="" XMANDIR=""
}

INSTALL_ETC = "ftl ide ieee1394 memory network parport scsi serial wireless"
INSTALL_ETC_DATA =     "config config.opts ftl.opts ieee1394.opts memory.opts network.opts parport.opts scsi.opts serial.opts shared"
INSTALL_ETC_DATA_arm = "config             ftl.opts ieee1394.opts memory.opts network.opts parport.opts scsi.opts serial.opts shared"

do_install() {
	install -d ${D}${sbindir}
	for f in cardmgr/cardctl cardmgr/cardmgr cardmgr/ide_info cardmgr/ifport cardmgr/ifuser cardmgr/pcinitrd flash/ftl_check flash/ftl_format
	do
		install -m 0755 $f ${D}${sbindir}/
	done
	install -d ${D}${sysconfdir}/init.d \
		   ${D}${sysconfdir}/pcmcia \
		   ${D}${sysconfdir}/pcmcia/cis

	install -m 0644 ${WORKDIR}/network.conf ${D}${sysconfdir}/pcmcia/
	install -m 0644 ${WORKDIR}/wnv.conf ${D}${sysconfdir}/pcmcia/

	for i in ${INSTALL_ETC}; do
		install -m 0755 etc/${i} ${D}${sysconfdir}/pcmcia/
	done
	for i in ${INSTALL_ETC_DATA}; do
		install -m 0644 etc/${i} ${D}${sysconfdir}/pcmcia/
	done

	# ensure that config.opts always exists, albeit empty
	echo >> ${D}${sysconfdir}/pcmcia/config.opts
	
	install -m 0644 ${WORKDIR}/ide.opts ${D}${sysconfdir}/pcmcia/
	install -m 0644 ${WORKDIR}/wireless.opts ${D}${sysconfdir}/pcmcia/
	for i in etc/cis/*; do
		install -m 0644 $i ${D}${sysconfdir}/pcmcia/cis/
	done
	install -m 0755 etc/rc.pcmcia ${D}${sysconfdir}/init.d/pcmcia
	install -d ${D}${sysconfdir}/sysconfig
	install -m 0755 ${WORKDIR}/pcmcia ${D}${sysconfdir}/sysconfig/pcmcia
}

PACKAGES =+ "${PN}-ftl ${PN}-pcinitrd"

FILES_${PN} = "${sbindir} ${sysconfdir}"
FILES_${PN}-ftl = "/sbin/ftl_format /sbin/ftl_check /etc/pcmcia/ftl*"
FILES_${PN}-pcinitrd = "/sbin/pcinitrd"

