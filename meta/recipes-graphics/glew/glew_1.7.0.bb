DESCRIPTION = "The OpenGL Extension Wrangler Library (GLEW) is a cross-platform open-source C/C++ extension loading library."
HOMEPAGE = "http://glew.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=67586"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2ac251558de685c6b9478d89be3149c2"

DEPENDS = "virtual/libx11 virtual/libgl libxext libxi libxmu"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/glew/glew/${PV}/glew-${PV}.tgz \
           file://autotools.patch \
          "

SRC_URI[md5sum] = "fb7a8bb79187ac98a90b57f0f27a3e84"
SRC_URI[sha256sum] = "1653a63fb1e1a518c4b5ccbaf1a617f1a0b4c1c29d39ae4e2583844d98365c09"

inherit autotools lib_package
