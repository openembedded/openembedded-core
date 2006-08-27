inherit base

DEPENDS_prepend = "pkgconfig-native "

# The namespaces can clash here hence the two step replace
def get_pkgconfig_mangle(d):
	import bb.data
	s = "-e ''"
	if not bb.data.inherits_class('native', d):
		s += " -e 's:=${libdir}:=OELIBDIR:;'"
		s += " -e 's:=${includedir}:=OEINCDIR:;'"
		s += " -e 's:=${datadir}:=OEDATADIR:'"
		s += " -e 's:=${prefix}:=OEPREFIX:'"
		s += " -e 's:=${exec_prefix}:=OEEXECPREFIX:'"
		s += " -e 's:OELIBDIR:${STAGING_LIBDIR}:;'"
		s += " -e 's:OEINCDIR:${STAGING_INCDIR}:;'"
		s += " -e 's:OEDATADIR:${STAGING_DATADIR}:'"
		s += " -e 's:OEPREFIX:${STAGING_LIBDIR}/..:'"
		s += " -e 's:OEEXECPREFIX:${STAGING_LIBDIR}/..:'"
	return s

do_stage_append () {
	for pc in `find ${S} -name '*.pc' -type f | grep -v -- '-uninstalled.pc$'`; do
		pcname=`basename $pc`
		install -d ${PKG_CONFIG_PATH}
		cat $pc | sed ${@get_pkgconfig_mangle(d)} > ${PKG_CONFIG_PATH}/$pcname
	done
}
