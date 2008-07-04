DESCRIPTION = "NetworkManager"
SECTION = "net/misc"
LICENSE = "GPL"
HOMEPAGE = "http://www.gnome.org"
PRIORITY = "optional"
DEPENDS = "libnl dbus dbus-glib hal gconf-dbus wireless-tools ppp gnome-common"
RDEPENDS = "hal wpa-supplicant iproute2 dhcp-client"

PV = "0.7+svnr${SRCREV}"
PR = "r6"

SRC_URI="svn://svn.gnome.org/svn/NetworkManager/;module=trunk;proto=http \
	file://no-restarts.diff;patch=1;pnum=0 \
        file://makefile-fix.patch;patch=1 \
	file://NetworkManager \
	file://99_networkmanager"

EXTRA_OECONF = " \
		--with-distro=debian \
		--with-ip=/sbin/ip"
# TODO: will /bin/ip from busybox do?

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "NetworkManager"
INITSCRIPT_PARAMS = "defaults 22"

do_install_append () {
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_networkmanager ${D}/etc/default/volatiles
        # This overwrites the provided init script
	install -m 0755 ${WORKDIR}/NetworkManager ${D}/etc/init.d/
}

do_stage () {
	autotools_stage_all
}

pkg_postinst_${PN} () {
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
