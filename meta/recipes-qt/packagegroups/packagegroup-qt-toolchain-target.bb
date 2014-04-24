SUMMARY = "Target packages for Qt SDK"

QTLIBPREFIX = ""

require packagegroup-qt-toolchain-target.inc

RDEPENDS_${PN} += " \
        qt4-x11-free-dev \
        ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'libqtopengl4-dev', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'openvg', 'libqtopenvg4-dev', '', d)} \
        "
