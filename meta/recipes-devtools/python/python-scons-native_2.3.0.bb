require python-scons_${PV}.bb
inherit native pythonnative
DEPENDS = "python-native"
RDEPENDS_${PN} = ""

do_install_append() {
    create_wrapper ${D}${bindir}/scons  PYTHONPATH='$PYTHONPATH:${STAGING_DIR_HOST}/${PYTHON_SITEPACKAGES_DIR}'
}
