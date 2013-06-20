DESCRIPTION = "A very basic Wayland image with a terminal"

IMAGE_FEATURES += "splash package-management"

LICENSE = "MIT"

inherit core-image

CORE_IMAGE_BASE_INSTALL += "weston weston-init weston-examples gtk+3-demo clutter-1.0-examples"
