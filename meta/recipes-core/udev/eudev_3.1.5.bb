SUMMARY = "eudev is a fork of systemd's udev"
HOMEPAGE = "https://wiki.gentoo.org/wiki/Eudev"
SRC_URI = "https://github.com/gentoo/${PN}/archive/v${PV}.tar.gz \
	file://init \
	file://local.rules \
	file://udev-cache \
	file://udev-cache.default \
        file://run.rules \
        file://udev.rules \
        file://devfs-udev.rules \
        file://links.conf \
        file://permissions.rules \
	"

LICENSE = "GPLv2.0+"

inherit autotools update-rc.d

SRC_URI[md5sum] = "e130f892d8744e292cb855db79935f68"
SRC_URI[sha256sum] = "ce9d5fa91e3a42c7eb95512ca0fa2a631e89833053066bb6cdf42046b2a88553"

LIC_FILES_CHKSUM="file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0-native glib-2.0 util-linux libxslt-native gperf-native kmod"

PROVIDES = "udev"
RPROVIDES_${PN} = "hotplug udev"

PACKAGES =+ "udev-cache"
PACKAGES =+ "libudev"

INITSCRIPT_PACKAGES = "eudev udev-cache"
INITSCRIPT_NAME_eudev = "udev"
INITSCRIPT_PARAMS_eudev = "start 04 S ."
INITSCRIPT_NAME_udev-cache = "udev-cache"
INITSCRIPT_PARAMS_udev-cache = "start 36 S ."

RRECOMMENDS_${PN} += "udev-cache"


FILES_libudev = "${base_libdir}/libudev.so.*"

FILES_${PN} += "${libexecdir} ${nonarch_base_libdir}/udev ${bindir}/udevadm"


FILES_${PN}-dev = "${datadir}/pkgconfig/udev.pc \
                   ${includedir}/libudev.h ${libdir}/libudev.so \
                   ${includedir}/udev.h ${libdir}/libudev.la \
                   ${libdir}/libudev.a ${libdir}/pkgconfig/libudev.pc \
		   "
FILES_udev-cache = "${sysconfdir}/init.d/udev-cache ${sysconfdir}/default/udev-cache"

EXTRA_OECONF = "--with-rootlibdir=${base_libdir} \
	     --sbindir=${base_sbindir} \
	     --libexecdir=${nonarch_base_libdir} \
	     --with-rootprefix= \
"
do_install_append(){
	install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
        install -m 0755 ${WORKDIR}/udev-cache ${D}${sysconfdir}/init.d/udev-cache
        sed -i s%@UDEVD@%${base_sbindir}/udevd% ${D}${sysconfdir}/init.d/udev
        sed -i s%@UDEVD@%${base_sbindir}/udevd% ${D}${sysconfdir}/init.d/udev-cache

        install -d ${D}${sysconfdir}/default
        install -m 0755 ${WORKDIR}/udev-cache.default ${D}${sysconfdir}/default/udev-cache

        touch ${D}${sysconfdir}/udev/cache.data

        install -d ${D}${sysconfdir}/udev/rules.d/

        install -m 0644 ${WORKDIR}/local.rules         ${D}${sysconfdir}/udev/rules.d/local.rules

	# Fix for multilib systems where libs along with confs are installed incorrectly 
        if ! [ -d ${D}${nonarch_base_libdir}/udev ]
          then
            install -d ${D}${nonarch_base_libdir}/udev
            mv ${D}${base_libdir}/udev ${D}${nonarch_base_libdir}
        fi

        # hid2hci has moved to bluez4. removed in udev as of version 169
        rm -f ${D}${base_libdir}/udev/hid2hci

        echo 'udev_run="/var/run/udev"' >> ${D}${sysconfdir}/udev/udev.conf

        # Use classic network interface naming scheme
        touch ${D}${sysconfdir}/udev/rules.d/80-net-name-slot.rules
}

python () {
    if bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' in DISTRO_FEATURES")
}
