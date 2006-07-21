inherit e

SECTION = "e/libs"

SRCNAME = "${@bb.data.getVar('PN', d, 1).replace('-native', '')}"
SRC_URI = "${E_URI}/${SRCNAME}-${PV}.tar.gz"
S = "${WORKDIR}/${SRCNAME}-${PV}"

INHIBIT_AUTO_STAGE_INCLUDES  = "1"
INHIBIT_NATIVE_STAGE_INSTALL = "1"

libdirectory = "src/lib"
libraries = "lib${SRCNAME}"
headers = "${@bb.data.getVar('SRCNAME',d,1).capitalize()}.h"

do_stage_append () {
	for i in ${libraries}
	do
		oe_libinstall -C ${libdirectory} $i ${STAGING_LIBDIR}
	done
	for i in ${headers}
	do
		install -m 0644 ${libdirectory}/$i ${STAGING_INCDIR}
	done

	# Install binaries automatically for native builds
	if [ "${@binconfig_suffix(d)}" = "-native" ]
	then

		# Most EFL binaries start with the package name
		for i in src/bin/${SRCNAME}*
		do
			if [ -x $i -a -f $i ]
			then

				# Don't install anything with an extension (.so, etc)
				if echo $i | grep -v \\.
				then
					${HOST_SYS}-libtool --mode=install install -m 0755 $i ${STAGING_BINDIR}
				fi
			fi
		done
	fi
}

PACKAGES = "${PN} ${PN}-themes ${PN}-dev ${PN}-examples"
FILES_${PN}-dev = "${bindir}/${PN}-config ${libdir}/pkgconfig ${libdir}/lib*.?a ${libdir}/lib*.a"
FILES_${PN}-examples = "${bindir} ${datadir}"

