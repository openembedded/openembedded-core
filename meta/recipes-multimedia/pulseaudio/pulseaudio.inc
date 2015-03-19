SUMMARY = "Sound server for Linux and Unix-like operating systems"
HOMEPAGE = "http://www.pulseaudio.org"
AUTHOR = "Lennart Poettering"
SECTION = "libs/multimedia"
LICENSE = "GPLv2+ & LGPLv2.1"
LIC_FILES_CHKSUM = "file://GPL;md5=4325afd396febcb659c36b49533135d4 \
                    file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/pulsecore/resampler.h;beginline=4;endline=21;md5=09794012ae16912c0270f3280cc8ff84"

DEPENDS = "libatomics-ops liboil libsndfile1 libtool"
# optional
DEPENDS += "udev alsa-lib glib-2.0 dbus gconf"
DEPENDS += "json-c gdbm speex libxml-parser-perl-native libcap"

inherit autotools pkgconfig useradd gettext perlnative bluetooth systemd

# *.desktop rules wont be generated during configure and build will fail
# if using --disable-nls
USE_NLS = "yes"

EXTRA_OECONF = "\
		--disable-hal-compat \
		--disable-orc \
		--enable-tcpwrap=no \
		--with-access-group=audio \
		--disable-openssl \
		--disable-xen \
		--with-database=simple \
		--without-fftw \
		--without-zsh-completion-dir \
		--with-udev-rules-dir=`pkg-config --variable=udevdir udev`/rules.d \
                ac_cv_header_valgrind_memcheck_h=no \
"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${BLUEZ}', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'avahi', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', '3g', 'ofono', '', d)}"
PACKAGECONFIG[bluez4] = "--enable-bluez4,--disable-bluez4,bluez4 sbc"
PACKAGECONFIG[bluez5] = "--enable-bluez5,--disable-bluez5,bluez5 sbc"
PACKAGECONFIG[ofono] = "--enable-bluez5-ofono-headset,--disable-bluez5-ofono-headset,ofono"
PACKAGECONFIG[gtk] = "--enable-gtk3,--disable-gtk3,gtk+3"
PACKAGECONFIG[systemd] = "--enable-systemd-daemon --enable-systemd-login --enable-systemd-journal,--disable-systemd-daemon --disable-systemd-login --disable-systemd-journal,systemd"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,virtual/libx11 libxtst libice libsm libxcb"
PACKAGECONFIG[avahi] = "--enable-avahi,--disable-avahi,avahi"
PACKAGECONFIG[jack] = "--enable-jack,--disable-jack,jack"

EXTRA_OECONF_append_arm = "${@bb.utils.contains("TUNE_FEATURES", "neon", "", " --enable-neon-opt=no", d)}"
EXTRA_OECONF_append_armeb = "${@bb.utils.contains("TUNE_FEATURES", "neon", "", " --enable-neon-opt=no", d)}"


export TARGET_PFPU = "${TARGET_FPU}"

# TODO: Use more fine granular version
#OE_LT_RPATH_ALLOW=":${libdir}/pulse-0.9:"
OE_LT_RPATH_ALLOW = "any"
OE_LT_RPATH_ALLOW[export]="1"

do_install_append() {
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/volatiles.04_pulse  ${D}${sysconfdir}/default/volatiles/volatiles.04_pulse
}

USERADD_PACKAGES = "pulseaudio-server"
GROUPADD_PARAM_pulseaudio-server = "pulse"
USERADD_PARAM_pulseaudio-server = "--system --home /var/run/pulse \
                              --no-create-home --shell /bin/false \
                              --groups audio,pulse --gid pulse pulse"

# The console-kit module is included here explicitly so bitbake can map to the
# RDEPENDS we define for it in this recipe, and thereby ensure that when
# adding the console-kit module to an image, we also get the necessary
# consolekit package produced.
PACKAGES =+ "libpulsecore libpulsecommon libpulse libpulse-simple libpulse-mainloop-glib \
             pulseaudio-server pulseaudio-misc ${@bb.utils.contains('PACKAGECONFIG', 'x11', 'pulseaudio-module-console-kit', '', d)}"

#upgrade path:
RREPLACES_pulseaudio-server = "libpulse-bin libpulse-conf"

PACKAGES_DYNAMIC += "^pulseaudio-lib-.* ^pulseaudio-module-.*"

FILES_libpulsecore = "${libdir}/libpulsecore*.so"
FILES_libpulsecommon = "${libdir}/pulseaudio/libpulsecommon*.so"

# client.conf configures the behaviour of libpulse, so it belongs in the same
# package.
FILES_libpulse = "${libdir}/libpulse.so.* ${sysconfdir}/pulse/client.conf"

FILES_libpulse-simple = "${libdir}/libpulse-simple.so.*"
FILES_libpulse-mainloop-glib = "${libdir}/libpulse-mainloop-glib.so.*"

FILES_${PN}-dbg += "${libexecdir}/pulse/.debug \
                    ${libdir}/pulse-${PV}/modules/.debug"
FILES_${PN}-dev += "${libdir}/pulse-${PV}/modules/*.la ${datadir}/vala ${libdir}/cmake"   
FILES_${PN}-conf = "${sysconfdir}"
FILES_${PN}-bin += "${sysconfdir}/default/volatiles/volatiles.04_pulse"
FILES_${PN}-server = "${bindir}/pulseaudio ${bindir}/start-* ${sysconfdir} ${bindir}/pactl */udev/rules.d/*.rules"

#SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE_${PN}-server = "pulseaudio.service"

FILES_${PN}-misc = "${bindir}/* ${libdir}/pulseaudio/libpulsedsp.so"

# Allow the pulseaudio package to be created empty as a placeholder (-dbg and -dev depend on it)
ALLOW_EMPTY_${PN} = "1"

CONFFILES_libpulse = "${sysconfdir}/pulse/client.conf"

CONFFILES_pulseaudio-server = "\ 
  ${sysconfdir}/pulse/default.pa \
  ${sysconfdir}/pulse/daemon.conf \
  ${sysconfdir}/pulse/system.pa \
  "

pkg_postinst_${PN}-server() {
        if [ -z "$D" ] && [ -e ${sysconfdir}/init.d/populate-volatile.sh ] ; then
            ${sysconfdir}/init.d/populate-volatile.sh update
        fi
}

python populate_packages_prepend() {
    #d.setVar('PKG_pulseaudio', 'pulseaudio')

    plugindir = d.expand('${libdir}/pulse-${PV}/modules/')
    do_split_packages(d, plugindir, '^module-(.*)\.so$', 'pulseaudio-module-%s', 'PulseAudio module for %s', extra_depends='', prepend=True)
    do_split_packages(d, plugindir, '^lib(.*)\.so$', 'pulseaudio-lib-%s', 'PulseAudio library for %s', extra_depends='', prepend=True)
}

RDEPENDS_pulseaudio-server = " \
    pulseaudio-module-filter-apply \
    pulseaudio-module-filter-heuristics \
    pulseaudio-module-udev-detect \
    pulseaudio-module-null-sink \
    pulseaudio-module-device-restore \
    pulseaudio-module-stream-restore \
    pulseaudio-module-card-restore \
    pulseaudio-module-augment-properties \
    pulseaudio-module-detect \
    pulseaudio-module-alsa-sink \
    pulseaudio-module-alsa-source \
    pulseaudio-module-alsa-card \
    pulseaudio-module-native-protocol-unix \
    pulseaudio-module-default-device-restore \
    pulseaudio-module-intended-roles \
    pulseaudio-module-rescue-streams \
    pulseaudio-module-always-sink \
    pulseaudio-module-suspend-on-idle \
    pulseaudio-module-position-event-sounds \
    pulseaudio-module-role-cork \
    pulseaudio-module-switch-on-port-available"

RDEPENDS_pulseaudio-module-console-kit =+ "consolekit"
RDEPENDS_pulseaudio-misc += "pulseaudio-module-cli-protocol-unix"

FILES_pulseaudio-module-gconf += "${libexecdir}/pulse/gconf-helper"
FILES_pulseaudio-module-alsa-card += "${datadir}/pulseaudio/alsa-mixer"

RDEPENDS_pulseaudio-server += "\
        ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'pulseaudio-module-console-kit', '', d)}"
