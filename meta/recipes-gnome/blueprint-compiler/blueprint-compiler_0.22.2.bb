SUMMARY = "A markup language for GTK user interface files."
HOMEPAGE = "https://gitlab.gnome.org/GNOME/blueprint-compiler"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=3000208d539ec061b899bce1d9ce9404"

SRC_URI = "git://gitlab.gnome.org/GNOME/blueprint-compiler;protocol=https;nobranch=1;tag=v${PV}"
SRCREV = "ed35ac74de0c1bb1f5e852410b97edd1f08904c9"

inherit meson pkgconfig python3-dir gobject-introspection-data

# The compiler needs python3-pygobject on target, which is skipped without
# GI data (which also depends on qemu-usermode), so skip along with it.
python() {
    if d.getVar('CLASSOVERRIDE') == "class-target" and not bb.utils.to_boolean(d.getVar("GI_DATA_ENABLED")):
        raise bb.parse.SkipRecipe("GI not available")
}

do_install:append () {
    # Meson encodes the full path of the build python in the shebang
    sed -i -e '1s|^#!.*|#!${USRBINPATH}/env python3|' ${D}${bindir}/blueprint-compiler
}

PACKAGES += "${PN}-python"

FILES:${PN}-python = "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS:${PN}-python = "python3-pygobject"

BBCLASSEXTEND = "native"
