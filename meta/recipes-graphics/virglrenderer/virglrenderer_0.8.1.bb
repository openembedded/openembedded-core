SUMMARY = "VirGL virtual OpenGL renderer"
DESCRIPTION = "Virgil is a research project to investigate the possibility of \
creating a virtual 3D GPU for use inside qemu virtual machines, that allows \
the guest operating system to use the capabilities of the host GPU to \
accelerate 3D rendering."
HOMEPAGE = "https://virgil3d.github.io/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=c81c08eeefd9418fca8f88309a76db10"

DEPENDS = "libdrm mesa libepoxy"
SRCREV = "66c57963aaf09a1c41056bd2a001da1d51957a14"
SRC_URI = "git://anongit.freedesktop.org/virglrenderer \
           file://0001-gallium-Expand-libc-check-to-be-platform-OS-check.patch \
           file://0001-meson.build-use-python3-directly-for-python.patch \
           "

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

BBCLASSEXTEND = "native nativesdk"

REQUIRED_DISTRO_FEATURES = "opengl"
REQUIRED_DISTRO_FEATURES_class-native = ""
REQUIRED_DISTRO_FEATURES_class-nativesdk = ""
