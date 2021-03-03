SUMMARY = "Basic Weston compositor setup"
DESCRIPTION = "Packages required to set up a basic working Weston session"
PR = "r1"

inherit packagegroup features_check
REQUIRED_DISTRO_FEATURES = "wayland"

RDEPENDS_${PN} = "\
    weston \
    weston-init \
    weston-examples \
    wayland-utils \
    "
