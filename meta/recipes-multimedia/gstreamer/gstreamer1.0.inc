SUMMARY = "GStreamer 1.0 multimedia framework"
DESCRIPTION = "GStreamer is a multimedia framework for encoding and decoding video and sound. \
It supports a wide range of formats including mp3, ogg, avi, mpeg and quicktime."
HOMEPAGE = "http://gstreamer.freedesktop.org/"
BUGTRACKER = "https://bugzilla.gnome.org/enter_bug.cgi?product=Gstreamer"
SECTION = "multimedia"
LICENSE = "LGPLv2+"
DEPENDS = "glib-2.0 libxml2 bison-native flex-native libcap"

inherit autotools pkgconfig gettext

PACKAGECONFIG[debug] = "--enable-debug,--disable-debug"
PACKAGECONFIG[check] = "--enable-check,--disable-check"
PACKAGECONFIG[tests] = "--enable-tests,--disable-tests"
PACKAGECONFIG[valgrind] = "--enable-valgrind,--disable-valgrind,valgrind,"

EXTRA_OECONF = "--disable-docbook --disable-gtk-doc \
                --disable-dependency-tracking \
                --disable-examples \
                "

RRECOMMENDS_${PN}_qemux86    += "kernel-module-snd-ens1370 kernel-module-snd-rawmidi"
RRECOMMENDS_${PN}_qemux86-64 += "kernel-module-snd-ens1370 kernel-module-snd-rawmidi"

CACHED_CONFIGUREVARS += "ac_cv_header_valgrind_valgrind_h=no"

FILES_${PN} += " ${libdir}/gstreamer-1.0/*.so ${datadir}/bash-completion/completions/ ${datadir}/bash-completion/helpers/gst*"
FILES_${PN}-dev += " ${libdir}/gstreamer-1.0/*.la ${libdir}/gstreamer-1.0/*.a ${libdir}/gstreamer-1.0/include"
FILES_${PN}-dbg += " ${libdir}/gstreamer-1.0/.debug/ ${libexecdir}/gstreamer-1.0/.debug/ ${datadir}/bash-completion/helpers/.debug/"
