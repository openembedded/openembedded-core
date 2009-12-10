
FIXEDREV = "${SRCREVMOZILLAHEADLESS}"
#file://0001-Adds-initial-Gtk-clipboard-support-to-moz-headless.patch;patch=1 \
SRC_URI = "hg://hg.mozilla.org/incubator;protocol=http;module=offscreen \
           file://configurefix-1a622cb7c384.patch;patch=1;rev=1a622cb7c384 \
           file://configurefix.patch;patch=1;notrev=1a622cb7c384 \
           file://mozilla-jemalloc.patch;patch=1 \
           file://removebadrpath-1a622cb7c384.patch;patch=1;rev=1a622cb7c384 \
           file://removebadrpath.patch;patch=1;notrev=1a622cb7c384 \
           file://buildfixhack.patch;patch=1;notrev=1a622cb7c384 \
           file://jsautocfg.h \
	   file://mozconfig"
PV = "0.2+hg-1.0+${SRCPV}"
PR = "r7"

S = "${WORKDIR}/offscreen"

DEPENDS = "gconf gnome-vfs pango dbus-glib alsa-lib libidl-native sqlite3 libidl"

FILES_${PN} += "${libdir}/xulrunner-1.9.2a1pre ${libdir}/xulrunner-devel-1.9.2a1pre/sdk/lib/*.so"
FILES_${PN}-dev += "${libdir}/xulrunner-devel-1.9.2a1pre"
FILES_${PN}-dbg += "${libdir}/xulrunner-devel-1.9.2a1pre/sdk/lib/.debug"

TARGET_CC_ARCH = ""

CFLAGS = "${TARGET_CFLAGS}"
TARGET_CFLAGS = "-Os -g -pipe -Wp,-D_FORTIFY_SOURCE=2 -fexceptions -fstack-protector --param=ssp-buffer-size=4 -m32 -march=core2 -msse3 -mtune=generic -mfpmath=sse -fasynchronous-unwind-tables"

LDFLAGS = "${TARGET_LDFLAGS}"
TARGET_LDFLAGS = "-Wl,-rpath,${libdir}/xulrunner-1.9.2a1pre"

export target_alias=${TARGET_PREFIX}

inherit autotools_stage mozilla

acpaths = "-I ${S}/build/autoconf "

export topsrcdir = "${S}"

do_configure () {
	if [ "${SRCREV}" != "1a622cb7c384" ]; then
		rm -rf ${S}/build/autoconf/acwinpaths.m4
		rm -rf ${S}/js/src/build/autoconf/acwinpaths.m4
		rm -rf ${S}/nsprpub/build/autoconf/acwinpaths.m4
		mkdir -p ${S}/js/src/dist/include
		ln -fs ../../../../nsprpub/dist/include/nspr ${S}/js/src/dist/include/nsprpub
	fi
	cp ${S}/build/autoconf/install-sh ${S}
	autotools_do_configure
	# Yes, we run this twice. The first pass sets up npsrpub-config which then 
	# sets the values correctly on the second try. Ick.
	autotools_do_configure
}

do_compile () {
	cp ${WORKDIR}/jsautocfg.h ${S}/js/src/
	#oe_runmake -f client.mk build_all
	base_do_compile
}

XULVERSION = "1.9.3a1pre"

do_install_append () {
	install -d ${D}${sysconfdir}/ld.so.conf.d/
	echo ${libdir}/xulrunner-${XULVERSION}/ > ${D}${sysconfdir}/ld.so.conf.d/mozilla-headless
}

EXTRA_OECONF =+ "--enable-application=xulrunner --enable-default-toolkit=cairo-headless --with-pthreads \
                 --enable-pango --enable-optimize --disable-debug --disable-tests \
                 --disable-printing --disable-crashreporter --disable-accessibility \
                 --disable-javaxpcom --enable-plugins --enable-system-sqlite --disable-necko-wifi"

export LIBXUL_DIST="${S}/dist"

SYSROOT_PREPROCESS_FUNCS += "mozilla_sysroot_preprocess"

mozilla_sysroot_preprocess () {
	autotools_stage_dir ${D}/${libdir}/xulrunner-${XULVERSION} ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-${XULVERSION}/
	autotools_stage_dir ${D}/${libdir}/xulrunner-devel-${XULVERSION} ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/

	ln -fs ${STAGING_DIR_HOST}${libdir}/xulrunner-${XULVERSION}/ ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/bin
	ln -fs ${STAGING_DIR_HOST}${datadir}/xulrunner-${XULVERSION}/unstable/ ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/idl
	ln -fs ${STAGING_DIR_HOST}${includedir}/xulrunner-${XULVERSION}/unstable/ ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/include
	ln -fs ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/sdk/lib/ ${SYSROOT_DESTDIR}${STAGING_DIR_HOST}${libdir}/xulrunner-devel-${XULVERSION}/lib

	install -d ${SYSROOT_DESTDIR}${STAGING_BINDIR_CROSS}
	install -m 755 ${S}/dist/host/bin/host_xpidl ${SYSROOT_DESTDIR}${STAGING_BINDIR_CROSS}/xpidl
}

__anonymous () {
	if bb.data.getVar("SRCREV", d, True) == "1a622cb7c384":
		bb.data.setVar("XULVERSION", "1.9.2a1pre", d)
}
