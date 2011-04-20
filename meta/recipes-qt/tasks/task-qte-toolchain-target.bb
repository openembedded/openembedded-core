DESCRIPTION = "Target packages for Qt Embedded SDK"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PR = "r5"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} += " \
        task-core-standalone-sdk-target \
        qt4-embedded-mkspecs \
        libqt-embeddedmultimedia4-dev \
        libqt-embeddedphonon4-dev \
        libqt-embedded3support4-dev \
        libqt-embeddedclucene4-dev \
        libqt-embeddedcore4-dev \
        libqt-embeddeddbus4-dev \
        libqt-embeddeddesignercomponents4-dev \
        libqt-embeddeddesigner4-dev \
        libqt-embeddeduitools4-dev \
        libqt-embeddedgui4-dev \
        libqt-embeddedhelp4-dev \
        libqt-embeddednetwork4-dev \
        libqt-embeddedscript4-dev \
        libqt-embeddedscripttools4-dev \
        libqt-embeddedsql4-dev \
        libqt-embeddedsvg4-dev \
        libqt-embeddedtest4-dev \
        libqt-embeddedwebkit4-dev \
        libqt-embeddedxml4-dev \
        libsqlite3-dev \
        expat-dev \
        "

