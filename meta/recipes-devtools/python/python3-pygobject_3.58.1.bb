SUMMARY = "Python GObject bindings"
HOMEPAGE = "https://gitlab.gnome.org/GNOME/pygobject"
DESCRIPTION = "PyGObject is a Python package which provides bindings for GObject based libraries such as GTK, GStreamer, WebKitGTK, GLib, GIO and many more."
SECTION = "devel/python"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=91db794c3adc9d824d4eddabb341e816"

GIR_MESON_OPTION = ""

inherit gnomebase setuptools3-base gobject-introspection upstream-version-is-even ptest-python-pytest

python() {
    if d.getVar('CLASSOVERRIDE') == "class-target" and not bb.utils.to_boolean(d.getVar("GI_DATA_ENABLED")):
        raise bb.parse.SkipRecipe("GI not available")
}

DEPENDS += "python3 glib-2.0"

GNOME_COMPRESS_TYPE = "gz"
GNOMEBN = "pygobject"

SRC_URI += "file://run-ptest"
SRC_URI[archive.sha256sum] = "4c80598ade17fbaa7798e01a25d0bf29ce109740786026a074c5c62bb3e79d23"

S = "${UNPACKDIR}/${GNOMEBN}-${PV}"

PACKAGECONFIG ??= "${@bb.utils.contains_any('DISTRO_FEATURES', [ 'directfb', 'wayland', 'x11' ], 'cairo', '', d)} \
                   ${@bb.utils.contains('PTEST_ENABLED', '1', 'tests', '', d)}"

RDEPENDS:${PN} += " \
    python3-asyncio \
    python3-io \
    python3-pkgutil \
    gobject-introspection \
"

# python3-pycairo is checked on configuration -> DEPENDS
# we don't link against python3-pycairo -> RDEPENDS
PACKAGECONFIG[cairo] = "-Dpycairo=enabled,-Dpycairo=disabled, cairo python3-pycairo, python3-pycairo"
# do_install_ptest_base cleans PTEST_PATH, so the test libraries go next to it
PACKAGECONFIG[tests] = "-Dtests=true -Dgobject-introspection-tests:install_dir=${libdir}/${BPN}/gi-tests,-Dtests=false,"

do_install_ptest:append() {
    rm -f ${D}${PTEST_PATH}/tests/*.c ${D}${PTEST_PATH}/tests/*.h ${D}${PTEST_PATH}/tests/meson.build
    install -m 0644 ${S}/pyproject.toml ${D}${PTEST_PATH}/
    install -m 0644 ${B}/tests/gschemas.compiled ${D}${PTEST_PATH}/tests/
    install -m 0755 ${B}/tests/testhelper*.so ${D}${PTEST_PATH}/tests/
}

FILES:${PN}-ptest += "${libdir}/${BPN}/gi-tests"

RDEPENDS:${PN}-ptest += " \
    bash \
    python3-ctypes \
    python3-misc \
    python3-pickle \
    python3-unittest \
"
RDEPENDS:${PN}-ptest:append:libc-glibc = " glibc-gconv-iso8859-1"

BBCLASSEXTEND = "native"
PACKAGECONFIG:class-native = ""
