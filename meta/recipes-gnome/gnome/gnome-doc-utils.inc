LICENSE = "GPL & LGPL"
DEPENDS = "libxml2 libxslt libxslt-native gnome-doc-utils-native glib-2.0"
DEPENDS_virtclass-native = "libxml2-native libxslt-native intltool-native glib-2.0-native"

inherit gnome gettext python-dir

EXTRA_OECONF += "--disable-scrollkeeper"

do_install_append() {
	mkdir -p  ${D}${datadir}/xml/gnome/xslt/
	cp -pPr ${S}/xslt/* ${D}${datadir}/xml/gnome/xslt/

	chown -R root:root ${D}
}

FILES_${PN} += "${datadir}/xml* ${PYTHON_SITEPACKAGES_DIR}/*"

RDEPENDS_${PN} = "bash"
RDEPENDS_${PN}_virtclass-native = ""

BBCLASSEXTEND = "native"
