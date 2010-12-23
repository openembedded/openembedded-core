DESCRIPTION = "Hardware Abstraction Layer"
HOMEPAGE = "http://freedesktop.org/Software/hal"
BUGTRACKER = "http://bugs.freedesktop.org/buglist.cgi?product=hal"
SECTION = "unknown"

LICENSE = "GPLv2+ | AFL"
LIC_FILES_CHKSUM = "file://COPYING;md5=5b5ffd59fbb7c2fff6de76c94177af31"

DEPENDS = "virtual/kernel dbus-glib udev util-linux intltool intltool-native expat libusb gperf-native"
RDEPENDS_${PN} += "udev hal-info"

SRC_URI = "http://hal.freedesktop.org/releases/hal-${PV}.tar.gz \
           file://20hal"

inherit autotools pkgconfig


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
PACKAGE_ARCH_emenlow := "${PACKAGE_ARCH}"

do_install_append() {
        install -d ${D}/etc/dbus-1/event.d
        install -m 0755 ${WORKDIR}/20hal ${D}/etc/dbus-1/event.d
}

# At the time the postinst runs, dbus might not be setup so only restart if running
pkg_postinst_hal () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	grep haldaemon /etc/group || addgroup haldaemon
	grep haldaemon /etc/passwd || adduser --disabled-password --system --home /var/run/hald --no-create-home haldaemon --ingroup haldaemon -g HAL

	# add volatile after new user/grp are created
	echo "d root root 0700 /var/run/hald none" > /etc/default/volatiles/99_hal
	echo "d haldaemon haldaemon 0755 /var/cache/hald none" >> /etc/default/volatiles/99_hal
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
