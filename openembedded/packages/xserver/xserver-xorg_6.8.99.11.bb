SECTION = "x11/base"
RPROVIDES = "virtual/xserver"
PROVIDES = "virtual/xserver"
LICENSE = "Xorg"
PR = "r0"

DEPENDS = "fontconfig freetype libxi xmu flex-native zlib"

SRC_URI = "cvs://anoncvs@cvs.freedesktop.org/cvs/xorg;module=xc;method=pserver;tag=XORG-6_8_99_11 \
	file://imake-staging.patch;patch=1 \
	file://imake-installed.patch;patch=1 \
	file://fontfile.patch;patch=1 file://freetype.patch;patch=1 \
	file://dri.patch;patch=1"

PACKAGES =+ "xserver-xorg-xprint xserver-xorg-xvfb xserver-xorg-utils"

S = "${WORKDIR}/xc"

FILES_xserver-xorg-xprint = "${bindir}/Xprt /etc/init.d/xprint /etc/rc.d/rc*.d/*xprint /etc/X11/Xsession.d/92xprint-xpserverlist.sh /etc/X11/xinit/xinitrc.d/92xprint-xpserverlist.sh /etc/X11/xserver/*/print ${sysconfdir}/profile.d/xprint.*"
FILES_xserver-xorg-xvfb = "${bindir}/Xvfb"
FILES_xserver-xorg-utils = "${bindir}/scanpci ${bindir}/pcitweak ${bindir}/ioport ${bindir}/in[bwl] ${bindir}/out[bwl] ${bindir}/mmap[rw] ${bindir}/gtf ${bindir}/getconfig ${bindir}/getconfig.pl"
FILES_${PN} += "${libdir}/modules/*.o "${libdir}/modules/*/*.o ${libdir}/X11/Options ${libdir}/X11/getconfig ${libdir}/X11/etc ${libdir}/modules"
FILES_${PN}-doc += "${libdir}/X11/doc"

do_configure() {
	cat <<EOF > config/cf/host.def
#define BuildServersOnly YES
#define ProjectRoot ${prefix}
#define XnestServer NO
#define XdmxServer NO
#define CcCmd gcc
#define LdCmd ld
#define HasFreetype2 YES
#define HasFontconfig YES
#define BuildDevelDRIDrivers YES
#define BuildXF86DRI YES
EOF
	echo "" > config/cf/date.def
	rm -f include/extensions/panoramiX.h
	make -C config/imake -f Makefile.ini CC="${BUILD_CC}" BOOTSTRAPCFLAGS="${BUILD_CFLAGS}" CROSSCOMPILEDIR="${CROSS_DIR}/${TARGET_SYS}/bin" PREPROCESS_CMD="gcc -E" clean imake
	make CC="${BUILD_CC}" xmakefile
	make Makefiles
	make clean
}

do_compile() {
	#make depend
	make includes
	make -C config/util CC="${BUILD_CC}"
	for l in font xtrans Xdmcp lbxutil; do make -C lib/$l CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}"; done
	make -C programs/Xserver CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}" INSTALLED_LIBS="" CPP="${CC} -E"
}

do_install() {
	make -C programs/Xserver DESTDIR="${D}" CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}" INSTALLED_LIBS="" install
	make -C lib/font DESTDIR="${D}" CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}" INSTALLED_LIBS="" install
}

do_stage() {
	install -d ${STAGING_INCDIR}/xserver-xorg
	for i in i810 via; do
	  pushd ${S}/programs/Xserver/hw/xfree86/drivers/$i; install -m 0644 *.h ${STAGING_INCDIR}/xserver-xorg/; popd
	done
	install -m 0644 programs/Xserver/hw/xfree86/common/fourcc.h ${STAGING_INCDIR}/xserver-xorg/
}
