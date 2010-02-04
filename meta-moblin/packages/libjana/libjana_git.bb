DESCRIPTION = "O-Hand Jana Library"
DEPENDS = "gtk+ eds-dbus gconf"
PV = "0.4.4+git${SRCPV}"
PR = "r0"

inherit autotools_stage pkgconfig lib_package

SRC_URI = "git://git.gnome.org/jana;protocol=git"
S = "${WORKDIR}/git/"

EXTRA_OECONF = "--enable-examples "

do_configure_prepend() {
	touch gtk-doc.make
}

PACKAGES =+ "libjana-ecal libjana-ecal-dbg \
             libjana-gtk libjana-gtk-dbg \ 
            "

LEAD_SONAME = "libjana.so"

FILES_libjana-ecal = "${libdir}/libjana-ecal.so.*"
FILES_libjana-ecal-dbg += "${libdir}/.debug/libjana-ecal*"
FILES_libjana-gtk = "${libdir}/libjana-gtk.so.* ${datadir}/jana/landwater.vmf"
FILES_libjana-gtk-dbg += "${libdir}/.debug/libjana-gtk.so.*"
FILES_libjana-bin_append = " ${datadir}/jana/flag-uk.png "
