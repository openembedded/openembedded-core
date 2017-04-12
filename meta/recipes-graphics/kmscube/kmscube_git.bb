DESCRIPTION = "Demo application to showcase 3D graphics using kms and gbm"
HOMEPAGE = "https://cgit.freedesktop.org/mesa/kmscube/"
LICENSE = "MIT"
SECTION = "graphics"
DEPENDS = "virtual/libgles2 virtual/egl libdrm gstreamer1.0 gstreamer1.0-plugins-base"

LIC_FILES_CHKSUM = "file://kmscube.c;beginline=1;endline=23;md5=8b309d4ee67b7315ff7381270dd631fb"

SRCREV = "e56980c28766ffb0bf8edee96b529a5b47f97137"
SRC_URI = "git://anongit.freedesktop.org/mesa/kmscube;branch=master;protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
