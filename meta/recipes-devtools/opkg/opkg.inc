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
INC_PR = "r11"

# Werror gives all kinds bounds issuses with gcc 4.3.3
do_configure_prepend() {
	sed -i -e s:-Werror::g ${S}/libopkg/Makefile.am
}

inherit autotools pkgconfig
EXTRA_OECONF += " --disable-gpg \
                  --disable-openssl \
                  --disable-ssl-curl \
                  --disable-curl \
                  --disable-sha256"

target_localstatedir := "${localstatedir}"
EXTRA_OECONF += "--with-opkglibdir=${localstatedir}/lib"
EXTRA_OECONF_class-native = "--with-opkglibdir=${target_localstatedir}/lib --disable-gpg --disable-curl --disable-openssl"
EXTRA_OECONF_class-nativesdk = "--with-opkglibdir=${target_localstatedir}/lib --disable-gpg --disable-curl --disable-openssl"

PROVIDES += "virtual/update-alternatives"
RPROVIDES_update-alternatives-cworth += "update-alternatives"
RCONFLICTS_update-alternatives-cworth = "update-alternatives-dpkg"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives} opkg-config-base"
RDEPENDS_${PN}_class-native = ""
RDEPENDS_${PN}_class-nativesdk = ""
RREPLACES_${PN} = "opkg-nogpg"

PACKAGES =+ "libopkg-dev libopkg-staticdev libopkg update-alternatives-cworth"

FILES_update-alternatives-cworth = "${bindir}/update-alternatives"
FILES_libopkg-dev = "${libdir}/*.la ${libdir}/*.so"
FILES_libopkg-staticdev = "${libdir}/*.a"
FILES_libopkg = "${libdir}/*.so.* ${localstatedir}/lib/opkg/"

# We need to create the lock directory
do_install_append() {
	install -d ${D}${localstatedir}/lib/opkg
}

pkg_postinst_${PN} () {
#!/bin/sh
if [ "x$D" != "x" ]; then
	install -d $D${sysconfdir}/rcS.d
	# this happens at S98 where our good 'ole packages script used to run
	echo "#!/bin/sh
opkg-cl configure
rm -f /${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
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
POSTINSTALL_INITPOSITION ?= "98"
