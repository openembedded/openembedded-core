LICENSE = "GPL"
DEPENDS = "dbus-glib"

SRC_URI = "git://anongit.freedesktop.org/DeviceKit/DeviceKit;protocol=git \
        file://volatile"

PV = "002+git${SRCREV}"
PR = "r1"
SRCREV = "014d168ba4bf40c9bae487bacff8bf2aa054b5f6"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-man-pages"

inherit autotools pkgconfig

do_install_append() {
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/volatile ${D}/etc/default/volatiles/devicekit
}

pkg_postinst_devicekit () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	/etc/init.d/populate-volatile.sh update

	DBUSPID=`pidof dbus-daemon`

	if [ "x$DBUSPID" != "x" ]; then
		/etc/init.d/dbus-1 reload
	fi
}

FILES_${PN} += "${datadir}/dbus-1/"
