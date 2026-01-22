SUMMARY = "A markup language for GTK user interface files."
HOMEPAGE = "https://gitlab.gnome.org/GNOME/blueprint-compiler"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=3000208d539ec061b899bce1d9ce9404"

SRC_URI = "git://gitlab.gnome.org/GNOME/blueprint-compiler;protocol=https;branch=main;tag=${PV} \
           file://0001-Make-python-site-path-configurable.patch \
          "

SRCREV = "07c9c9df9cd1b6b4454ecba21ee58211e9144a4b"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dpython_site_dir=${PYTHON_SITEPACKAGES_DIR}"

PACKAGES += "${PN}-python"

FILES:${PN}-python = "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS:${PN}-python = "python3-pygobject"

BBCLASSEXTEND = "native"
