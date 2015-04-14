SUMMARY = "OpenGL function pointer management library"
HOMEPAGE = "https://github.com/anholt/libepoxy/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=58ef4c80d401e07bd9ee8b6b58cf464b"


SRC_URI = "git://github.com/anholt/libepoxy.git"
SRCREV="7422de5b4be7b19d789136b3bb5f932de42db27c"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

DEPENDS = "util-macros virtual/egl virtual/libx11"
