
SRC_URI = "hg://hg.mozilla.org/incubator;protocol=http;rev=75786cabae7f;module=offscreen \
           file://configurefix.patch;patch=1 \
           file://jsautocfg.h \
	   file://mozconfig"
PV = "0.0+hg-1.0+75786cabae7f"
PR = "r0"

S = "${WORKDIR}/offscreen"

DEPENDS = "gconf gnome-vfs pango dbus-glib alsa-lib libidl-native sqlite3 libidl"

FILES_${PN} += "${libdir}/xulrunner-1.9.2a1pre"
FILES_${PN}-dev += "${libdir}/xulrunner-devel-1.9.2a1pre"
FILES_${PN}-dbg += "${libdir}/xulrunner-devel-1.9.2a1pre/sdk/lib/.debug"

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

EXTRA_OECONF =+ "--enable-application=xulrunner --enable-default-toolkit=cairo-headless --enable-pango --disable-optimize --enable-debug --disable-tests --disable-printing --disable-crashreporter --disable-accessibility --disable-javaxpcom --enable-plugins --enable-system-sqlite --disable-necko-wifi"

export LIBXUL_DIST="${S}/dist"

do_stage_append () {
	autotools_stage_dir ${STAGE_TEMP}/${libdir}/xulrunner-1.9.2a1pre ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-1.9.2a1pre/
	autotools_stage_dir ${STAGE_TEMP}/${libdir}/xulrunner-devel-1.9.2a1pre ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/
	ln -fs ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-1.9.2a1pre/ ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/bin
	ln -fs ${STAGING_DIR_HOST}${layout_datadir}/xulrunner-1.9.2a1pre/unstable/ ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/idl
	ln -fs ${STAGING_DIR_HOST}${layout_includedir}/xulrunner-1.9.2a1pre/unstable/ ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/include
	ln -fs ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/sdk/lib/ ${STAGING_DIR_HOST}${layout_libdir}/xulrunner-devel-1.9.2a1pre/lib
}

