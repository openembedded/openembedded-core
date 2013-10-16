DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
HOMEPAGE = "http://code.google.com/p/opkg/"
BUGTRACKER = "http://code.google.com/p/opkg/issues/list"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/opkg-cl.c;beginline=1;endline=20;md5=321f658c3f6b6c832e25c8850b5dffba"

PE = "1"
INC_PR = "r13"

# Werror gives all kinds bounds issuses with gcc 4.3.3
do_configure_prepend() {
	sed -i -e s:-Werror::g ${S}/libopkg/Makefile.am
}

inherit autotools pkgconfig systemd

python () {
    if 'sysvinit' not in d.getVar("DISTRO_FEATURES", True).split():
        pn = d.getVar('PN', True)
        d.setVar('SYSTEMD_SERVICE_%s' % (pn), 'opkg-configure.service')
}

target_localstatedir := "${localstatedir}"
OPKGLIBDIR = "${target_localstatedir}/lib"

SHARED_EXTRA_OECONF = "\
  --with-opkglibdir=${OPKGLIBDIR} \
  --disable-gpg \
  --disable-curl \
  --disable-openssl \
"
EXTRA_OECONF_class-target = "${SHARED_EXTRA_OECONF} --disable-ssl-curl --disable-sha256"
EXTRA_OECONF_class-native = "${SHARED_EXTRA_OECONF}"
EXTRA_OECONF_class-nativesdk = "${SHARED_EXTRA_OECONF}"

PROVIDES += "virtual/update-alternatives"
RPROVIDES_update-alternatives-cworth += "update-alternatives"
RCONFLICTS_update-alternatives-cworth = "update-alternatives-dpkg"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives} opkg-config-base"
RDEPENDS_${PN}_class-native = ""
RDEPENDS_${PN}_class-nativesdk = ""
RREPLACES_${PN} = "opkg-nogpg"

PACKAGES =+ "libopkg-dev libopkg-staticdev libopkg update-alternatives-cworth"

FILES_update-alternatives-cworth = "${bindir}/update-alternatives"
FILES_libopkg-dev = "${libdir}/*.la ${libdir}/*.so ${includedir}/libopkg"
FILES_libopkg-staticdev = "${libdir}/*.a"
FILES_libopkg = "${libdir}/*.so.* ${OPKGLIBDIR}/opkg/"
FILES_${PN} += "${systemd_unitdir}/system/"

do_install_append() {
	# We need to create the lock directory
	install -d ${D}${OPKGLIBDIR}/opkg

	if ${@base_contains('DISTRO_FEATURES','sysvinit','false','true',d)};then
		install -d ${D}${systemd_unitdir}/system
		install -m 0644 ${WORKDIR}/opkg-configure.service ${D}${systemd_unitdir}/system/
		sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
			-e 's,@SYSCONFDIR@,${sysconfdir},g' \
			-e 's,@BINDIR@,${bindir},g' \
			-e 's,@SYSTEMD_UNITDIR@,${systemd_unitdir},g' \
			${D}${systemd_unitdir}/system/opkg-configure.service
	fi
}

do_install_append_class-native() {
	if [ "${PREFERRED_PROVIDER_virtual/update-alternatives-native}" != "${PN}" ]; then
		rm ${D}${bindir}/update-alternatives
	fi
}

pkg_postinst_${PN} () {
#!/bin/sh
if ${@base_contains('DISTRO_FEATURES','sysvinit','true','false',d)} && \
	[ "x$D" != "x" ] && [ -f $D${OPKGLIBDIR}/opkg/status ]; then
	install -d $D${sysconfdir}/rcS.d

	# this happens at S98 where our good 'ole packages script used to run
	echo "#!/bin/sh
[ -e ${sysconfdir}/default/postinst ] && . ${sysconfdir}/default/postinst
if [ \"\$POSTINST_LOGGING\" = \"1\" ]; then
    opkg-cl configure >\$LOGFILE 2>&1
else
    opkg-cl configure
fi
rm -f ${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
" > $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
	chmod 0755 $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
fi
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "opkg"
ALTERNATIVE_LINK_NAME[opkg] = "${bindir}/opkg"
ALTERNATIVE_TARGET[opkg] = "${bindir}/opkg-cl"

BBCLASSEXTEND = "native nativesdk"

# Define a variable to allow distros to run configure earlier.
# (for example, to enable loading of ethernet kernel modules before networking starts)
# note: modifying name or default value for POSTINSTALL_INITPOSITION requires
# changes in systemd-compat-units.bb
POSTINSTALL_INITPOSITION ?= "98"
