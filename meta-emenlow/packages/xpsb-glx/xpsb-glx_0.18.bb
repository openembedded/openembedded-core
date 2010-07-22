DESCRIPTION = "X11 drivers for Poulsbo (psb) 3D acceleration"

# There's a mesa implementation in this package, which is presumably
# not Intel proprietary, but it has no obvious license attached to it.
LICENSE = "Intel-binary-only"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}/COPYING;md5=02c597a2f082b4581596065bb5a521a8"
PR = "r3"

inherit autotools

PROVIDES = "virtual/libgl mesa-dri"

SRC_URI="https://launchpad.net/~gma500/+archive/ppa/+files/xpsb-glx_0.18-0ubuntu1netbook2~1004um1ubuntu1.tar.gz \
	file://cross-compile.patch;patch=1"

do_configure () {
	chmod +x autogen.sh && ./autogen.sh && make realclean
}

do_install() {
	make DESTDIR=${D} install
	install -d -m 0755 ${D}/${libdir}/xorg/modules/dri \
		${D}/${libdir}/xorg/modules/drivers
	install -m 0644 ${WORKDIR}/${PN}-${PV}/dri/* \
		${D}/${libdir}/xorg/modules/dri
	install -m 0644 ${WORKDIR}/${PN}-${PV}/drivers/* \
		${D}/${libdir}/xorg/modules/drivers
}

S = "${WORKDIR}/${PN}-${PV}/mesa"

EXTRA_OEMAKE = "linux-dri-x86"

DEPENDS += "libdrm-poulsbo libxxf86vm dri2proto libxmu libxi glproto makedepend-native"

FILES_${PN} = "${libdir}/* ${libdir}/xorg/modules/dri/* \
	    ${libdir}/xorg/modules/drivers/*"

# Multiple virtual/gl providers being built breaks staging
EXCLUDE_FROM_WORLD = "1"
