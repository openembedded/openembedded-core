SUMMARY = "Framework for defining and tracking users, login sessions, and seats"
DESCRIPTION = "ConsoleKit2 provides a mechanism for software to react to changes \
of any of these items or of any of the metadata associated with them."
HOMEPAGE = "https://github.com/ConsoleKit2/ConsoleKit2"
BUGTRACKER = "https://github.com/ConsoleKit2/ConsoleKit2/issues"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/main.c;endline=19;md5=c343c20c262ac4b97f0ab08665f3058d"

DEPENDS = "glib-2.0 glib-2.0-native dbus zlib"
RDEPENDS:${PN} += "base-files"

inherit autotools pkgconfig gettext gobject-introspection gtk-doc

SRC_URI = "git://github.com/ConsoleKit2/ConsoleKit2.git;protocol=https;branch=master;tag=${PV} \
           file://sepbuildfix.patch \
"
SRCREV = "cbaab28b42a9126448edd093a68e1f8ea6930982"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'pam systemd polkit selinux x11', d)} udev"

PACKAGECONFIG[pam] = "--enable-pam-module --with-pam-module-dir=${base_libdir}/security,--disable-pam-module,libpam"
PACKAGECONFIG[polkit] = "--enable-polkit,--disable-polkit,polkit"
PACKAGECONFIG[systemd] = "--with-systemdsystemunitdir=${systemd_system_unitdir}/,--without-systemdsystemunitdir"
PACKAGECONFIG[selinux] = "--enable-libselinux,--disable-libselinux,libselinux"
# udev backend for device management with a session controller needs both libudev and libdrm
PACKAGECONFIG[udev] = "--enable-libudev --enable-libdrm,--disable-libudev --disable-libdrm,udev libdrm"
PACKAGECONFIG[evdev] = "--enable-libevdev,--disable-libevdev,libevdev"
PACKAGECONFIG[udev-acl] = "--enable-udev-acl,--disable-udev-acl,udev acl"
PACKAGECONFIG[x11] = "--with-x,--without-x,virtual/libx11"

AUTOTOOLS_AUXDIR = "${S}/autotools"

do_configure:prepend() {
	mkdir -p ${AUTOTOOLS_AUXDIR}
}

# AC_CHECK_FILE cannot be used when cross compiling, all supported kernels
# provide VT change signalling via sysfs
CACHED_CONFIGUREVARS += "ac_cv_file__sys_class_tty_tty0_active=yes"

EXTRA_OECONF = "--disable-libcgmanager \
                --disable-docbook-docs \
                --disable-tests \
"

FILES:${PN} += "${exec_prefix}/lib/ConsoleKit \
                ${libdir}/ConsoleKit ${systemd_unitdir} ${base_libdir} \
                ${datadir}/dbus-1 ${datadir}/polkit-1 \
                ${sysconfdir}/X11/xinit/xinitrc.d"

PACKAGES =+ "pam-plugin-ck-connector"
FILES:pam-plugin-ck-connector += "${base_libdir}/security/*.so"
RDEPENDS:pam-plugin-ck-connector += "${PN}"

do_install:append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}${sysconfdir}/tmpfiles.d
		echo "d ${localstatedir}/log/ConsoleKit - - - -" \
			> ${D}${sysconfdir}/tmpfiles.d/consolekit.conf
	fi

	# Remove /var/ directories as the daemon creates them as required
	rm -rf ${D}${localstatedir}
}
