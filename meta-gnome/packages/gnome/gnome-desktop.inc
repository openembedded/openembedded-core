DESCRIPTION = "GNOME library for reading .desktop files"
SECTION = "x11/gnome"
LICENSE = "GPL"
DEPENDS = "gnome-common libgnomeui"

EXTRA_OECONF = "--disable-scrollkeeper"

do_configure_prepend () {
	cp ${STAGING_DATADIR}/gnome-common/data/omf.make ${S}
}

do_stage () {
	autotools_stage_all
}

FILES_${PN} += "${datadir}/gnome-about"
