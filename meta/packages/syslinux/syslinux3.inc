require syslinux.inc

STAGE_TEMP="${WORKDIR}/stage_temp"

do_stage() {
	install -d ${STAGE_TEMP}
	oe_runmake install INSTALLROOT="${STAGE_TEMP}"

	install -d ${STAGING_BINDIR}
	install -m 755 ${STAGE_TEMP}/usr/bin/syslinux ${STAGING_BINDIR}
	install -m 755 ${STAGE_TEMP}/sbin/extlinux ${STAGING_BINDIR}

	# When building media, the syslinux binary isn't nearly as useful
	# as the DOS data files, so we copy those into a special location
	# for usage during a image build stage

	install -d ${STAGING_DATADIR}/syslinux
	install -m 0644 ${STAGE_TEMP}/usr/lib/syslinux/isolinux.bin ${STAGING_DATADIR}/syslinux/isolinux.bin
	install -m 644 ${S}/ldlinux.sys ${STAGING_DATADIR}/syslinux/ldlinux.sys
	install -m 644 ${S}/ldlinux.bss ${STAGING_DATADIR}/syslinux/ldlinux.bss
}
