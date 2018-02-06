SUMMARY = "libva-utils is a collection of utilities from libva project"

DESCRIPTION = "libva-utils is a collection of utilities \
and examples to exercise VA-API in accordance with the libva \
project.VA-API is an open-source library and API specification, \
which provides access to graphics hardware acceleration capabilities \
for video processing. It consists of a main library and driver-specific \
acceleration backends for each supported hardware vendor"

HOMEPAGE = "https://01.org/linuxmedia/vaapi"
BUGTRACKER = "https://github.com/intel/libva-utils/issues"

SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=b148fc8adf19dc9aec17cf9cd29a9a5e"

SRC_URI = "https://github.com/intel/${BPN}/releases/download/${PV}/${BP}.tar.bz2"
SRC_URI[md5sum] = "60fd49a60ef96ebfb559374d412e1fc4"
SRC_URI[sha256sum] = "a921df31311d8f49d2e392a5fc2a068d79f89aeb588309fbff24365310dbc5f6"

UPSTREAM_CHECK_URI = "https://github.com/intel/libva-utils/releases"

DEPENDS = "libva"

inherit autotools pkgconfig distro_features_check

# depends on libva which requires opengl
REQUIRED_DISTRO_FEATURES = "opengl"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,virtual/libx11 libxext libxfixes"
PACKAGECONFIG[wayland] = "--enable-wayland,--disable-wayland,wayland-native wayland"
