DESCRIPTION = "The OpenGL Extension Wrangler Library (GLEW) is a cross-platform open-source C/C++ extension loading library."
HOMEPAGE = "http://glew.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=67586"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2ac251558de685c6b9478d89be3149c2"

DEPENDS = "virtual/libx11 virtual/libgl libglu libxext libxi libxmu"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/glew/glew/${PV}/glew-${PV}.tgz \
           file://autotools.patch \
           file://glew_fix_for_automake-1.12.patch \
          "

SRC_URI[md5sum] = "69ce911decef6249d24742497e6ad06a"
SRC_URI[sha256sum] = "9b36530e414c95d6624be9d6815a5be1531d1986300ae5903f16977ab8aeb787"

inherit autotools lib_package
