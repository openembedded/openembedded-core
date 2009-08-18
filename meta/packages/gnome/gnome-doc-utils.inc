LICENSE = "GPL LGPL"
DEPENDS = "libxml2 libxslt libxslt-native"

PR = "r1"

inherit gnome

EXTRA_OECONF = "--disable-scrollkeeper"

do_stage() {
	mkdir -p  ${STAGING_DATADIR}/xml/gnome/xslt/
	cp -pPr ${S}/xslt/* ${STAGING_DATADIR}/xml/gnome/xslt/
	autotools_stage_all
}

FILES_${PN} += "${datadir}/xml*"
