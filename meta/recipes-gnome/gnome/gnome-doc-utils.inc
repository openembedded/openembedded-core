LICENSE = "GPLv2 & LGPLv2.1"
DEPENDS = "libxml2 libxslt libxslt-native gnome-doc-utils-native glib-2.0"
DEPENDS_class-native = "libxml2-native libxslt-native intltool-native glib-2.0-native"

inherit gnome gettext python-dir pythonnative

EXTRA_OECONF += "--disable-scrollkeeper"

do_install_append() {
	mkdir -p  ${D}${datadir}/xml/gnome/xslt/
	cp -pPr ${S}/xslt/* ${D}${datadir}/xml/gnome/xslt/

	chown -R root:root ${D}
}

do_install_append_class-native () {
	create_wrapper ${D}${bindir}/xml2po ${STAGING_BINDIR_NATIVE}/python-native/python
}

FILES_${PN} += "${datadir}/xml* ${PYTHON_SITEPACKAGES_DIR}/*"

RDEPENDS_${PN} = "bash"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
