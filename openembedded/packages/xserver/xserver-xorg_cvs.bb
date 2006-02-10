SECTION = "x11/base"
LICENSE = "Xorg"
RPROVIDES = "virtual/xserver"
PROVIDES = "virtual/xserver"
PR = "r3"
PV = "6.8.1+cvs${SRCDATE}"

DEPENDS = "fontconfig freetype libxi xmu flex-2.5.4-native"

SRC_URI = "cvs://anoncvs@cvs.freedesktop.org/cvs/xorg;module=xc;method=pserver \
	file://imake-staging.patch;patch=1 \
	file://dri.patch;patch=1"

PACKAGES =+ "xserver-xorg-xprint xserver-xorg-xvfb xserver-xorg-utils"

S = "${WORKDIR}/xc"

FILES_xserver-xorg-xprint = "${bindir}/Xprt /etc/init.d/xprint /etc/rc.d/rc*.d/*xprint /etc/X11/Xsession.d/92xprint-xpserverlist.sh /etc/X11/xinit/xinitrc.d/92xprint-xpserverlist.sh /etc/X11/xserver/*/print"
FILES_xserver-xorg-xvfb = "${bindir}/Xvfb"
FILES_xserver-xorg-utils = "${bindir}/scanpci ${bindir}/pcitweak ${bindir}/ioport ${bindir}/in[bwl] ${bindir}/out[bwl] ${bindir}/mmap[rw] ${bindir}/gtf ${bindir}/getconfig ${bindir}/getconfig.pl"
FILES_${PN} += "${libdir}/modules/*.o "${libdir}/modules/*/*.o ${libdir}/X11/Options ${libdir}/X11/getconfig ${libdir}/X11/etc ${libdir}/modules"
FILES_${PN}-doc += "${libdir}/X11/doc"

do_configure() {
	echo "#define BuildServersOnly YES" > config/cf/host.def
	echo "#define ProjectRoot /usr" >> config/cf/host.def
	echo "#define XnestServer NO"  >> config/cf/host.def
	echo "#define XdmxServer NO"  >> config/cf/host.def
	echo "#define CcCmd ${CC}" >> config/cf/host.def
	echo "#define LdCmd ${LD}" >> config/cf/host.def
	echo "#define HasFreetype2 YES" >> config/cf/host.def
	echo "#define HasFontconfig YES" >> config/cf/host.def
	echo "#define BuildDevelDRIDrivers YES" >>config/cf/host.def
	echo "" > config/cf/date.def
	rm -f include/extensions/panoramiX.h
}

do_compile() {
	make -C config/imake -f Makefile.ini CC="${BUILD_CC}" BOOTSTRAPCFLAGS="${BUILD_CFLAGS}" clean imake
	make CC="${BUILD_CC}" xmakefile
	make Makefiles
	make clean
	#make depend
	make includes
	make -C config/util CC="${BUILD_CC}"
	for l in font xtrans Xdmcp lbxutil; do make -C lib/$l CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}"; done
	make -C programs/Xserver CC="${CC}" LD="${LD}" CC_STAGING="-I${STAGING_INCDIR}" LD_STAGING="-L${STAGING_LIBDIR}" INSTALLED_LIBS=""
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
