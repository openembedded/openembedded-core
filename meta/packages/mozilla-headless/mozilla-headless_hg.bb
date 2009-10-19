
FIXEDREV = "${SRCREVMOZILLAHEADLESS}"
#file://0001-Adds-initial-Gtk-clipboard-support-to-moz-headless.patch;patch=1 \
SRC_URI = "hg://hg.mozilla.org/incubator;protocol=http;module=offscreen \
           file://configurefix.patch;patch=1 \
           file://mozilla-jemalloc.patch;patch=1 \
           file://removebadrpath.patch;patch=1 \
           file://jsautocfg.h \
	   file://mozconfig"
PV = "0.1+hg-1.0+${SRCPV}"
PR = "r5"

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

inherit autotools_stage mozilla

acpaths = "-I ${S}/build/autoconf "

export topsrcdir = "${S}"

do_configure () {
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

do_install_append () {
	install -d ${D}${sysconfdir}/ld.so.conf.d/
	echo ${libdir}/xulrunner-1.9.2a1pre/ > ${D}${sysconfdir}/ld.so.conf.d/mozilla-headless
}

EXTRA_OECONF =+ "--enable-application=xulrunner --enable-default-toolkit=cairo-headless --with-pthreads \
                 --enable-pango --enable-optimize --disable-debug --disable-tests \
                 --disable-printing --disable-crashreporter --disable-accessibility \
                 --disable-javaxpcom --enable-plugins --enable-system-sqlite --disable-necko-wifi"

export LIBXUL_DIST="${S}/dist"

do_stage_append () {
	autotools_stage_dir ${STAGE_TEMP}/${libdir}/xulrunner-1.9.2a1pre ${STAGING_DIR_HOST}${libdir}/xulrunner-1.9.2a1pre/
	autotools_stage_dir ${STAGE_TEMP}/${libdir}/xulrunner-devel-1.9.2a1pre ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/
	ln -fs ${STAGING_DIR_HOST}${libdir}/xulrunner-1.9.2a1pre/ ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/bin
	ln -fs ${STAGING_DIR_HOST}${datadir}/xulrunner-1.9.2a1pre/unstable/ ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/idl
	ln -fs ${STAGING_DIR_HOST}${includedir}/xulrunner-1.9.2a1pre/unstable/ ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/include
	ln -fs ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/sdk/lib/ ${STAGING_DIR_HOST}${libdir}/xulrunner-devel-1.9.2a1pre/lib
	install -m 755 ${S}/dist/host/bin/host_xpidl ${STAGING_BINDIR_NATIVE}/xpidl
}

