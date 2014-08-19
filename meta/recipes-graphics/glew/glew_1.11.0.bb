SUMMARY = "OpenGL extension loading library"
DESCRIPTION = "The OpenGL Extension Wrangler Library (GLEW) is a cross-platform open-source C/C++ extension loading library."
HOMEPAGE = "http://glew.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=67586"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2ac251558de685c6b9478d89be3149c2"

DEPENDS = "virtual/libx11 virtual/libgl libglu libxext libxi libxmu"


SRC_URI = "${SOURCEFORGE_MIRROR}/project/glew/glew/${PV}/glew-${PV}.tgz \
           file://autotools.patch \
           file://glew_fix_for_automake-1.12.patch \
           file://fix-glew.pc-install.patch \
          "

SRC_URI[md5sum] = "f6d72c7426a5f66580ad09e50816450a"
SRC_URI[sha256sum] = "69bbce306ac281c4fa806a7a7d02c0596281a2d8f9d70690e98126f23ba513d6"

inherit autotools lib_package pkgconfig
