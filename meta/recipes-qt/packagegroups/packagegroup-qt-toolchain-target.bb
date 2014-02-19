SUMMARY = "Target packages for Qt SDK"

QTLIBPREFIX = ""

require packagegroup-qt-toolchain-target.inc

RDEPENDS_${PN} += " \
        qt4-x11-free-dev \
        ${@base_contains('DISTRO_FEATURES', 'opengl', 'libqtopengl4-dev', '', d)} \
        ${@base_contains('DISTRO_FEATURES', 'openvg', 'libqtopenvg4-dev', '', d)} \
        "
