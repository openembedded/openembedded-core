DESCRIPTION = "Pulseaudio is a sound server for Linux and Unix-like operating systems."
HOMEPAGE = "http://www.pulseaudio.org"
AUTHOR = "Lennart Poettering"
SECTION = "libs/multimedia"
LICENSE = "GPLv2+ & LGPLv2.1"
LIC_FILES_CHKSUM = "file://GPL;md5=4325afd396febcb659c36b49533135d4 \
                    file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/modules/bluetooth/proximity-helper.c;beginline=1;endline=25;md5=e4cc710e40a4d900c2c294167bc58270 \
                    file://src/pulsecore/vector.h;beginline=1;endline=21;md5=1b561f0a2ca47b1fa49862a6ccaacff1"
DEPENDS = "libatomics-ops liboil avahi libsamplerate0 libsndfile1 libtool virtual/libx11"
# optional
DEPENDS += "bluez4 udev alsa-lib glib-2.0 dbus consolekit"

SRC_URI = "http://0pointer.de/lennart/projects/pulseaudio/pulseaudio-${PV}.tar.gz \
           file://gcc4-compile-fix.patch \
           file://volatiles.04_pulse"

inherit autotools pkgconfig

EXTRA_OECONF = "\
		--disable-lynx \
		--enable-bluez \
		--disable-polkit \
		--with-x \
		--without-jack \
		--with-glib \
		--with-alsa \
		--with-oss \
		--without-hal \
		--disable-hal \
		--with-access-group=audio \
"

PARALLEL_MAKE = ""

export TARGET_PFPU = "${TARGET_FPU}"

# TODO: Use more fine granular version
#OE_LT_RPATH_ALLOW=":${libdir}/pulse-0.9:"
OE_LT_RPATH_ALLOW = "any"
OE_LT_RPATH_ALLOW[export]="1"

do_install_append() {
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/volatiles.04_pulse  ${D}${sysconfdir}/default/volatiles/volatiles.04_pulse
	
	if [ "x${TARGET_PFPU}" == "xsoft" ] ; then 
	     sed -i -e s:\;\ resample-method\ =\ sinc-fastest:resample-method\ =\ trivial: ${D}${sysconfdir}/pulse/daemon.conf
	fi
}

PACKAGES =+ "libpulsecore libpulsecommon libpulsedsp libpulse libpulse-simple libpulse-browse libpulse-mainloop-glib \
             pulseaudio-server pulseaudio-misc pulseaudio-gconf-helper"

#upgrade path:
RREPLACES_pulseaudio-server = "libpulse-bin libpulse-conf"

PACKAGES_DYNAMIC = "pulseaudio-lib* pulseaudio-module* libpulse-lib* libpulse-module* "

FILES_libpulsecore = "${libdir}/libpulsecore*.so"
FILES_libpulsecommon = "${libdir}/libpulsecommon*.so"
FILES_libpulsedsp = "${libdir}/libpulsedsp*.so"
FILES_libpulse = "${libdir}/libpulse.so.*"
FILES_libpulse-simple = "${libdir}/libpulse-simple.so.*"
FILES_libpulse-browse = "${libdir}/libpulse-browse.so.*"
FILES_libpulse-mainloop-glib = "${libdir}/libpulse-mainloop-glib.so.*"
				   	 
FILES_${PN}-dbg += "${libexecdir}/pulse/.debug \
                    ${libdir}/pulse-${PV}/modules/.debug"
FILES_${PN}-dev += "${libdir}/pulse-${PV}/modules/*.la ${datadir}/vala"		    
FILES_${PN}-conf = "${sysconfdir}"
FILES_${PN}-bin = "${bindir}/* \
                   ${sysconfdir}/default/volatiles/volatiles.04_pulse"
FILES_${PN}-server = "${bindir}/pulseaudio ${bindir}/start-* ${sysconfdir} ${bindir}/pactl ${base_libdir}/udev/rules.d/*.rules"
FILES_${PN}-gconf-helper = "${libexecdir}/pulse/gconf-helper"
FILES_${PN}-misc = "${bindir}/*"

# Allow the pulseaudio package to be created empty as a placeholder (-dbg and -dev depend on it)
ALLOW_EMPTY_${PN} = "1"

CONFFILES_pulseaudio-server = "\ 
  ${sysconfdir}/pulse/default.pa \
  ${sysconfdir}/pulse/daemon.conf \
  ${sysconfdir}/pulse/client.conf \
  "

pkg_postinst_${PN}-server() {
        # can't do this offline
        if [ "x$D" != "x" ]; then
                exit 1
        fi
        grep -q pulse: /etc/group || addgroup pulse
        grep -q pulse: /etc/passwd || \
            adduser --disabled-password --home=/var/run/pulse --system \
                    --ingroup pulse --no-create-home -g "Pulse audio daemon" pulse
        addgroup pulse audio
        /etc/init.d/populate-volatile.sh update
}

pkg_postrm_${PN}-server() {
        deluser pulse || true
}

python populate_packages_prepend() {
        #bb.data.setVar('PKG_pulseaudio', 'pulseaudio', d)

        plugindir = bb.data.expand('${libdir}/pulse-${PV}/modules/', d)
        do_split_packages(d, plugindir, '^module-(.*)\.so$', 'pulseaudio-module-%s', 'PulseAudio module for %s', extra_depends='' )
        do_split_packages(d, plugindir, '^lib(.*)\.so$', 'pulseaudio-lib-%s', 'PulseAudio library for %s', extra_depends='' )
}

RDEPENDS_pulseaudio-module-console-kit =+ "consolekit"
RDEPENDS_pulseaudio-server = " \
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
    pulseaudio-module-console-kit \
    pulseaudio-module-position-event-sounds \
    pulseaudio-module-cork-music-on-phone "

