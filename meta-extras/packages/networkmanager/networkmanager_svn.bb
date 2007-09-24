DESCRIPTION = "NetworkManager"
SECTION = "net/misc"
LICENSE = "GPL"
HOMEPAGE = "http://www.gnome.org"
PRIORITY = "optional"
DEPENDS = "libnl dbus dbus-glib hal gconf-dbus wireless-tools ppp"
RDEPENDS = "hal wpa-supplicant iproute2"

PV = "0.7+svn${SRCDATE}"
PR = "r1"

SRC_URI="svn://svn.gnome.org/svn/NetworkManager/;module=trunk;proto=http \
	file://build-fixes.diff;patch=1;pnum=0 \
	file://install-tools.patch;patch=1;pnum=0 \
	file://25NetworkManager \
	file://99_networkmanager"

EXTRA_OECONF = " \
		--with-distro=debian \
		--with-ip=/sbin/ip"

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_networkmanager ${D}/etc/default/volatiles
	install -d ${D}/etc/dbus-1/event.d
	install -m 0755 ${WORKDIR}/25NetworkManager ${D}/etc/dbus-1/event.d
}

do_stage () {
	autotools_stage_all
}

pkg_postinst () {
        if [ "x$D" != "x" ]; then
                exit 1
        fi
        /etc/init.d/populate-volatile.sh update
}

PACKAGES =+ "libnmutil libnmglib"

FILES_libnmutil += "${libdir}/libnm-util.so.*"

FILES_libnmglib += "${libdir}/libnm_glib.so.*"

FILES_${PN}-dev = "${includedir}/* \
        ${libdir}/*.so \
        ${libdir}/*.a \
        ${libdir}/pkgconfig/*.pc \
        ${datadir}/NetworkManager/gdb-cmd \
        "
