RPROVIDES_${PN} = "hotplug"

PR = "r11"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/hotplug/udev-${PV}.tar.gz \
           file://enable-gudev.patch \
	   file://run.rules \
	   "

require udev.inc

INITSCRIPT_PARAMS = "start 03 S ."

FILES_${PN} += "${base_libdir}/udev/*"
FILES_${PN}-dbg += "${base_libdir}/udev/.debug"
UDEV_EXTRAS = "extras/firmware/ extras/scsi_id/ extras/volume_id/"

EXTRA_OECONF = "--with-udev-prefix= --disable-extras --disable-introspection"

DEPENDS += "glib-2.0"

do_install () {
	install -d ${D}${usrsbindir} \
		   ${D}${sbindir}
	oe_runmake 'DESTDIR=${D}' INSTALL=install install
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
	install -m 0755 ${WORKDIR}/udev-cache ${D}${sysconfdir}/init.d/udev-cache

	install -d ${D}${sysconfdir}/udev/rules.d/

	cp ${S}/rules/rules.d/* ${D}${sysconfdir}/udev/rules.d/
	cp ${S}/rules/packages/* ${D}${sysconfdir}/udev/rules.d/
	install -m 0644 ${WORKDIR}/local.rules         ${D}${sysconfdir}/udev/rules.d/local.rules
	#install -m 0644 ${WORKDIR}/permissions.rules   ${D}${sysconfdir}/udev/rules.d/permissions.rules
	#install -m 0644 ${WORKDIR}/run.rules          ${D}${sysconfdir}/udev/rules.d/run.rules
	#install -m 0644 ${WORKDIR}/udev.rules          ${D}${sysconfdir}/udev/rules.d/udev.rules
	#install -m 0644 ${WORKDIR}/links.conf          ${D}${sysconfdir}/udev/links.conf
	#if [ "${UDEV_DEVFS_RULES}" = "1" ]; then
	#	install -m 0644 ${WORKDIR}/devfs-udev.rules ${D}${sysconfdir}/udev/rules.d/devfs-udev.rules
	#fi

	# Remove some default rules that don't work well on embedded devices
	#rm ${D}${sysconfdir}/udev/rules.d/60-persistent-input.rules
	#rm ${D}${sysconfdir}/udev/rules.d/60-persistent-storage.rules
	#rm ${D}${sysconfdir}/udev/rules.d/60-persistent-storage-tape.rules

	install -d ${D}${sysconfdir}/udev/scripts/

	install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts/mount.sh
	install -m 0755 ${WORKDIR}/network.sh ${D}${sysconfdir}/udev/scripts

	install -d ${D}${base_libdir}/udev/
}

