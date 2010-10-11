DESCRIPTION = "X11 drivers for Poulsbo (psb) 3D acceleration"

# There's a mesa implementation in this package, which is presumably
# not Intel proprietary, but it has no obvious license attached to it.
LICENSE = "Intel-binary-only"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}/COPYING;md5=02c597a2f082b4581596065bb5a521a8"
PR = "r6"

inherit autotools

PROVIDES = "virtual/libgl mesa-dri"

SRC_URI="https://launchpad.net/~gma500/+archive/ppa/+files/xpsb-glx_0.18-0ubuntu1netbook2~1004um1ubuntu1.tar.gz \
         file://cross-compile.patch \
         file://libdrmname.patch \
         file://native-matypes.patch"

do_configure () {
	chmod +x autogen.sh && ./autogen.sh ${CONFIGUREOPTS} && make realclean
	cd ${S}/src/mesa/x86/
	${BUILD_CC} -I../../../include/GL -I../../../include -I.. -I../main -I../math -I../glapi -I../tnl -Wall -Wmissing-prototypes -std=c99 -ffast-math -fPIC -D_POSIX_SOURCE -D_POSIX_C_SOURCE=199309L -D_SVID_SOURCE -D_BSD_SOURCE -D_GNU_SOURCE -DPTHREADS -DUSE_EXTERNAL_DXTN_LIB=1 -DIN_DRI_DRIVER -DGLX_DIRECT_RENDERING -DGLX_INDIRECT_RENDERING -DHAVE_ALIAS -DHAVE_POSIX_MEMALIGN -DUSE_X86_ASM -DUSE_MMX_ASM -DUSE_3DNOW_ASM -DUSE_SSE_ASM -fno-strict-aliasing gen_matypes.c -o gen_matypes
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

COMPATIBLE_MACHINE = "emenlow"
