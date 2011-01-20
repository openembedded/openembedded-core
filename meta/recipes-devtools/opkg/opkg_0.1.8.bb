require opkg.inc

PROVIDES += "virtual/update-alternatives"
RPROVIDES_update-alternatives-cworth += "update-alternatives"
RCONFLICTS_update-alternatives-cworth = "update-alternatives-dpkg"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives} opkg-config-base"
RDEPENDS_${PN}_virtclass-native = ""
RDEPENDS_${PN}_virtclass-nativesdk = ""
PACKAGE_ARCH_update-alternatives-cworth = "all"

SRC_URI = "http://opkg.googlecode.com/files/opkg-${PV}.tar.gz \
           file://add_vercmp.patch \
           file://headerfix.patch \
"

PR = "r3"

PACKAGES =+ "libopkg${PKGSUFFIX}-dev libopkg${PKGSUFFIX} update-alternatives-cworth${PKGSUFFIX}"

FILES_update-alternatives-cworth${PKGSUFFIX} = "${bindir}/update-alternatives"
FILES_libopkg${PKGSUFFIX}-dev = "${libdir}/*.a ${libdir}/*.la ${libdir}/*.so"
FILES_libopkg${PKGSUFFIX} = "${libdir}/*.so.* ${localstatedir}/lib/opkg/"

# We need to create the lock directory
do_install_append() {
	install -d ${D}${localstatedir}/lib/opkg
}

# Define a variable to allow distros to run configure earlier.
# (for example, to enable loading of ethernet kernel modules before networking starts)
OPKG_INIT_POSITION = "98"
OPKG_INIT_POSITION_slugos = "41"

pkg_postinst_${PN} () {
#!/bin/sh
if [ "x$D" != "x" ]; then
	install -d ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d
	# this happens at S98 where our good 'ole packages script used to run
	echo "#!/bin/sh
opkg-cl configure
rm -f /${sysconfdir}/rcS.d/S${OPKG_INIT_POSITION}configure
" > $D${sysconfdir}/rcS.d/S${OPKG_INIT_POSITION}configure
	chmod 0755 $D${sysconfdir}/rcS.d/S${OPKG_INIT_POSITION}configure
fi

update-alternatives --install ${bindir}/opkg opkg ${bindir}/opkg-cl 100
}

pkg_postrm_${PN} () {
#!/bin/sh
update-alternatives --remove opkg ${bindir}/opkg-cl
}

