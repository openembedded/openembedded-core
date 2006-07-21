DEPENDS_prepend="tmake "

python tmake_do_createpro() {
	import glob, sys
	from bb import note
	out_vartranslate = {
		"TMAKE_HEADERS": "HEADERS",
		"TMAKE_INTERFACES": "INTERFACES",
		"TMAKE_TEMPLATE": "TEMPLATE",
		"TMAKE_CONFIG": "CONFIG",
		"TMAKE_DESTDIR": "DESTDIR",
		"TMAKE_SOURCES": "SOURCES",
		"TMAKE_DEPENDPATH": "DEPENDPATH",
		"TMAKE_INCLUDEPATH": "INCLUDEPATH",
		"TMAKE_TARGET": "TARGET",
		"TMAKE_LIBS": "LIBS",
	}
	s = data.getVar('S', d, 1) or ""
	os.chdir(s)
	profiles = (data.getVar('TMAKE_PROFILES', d, 1) or "").split()
	if not profiles:
		profiles = ["*.pro"]
	for pro in profiles:
		ppro = glob.glob(pro)
		if ppro:
			if ppro != [pro]:
				del profiles[profiles.index(pro)]
				profiles += ppro
				continue
			if ppro[0].find('*'):
				del profiles[profiles.index(pro)]
				continue
		else:
			del profiles[profiles.index(pro)]
	if len(profiles) != 0:
		return

	# output .pro using this metadata store
	try:
		from __builtin__ import file
		profile = file(data.expand('${PN}.pro', d), 'w')
	except OSError:
		raise FuncFailed("unable to open pro file for writing.")

#	fd = sys.__stdout__
	fd = profile
	for var in out_vartranslate.keys():
		val = data.getVar(var, d, 1)
		if val:
			fd.write("%s\t: %s\n" % (out_vartranslate[var], val))

#	if fd is not sys.__stdout__:
	fd.close()
}

tmake_do_configure() {
	paths="${STAGING_DATADIR}/tmake/qws/${TARGET_OS}-${TARGET_ARCH}-g++ $STAGING_DIR/share/tmake/$OS-g++"
	if (echo "${TARGET_ARCH}"|grep -q 'i.86'); then
		paths="${STAGING_DATADIR}/tmake/qws/${TARGET_OS}-x86-g++ $paths"
	fi
	for i in $paths; do
		if test -e $i; then
			export TMAKEPATH=$i
			break
		fi
	done

	if [ -z "${TMAKE_PROFILES}" ]; then
		TMAKE_PROFILES="`ls *.pro`"
	fi
	tmake -o Makefile $TMAKE_PROFILES || die "Error calling tmake on ${TMAKE_PROFILES}"
}

EXPORT_FUNCTIONS do_configure do_createpro

addtask configure after do_unpack do_patch before do_compile
addtask createpro before do_configure after do_unpack do_patch
