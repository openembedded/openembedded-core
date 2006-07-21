inherit base

# The namespaces can clash here hence the two step replace
def get_binconfig_mangle(d):
	import bb.data
	s = "-e ''"
	if not bb.data.inherits_class('native', d):
		s += " -e 's:=${libdir}:=OELIBDIR:;'"
		s += " -e 's:=${includedir}:=OEINCDIR:;'"
		s += " -e 's:=${datadir}:=OEDATADIR:'"
		s += " -e 's:=${prefix}:=OEPREFIX:'"
		s += " -e 's:=${exec_prefix}:=OEEXECPREFIX:'"
		s += " -e 's:-L${libdir}:-LOELIBDIR:;'"
		s += " -e 's:-I${includedir}:-IOEINCDIR:;'"
		s += " -e 's:OELIBDIR:${STAGING_LIBDIR}:;'"
		s += " -e 's:OEINCDIR:${STAGING_INCDIR}:;'"
		s += " -e 's:OEDATADIR:${STAGING_DATADIR}:'"
		s += " -e 's:OEPREFIX:${STAGING_LIBDIR}/..:'"
		s += " -e 's:OEEXECPREFIX:${STAGING_LIBDIR}/..:'"
	return s

# Native package configurations go in ${BINDIR}/<name>-config-native to prevent a collision with cross packages
def is_native(d):
	import bb.data
	return ["","-native"][bb.data.inherits_class('native', d)]

BINCONFIG_GLOB ?= "*-config"

do_stage_append() {
	for config in `find ${S} -name '${BINCONFIG_GLOB}'`; do
		configname=`basename $config`${@is_native(d)}
		install -d ${STAGING_BINDIR}
		cat $config | sed ${@get_binconfig_mangle(d)} > ${STAGING_BINDIR}/$configname
		chmod u+x ${STAGING_BINDIR}/$configname
	done
}
