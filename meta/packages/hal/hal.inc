DESCRIPTION = "Hardware Abstraction Layer"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL LGPL AFL"

SRC_URI = "http://hal.freedesktop.org/releases/hal-${PV}.tar.gz"

S = "${WORKDIR}/hal-${PV}"

inherit autotools pkgconfig

DEPENDS = "virtual/kernel dbus-glib udev intltool intltool-native expat libusb gperf-native"
RDEPENDS += "udev hal-info"
RRECOMMENDS = "udev-utils"

SRC_URI += "file://99_hal \
            file://20hal \
	   "

LEAD_SONAME = "libhal.so"

# machines with pci and acpi get a machine dependant hal
EXTRA_OECONF = "--with-hwdata=${datadir}/hwdata \
                --with-expat=${STAGING_LIBDIR}/.. \
                --with-dbus-sys=${sysconfdir}/dbus-1/system.d \
                --with-hotplug=${sysconfdir}/hotplug.d \
                --disable-docbook-docs \
                --disable-policy-kit \
                --disable-pmu \
                --disable-pnp-ids \
                ${@base_contains('COMBINED_FEATURES', 'pci', '--enable-pci --enable-pci-ids', '--disable-pci --disable-pci-ids',d)} \
                ${@base_contains('MACHINE_FEATURES', 'acpi', '--enable-acpi', '--disable-acpi',d)} \
               "

MY_ARCH := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${@base_contains('MACHINE_FEATURES', 'acpi', '${MACHINE_ARCH}', '${MY_ARCH}',d)}"
PACKAGE_ARCH = "${@base_contains('MACHINE_FEATURES', 'pci', '${MACHINE_ARCH}', '${MY_ARCH}',d)}"

do_install_append() {
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_hal ${D}/etc/default/volatiles
        install -d ${D}/etc/dbus-1/event.d
        install -m 0755 ${WORKDIR}/20hal ${D}/etc/dbus-1/event.d
}

do_stage() {
        oe_libinstall -C libhal -a -so libhal ${STAGING_LIBDIR}
        oe_libinstall -C libhal-storage -a -so libhal-storage ${STAGING_LIBDIR}

        install -d ${STAGING_INCDIR}/hal
        install -m 0644 libhal/libhal.h ${STAGING_INCDIR}/hal
        install -m 0644 libhal-storage/libhal-storage.h ${STAGING_INCDIR}/hal
}

# At the time the postinst runs, dbus might not be setup so only restart if running
pkg_postinst_hal () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	grep haldaemon /etc/group || addgroup haldaemon
	grep haldaemon /etc/passwd || adduser --disabled-password --system --home /var/run/hald --no-create-home haldaemon --ingroup haldaemon -g HAL

	/etc/init.d/populate-volatile.sh update

	DBUSPID=`pidof dbus-daemon`

	if [ "x$DBUSPID" != "x" ]; then
		/etc/init.d/dbus-1 force-reload
	fi
}

pkg_postrm_hal () {
	deluser haldaemon || true
	delgroup haldaemon || true
}

FILES_${PN} = "${sysconfdir} \
                ${bindir}/lshal \
                ${bindir}/hal-find-by-capability \
                ${bindir}/hal-find-by-property \
                ${bindir}/hal-device  \
                ${bindir}/hal-get-property \
                ${bindir}/hal-set-property  \
                ${bindir}/hal-lock  \
                ${bindir}/hal-is-caller-locked-out  \
                ${sbindir} \
                ${libdir}/libhal.so.* \
                ${libdir}/libhal-storage.so.* \
                ${libdir}/hal \
                ${libexecdir} \
                ${datadir}/hal/fdi \
                ${datadir}/hal/scripts"
