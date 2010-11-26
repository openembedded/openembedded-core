DESCRIPTION = "Qmmp (Qt-based Multimedia Player) is an audio-player, written with help of Qt library"
HOMEPAGE = "http://qmmp.ylsoftware.com"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SECTION = "multimedia"

PR = "r1"

DEPENDS = "qt4-x11-free taglib"
RDEPENDS = "qt4-x11-free libmad libvorbis libogg taglib alsa-lib curl"

SRC_URI = "http://qmmp.ylsoftware.com/files/qmmp-0.4.1.tar.bz2"

PARALLEL_MAKE = ""

inherit qmake2 pkgconfig

do_configure_prepend() {
	# fix qt4 lrelease name
	sed -i -e 's/lrelease-qt4/lrelease4/' ${S}/qmmp.pro

	# disable the unsupported plugin
	for plugin in sndfile wavpack ; do
		sed -i -e "s/$plugin//" ${S}/src/plugins/Input/Input.pro
	done
	sed -i -e 's/mms//' ${S}/src/plugins/Transports/Transports.pro
	sed -i -e 's/srconverter//' ${S}/src/plugins/Effect/Effect.pro

	sed -i -e 's/^CONFIG/#CONFIG/' ${S}/qmmp.pri
	sed -i -e 's/CONFIG += WITH_ENCA/#CONFIG += WITH_ENCA/' ${S}/qmmp.pri
}

do_install() {
	oe_runmake INSTALL_ROOT=${D} install
}
