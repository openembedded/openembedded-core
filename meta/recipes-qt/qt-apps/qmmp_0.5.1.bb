DESCRIPTION = "Qmmp (Qt-based Multimedia Player) is an audio-player, written with help of Qt library"
HOMEPAGE = "http://qmmp.ylsoftware.com"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SECTION = "multimedia"

PR = "r0"

DEPENDS = "qt4-x11-free taglib libmad libvorbis libogg alsa-lib"
RDEPENDS_${PN} += "taglib alsa-lib curl"

SRC_URI = "http://qmmp.ylsoftware.com/files/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "7934c069256bf2fc4ab762ad2683db2f"
SRC_URI[sha256sum] = "16ab0334f368a5ef14d631a1d43d7cae876ee06b7185643377b5e9cfd6143099"


PARALLEL_MAKE = ""

inherit qmake2 cmake

export EXTRA_OECMAKE = "-DQT_QMAKE_EXECUTABLE=${OE_QMAKE_QMAKE} \
                        -DQT_LRELEASE_EXECUTABLE=${OE_QMAKE_LRELEASE} \
                        -DQT_MOC_EXECUTABLE=${OE_QMAKE_MOC} \
                        -DQT_UIC_EXECUTABLE=${OE_QMAKE_UIC} \
                        -DQT_RCC_EXECUTABLE=${OE_QMAKE_RCC} \
                        -DQT_LIBRARY_DIR=${OE_QMAKE_LIBDIR_QT} \
                        -DQT_HEADERS_DIR=${OE_QMAKE_INCDIR_QT} \
                        -DQT_QTCORE_INCLUDE_DIR=${OE_QMAKE_INCDIR_QT}/QtCore \
                        "

FILES_${PN} = "${bindir}/qmmp ${libdir}/*.so* \ 
               ${libdir}/qmmp/PlaylistFormats/*.so \
               ${libdir}/qmmp/Output/libalsa.so \
               ${libdir}/qmmp/Transports/libhttp.so \
               ${libdir}/qmmp/Visual/libanalyzer.so \
               ${datadir}/icons/* \
               ${datadir}/qmmp/images/* \
               ${datadir}/applications/qmmp.desktop \
               "

PACKAGES += "${PN}-plugin-input-mad ${PN}-plugin-input-vorbis"

FILES_${PN}-plugin-input-mad = "${libdir}/qmmp/Input/libmad.so"
RDEPENDS_${PN}-plugin-input-mad = "libmad"
FILES_${PN}-plugin-input-vorbis = "${libdir}/qmmp/Input/libvorbis.so"
RDEPENDS_${PN}-plugin-input-vorbis = "libvorbis libogg"
