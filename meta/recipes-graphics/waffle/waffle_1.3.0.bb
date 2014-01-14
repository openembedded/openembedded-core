SUMMARY = "cross-platform C library to defer selection of GL API and of window system"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4c5154407c2490750dd461c50ad94797 \
                    file://include/waffle/waffle.h;endline=24;md5=61dbf8697f61c78645e75a93c585b1bf"

SRC_URI = "http://people.freedesktop.org/~chadversary/waffle/files/release/${BPN}-${PV}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "5020ecc249096c881e1f59ee961f3d41"
SRC_URI[sha256sum] = "340ee04172dba878249469018cd7ec9d1ecd41af26b612c741b8b52e713bca8e"

inherit cmake

# This should be overridden per-machine to reflect the capabilities of the GL
# stack.
PACKAGECONFIG ??= "glx"

# I say virtual/libgl, actually wants gl.pc
PACKAGECONFIG[glx] = "-Dwaffle_has_glx=1,,virtual/libgl libx11"

# I say virtual/libgl, actually wants wayland-egl.pc, egl.pc, and the wayland
# DISTRO_FEATURE.
PACKAGECONFIG[wayland] = "-Dwaffle_has_wayland=1,,virtual/libgl wayland"

# I say virtual/libgl, actually wants gbm.pc egl.pc
PACKAGECONFIG[gbm] = "-Dwaffle_has_wayland=1,,virtual/libgl udev"

# I say virtual/libgl, actually wants egl.pc
PACKAGECONFIG[x11-egl] = "-Dwaffle_has_x11_egl=1,,virtual/libgl libxcb"

# Take the flags added by PACKAGECONFIG and pass them to cmake.
EXTRA_OECMAKE = "${EXTRA_OECONF}"

FILES_${PN}-dev += "${datadir}/cmake/Modules/FindWaffle.cmake"
