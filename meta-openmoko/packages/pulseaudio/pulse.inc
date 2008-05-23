DESCRIPTION = "Pulseaudio is a sound server for Linux and Unix-like operating systems."
HOMEPAGE = "http://www.pulseaudio.org"
AUTHOR = "Lennart Poettering"
SECTION = "libs/multimedia"
LICENSE = "LGPL"
DEPENDS = "libatomics-ops liboil avahi libsamplerate0 libsndfile1 libtool hal"
# optional
DEPENDS += "alsa-lib glib-2.0"

SRC_URI = "http://0pointer.de/lennart/projects/pulseaudio/pulseaudio-${PV}.tar.gz \
           file://gcc4-compile-fix.patch;patch=1 \
           file://volatiles.04_pulse"

inherit autotools pkgconfig

EXTRA_OECONF = "\
		--disable-lynx \
		--without-x \
		--without-jack \
		--with-glib \
		--with-alsa \
		--with-oss \
		--with-hal \
"

PARALLEL_MAKE = ""

export TARGET_PFPU = "${TARGET_FPU}"

# TODO: Use more fine granular version
#OE_LT_RPATH_ALLOW=":${libdir}/pulse-0.9:"
OE_LT_RPATH_ALLOW = "any"
OE_LT_RPATH_ALLOW[export]="1"

do_stage() {
	autotools_stage_all
}

do_install_append() {
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/volatiles.04_pulse  ${D}${sysconfdir}/default/volatiles/volatiles.04_pulse
	
	if [ "x${TARGET_PFPU}" == "xsoft" ] ; then 
	     sed -i -e s:\;\ resample-method\ =\ sinc-fastest:resample-method\ =\ trivial: ${D}${sysconfdir}/pulse/daemon.conf
	fi
}

PACKAGES =+ "libpulsecore libpulse libpulse-simple libpulse-browse libpulse-mainloop-glib \
             pulseaudio-server pulseaudio-misc pulseaudio-gconf-helper"

#upgrade path:
RREPLACES_pulseaudio-server = "libpulse-bin libpulse-conf"

PACKAGES_DYNAMIC = "pulseaudio-lib* pulseaudio-module* libpulse-lib* libpulse-module* "

FILES_libpulsecore = "${libdir}/libpulsecore.so.*"
FILES_libpulse = "${libdir}/libpulse.so.*"
FILES_libpulse-simple = "${libdir}/libpulse-simple.so.*"
FILES_libpulse-browse = "${libdir}/libpulse-browse.so.*"
FILES_libpulse-mainloop-glib = "${libdir}/libpulse-mainloop-glib.so.*"
				   	 
FILES_${PN}-dbg += "${libexecdir}/pulse/.debug \
                    ${libdir}/pulse-0.9/modules/.debug"
FILES_${PN}-dev += "${libdir}/pulse-0.9/modules/*.la"		    
FILES_${PN}-conf = "${sysconfdir}"
FILES_${PN}-bin = "${bindir}/* \
                   ${sysconfdir}/default/volatiles/volatiles.04_pulse"
FILES_${PN}-server = "${bindir}/pulseaudio ${sysconfdir}"
FILES_${PN}-gconf-helper = "${libexecdir}/pulse/gconf-helper"
FILES_${PN}-misc = "${bindir}/*"

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

        plugindir = bb.data.expand('${libdir}/pulse-0.9/modules/', d)
        do_split_packages(d, plugindir, '^module-(.*)\.so$', 'pulseaudio-module-%s', 'PulseAudio module for %s', extra_depends='' )
        do_split_packages(d, plugindir, '^lib(.*)\.so$', 'pulseaudio-lib-%s', 'PulseAudio library for %s', extra_depends='' )
}
