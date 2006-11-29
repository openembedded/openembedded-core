DESCRIPTION = "Linux Bluetooth Stack Userland Utilities."
SECTION = "console"
PRIORITY = "optional"
DEPENDS = "bluez-libs-${PV} pcmcia-cs"
RPROVIDES_${PN} = "bluez-pan bluez-sdp bluez-utils"
LICENSE = "GPL"

SRC_URI = "http://bluez.sourceforge.net/download/bluez-utils-${PV}.tar.gz \
	file://base.patch;patch=1 \
	file://blueboxes.patch;patch=1 \
	file://no-user-include.patch;patch=1 \
	file://hcid.conf \
	file://bluetooth.default \
	file://bluetooth.conf \
	file://02dtl1_cs.sh"

# Almost all serial CF cards w/ manfid 0x0000,0x0000 seem to use the bcs protocol
# Let's default to that instead of 'any' until further notice...
SRC_URI += " file://default-manfid-0x0-to-bcps.patch;patch=1"

S = "${WORKDIR}/bluez-utils-${PV}"

# We don't depend on openobex, fuse, alsa or usb but we set paths into staging
# otherwise /usr/lib and /usr/include make it onto command lines with unpredictable results
# Ideally acinclude.m4 needs fixing
EXTRA_OECONF = "--with-bluez-libs=${STAGING_LIBDIR} --with-bluez-includes=${STAGING_INCDIR} \
                --enable-pcmcia --enable-initscripts --enable-bcm203x --enable-hid2hci \
		--disable-obex --disable-alsa \
		--with-openobex=${STAGING_DIR}/${HOST_SYS} --with-fuse=${STAGING_DIR}/${HOST_SYS} \
		--with-alsa=${STAGING_DIR}/${HOST_SYS} --with-usb=${STAGING_DIR}/${HOST_SYS} \
		--with-bluez=${STAGING_LIBDIR}/${HOST_SYS}"

inherit autotools update-rc.d

INITSCRIPT_NAME = "bluetooth"
INITSCRIPT_PARAMS = "defaults 23 19"

do_compile_prepend() {
	find ${S} -name Makefile | xargs sed -i 's:/usr/include:${STAGING_INCDIR}:'
}

do_install_append() {
	install -d ${D}${base_sbindir} ${D}${base_bindir}/ ${D}${sysconfdir}/apm/event.d/
	mv ${D}${sbindir}/* ${D}${base_sbindir}/
	mv ${D}${bindir}/* ${D}${base_bindir}/
	rmdir ${D}${bindir} ${D}${sbindir}
	chmod u+s ${D}${base_sbindir}/hciattach ${D}${base_sbindir}/hciconfig
	chmod 755 ${D}${sysconfdir}/pcmcia/bluetooth
	install -m 0644 ${WORKDIR}/hcid.conf ${D}${sysconfdir}/bluetooth/
	install -m 0644 ${WORKDIR}/bluetooth.default ${D}${sysconfdir}/default/bluetooth
	cat ${WORKDIR}/bluetooth.conf >> ${D}${sysconfdir}/pcmcia/bluetooth.conf
	install -m 0755 ${WORKDIR}/02dtl1_cs.sh ${D}${sysconfdir}/apm/event.d/
}

CONFFILES_${PN} = "${sysconfdir}/bluetooth/hcid.conf ${sysconfdir}/bluetooth/rfcomm.conf \
	${sysconfdir}/bluetooth/pin ${sysconfdir}/default/bluetooth"

PACKAGES =+ "${PN}-ciptool ${PN}-bluepin"
FILES_${PN}-ciptool = "/bin/ciptool"
FILES_${PN}-bluepin = "/bin/bluepin"
