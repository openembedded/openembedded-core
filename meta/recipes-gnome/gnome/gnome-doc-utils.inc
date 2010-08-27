LICENSE = "GPL LGPL"
DEPENDS = "libxml2 libxslt libxslt-native"

PR = "r1"

inherit gnome

EXTRA_OECONF = "--disable-scrollkeeper"

do_install_append() {
	mkdir -p  ${D}${datadir}/xml/gnome/xslt/
	cp -pPr ${S}/xslt/* ${D}${datadir}/xml/gnome/xslt/
}

FILES_${PN} += "${datadir}/xml*"
