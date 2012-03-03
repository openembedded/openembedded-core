DESCRIPTION = "Qmmp (Qt-based Multimedia Player) is an audio-player, written with help of Qt library"
HOMEPAGE = "http://qmmp.ylsoftware.com"
LICENSE = "GPLv2"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SECTION = "multimedia"

PR = "r0"

DEPENDS = "taglib libmad libvorbis libogg alsa-lib"

SRC_URI = "http://qmmp.ylsoftware.com/files/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "20852f3cce3471bfc5affa9b2e947dc6"
SRC_URI[sha256sum] = "6391dec020d2a381d7f4b7890fae6c49eadf88b3c9aef571fe3c5e96140822ec"


inherit qmake2 cmake qt4x11

export EXTRA_OECMAKE = "-DQT_QMAKE_EXECUTABLE=${OE_QMAKE_QMAKE} \
                        -DQT_LRELEASE_EXECUTABLE=${OE_QMAKE_LRELEASE} \
                        -DQT_MOC_EXECUTABLE=${OE_QMAKE_MOC} \
                        -DQT_UIC_EXECUTABLE=${OE_QMAKE_UIC} \
                        -DQT_RCC_EXECUTABLE=${OE_QMAKE_RCC} \
                        -DQT_LIBRARY_DIR=${OE_QMAKE_LIBDIR_QT} \
                        -DQT_HEADERS_DIR=${OE_QMAKE_INCDIR_QT} \
                        -DQT_QTCORE_INCLUDE_DIR=${OE_QMAKE_INCDIR_QT}/QtCore \
                        "
PACKAGES_DYNAMIC = "qmmp-plugin-* "


python populate_packages_prepend () {
	import os
	qmmp_libdir = d.expand('${libdir}/qmmp')
	gd = d.expand('${D}/${libdir}/qmmp')
	plug_dirs = os.listdir(gd)

	for plug_dir in plug_dirs:
		g_plug_dir = os.path.join(qmmp_libdir,plug_dir)
		do_split_packages(d, g_plug_dir, '^lib(.*)\.so$', 'qmmp-plugin-' + plug_dir.lower() + '-%s', 'Qmmp' + plug_dir  + 'plugin for %s')
} 

FILES_${PN} = "\
		${bindir}/qmmp \
                ${libdir}/lib*${SOLIBS} \ 
		${datadir}/icons/* \
                ${datadir}/qmmp/images/* \
                ${datadir}/applications/* \
		"

FILES_${PN}-dbg += "\
                ${libdir}/qmmp/*/.debug/* \
               "

RDEPENDS_${PN} += "taglib alsa-lib libmad curl"
