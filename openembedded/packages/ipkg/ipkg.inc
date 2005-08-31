DESCRIPTION = "Itsy Package Manager"
DESCRIPTION_libipkg = "Itsy Package Manager Library"
SECTION = "base"
LICENSE = "GPL"
PROVIDES = "virtual/ipkg libipkg"

PACKAGES =+ "libipkg-dev libipkg"
FILES_libipkg-dev = "${libdir}/*.a ${libdir}/*.la ${libdir}/*.so"
FILES_libipkg = "${libdir}"
AUTO_LIBNAME_PKGS = "libipkg"

SRC_URI = "${HANDHELDS_CVS};module=familiar/dist/ipkg;tag=${@'V' + bb.data.getVar('PV',d,1).replace('.', '-')} \
	file://terse.patch;patch=1"

S = "${WORKDIR}/ipkg/C"

inherit autotools pkgconfig

pkg_postinst_ipkg () {
#!/bin/sh
if [ "x$D" != "x" ]; then
	install -d ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d
	# this happens at S98 where our good 'ole packages script used to run
	echo -e "#!/bin/sh
ipkg-cl configure
" > ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d/S98configure
	chmod 0755 ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d/S98configure
fi

update-alternatives --install ${bindir}/ipkg ipkg ${bindir}/ipkg-cl 100
}

pkg_postrm_ipkg () {
#!/bin/sh
update-alternatives --remove ipkg ${bindir}/ipkg-cl
}

do_stage() {
	oe_libinstall -so libipkg ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/replace/
	install -m 0644 replace/replace.h ${STAGING_INCDIR}/replace/
	install -d ${STAGING_INCDIR}/libipkg/
	for f in *.h
	do
		install -m 0644 $f ${STAGING_INCDIR}/libipkg/
	done
}

#
# FIXME: Install /etc/ipkg.conf and /etc/ipkg/arch.conf
#
