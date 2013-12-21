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

SRC_URI[md5sum] = "2f09e5e6cb1b9f3611bcac79bc9c2d5d"
SRC_URI[sha256sum] = "99c41320b63f6860869b5fb9af9a1854b15582796c64ee3dfd7096dc0c89f307"

inherit autotools lib_package
