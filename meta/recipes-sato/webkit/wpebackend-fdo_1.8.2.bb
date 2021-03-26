SUMMARY = "WPE's backend based on a freedesktop.org stack."
HOMEPAGE = "https://github.com/Igalia/WPEBackend-fdo"
BUGTRACKER = "https://github.com/Igalia/WPEBackend-fdo/issues"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=1f62cef2e3645e3e74eb05fd389d7a66"
DEPENDS = "glib-2.0 libxkbcommon wayland virtual/egl libwpe libepoxy"

DEPENDS_append_class-target = " wayland-native"

inherit meson features_check

REQUIRED_DISTRO_FEATURES = "opengl"

SRC_URI = "https://wpewebkit.org/releases/${BPN}-${PV}.tar.xz"
SRC_URI[sha256sum] = "98c291071128326a72918d670a46ef63268c43267928560ac3b84b6aebb2d78d"

# This is a tweak of upstream-version-is-even needed because
# ipstream directory contains tarballs for other components as well.
UPSTREAM_CHECK_REGEX = "wpebackend-fdo-(?P<pver>\d+\.(\d*[02468])+(\.\d+)+)\.tar"
