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

SRC_URI[md5sum] = "7dfbb444b5a4e125bc5dba0aef403082"
SRC_URI[sha256sum] = "bea2a7e9bb97a7a5054d4a65d16aaeedeaa091719359ad3fcd9bfdb0fe8eb7fa"

inherit autotools lib_package
