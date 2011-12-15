require opkg.inc

PROVIDES += "virtual/update-alternatives"
RPROVIDES_update-alternatives-cworth += "update-alternatives"
RCONFLICTS_update-alternatives-cworth = "update-alternatives-dpkg"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives} opkg-config-base"
RDEPENDS_${PN}_virtclass-native = ""
RDEPENDS_${PN}_virtclass-nativesdk = ""
PACKAGE_ARCH_update-alternatives-cworth = "all"
RREPLACES_${PN} = "opkg-nogpg"

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;proto=http \
           file://add_vercmp.patch \
           file://add_uname_support.patch \
           file://fix_installorder.patch \
           file://offline_postinstall.patch\
           file://offlineroot_varname.patch \
"

S = "${WORKDIR}/trunk"

SRCREV = "633"
PV = "0.1.8+svnr${SRCPV}"
PR = "r2"

PACKAGES =+ "libopkg${PKGSUFFIX}-dev libopkg${PKGSUFFIX} update-alternatives-cworth${PKGSUFFIX}"

FILES_update-alternatives-cworth${PKGSUFFIX} = "${bindir}/update-alternatives"
FILES_libopkg${PKGSUFFIX}-dev = "${libdir}/*.a ${libdir}/*.la ${libdir}/*.so"
FILES_libopkg${PKGSUFFIX} = "${libdir}/*.so.* ${localstatedir}/lib/opkg/"

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
rm -f /${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure
" > $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure
	chmod 0755 $D${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure
fi

update-alternatives --install ${bindir}/opkg opkg ${bindir}/opkg-cl 100
}

pkg_postrm_${PN} () {
#!/bin/sh
update-alternatives --remove opkg ${bindir}/opkg-cl
}

