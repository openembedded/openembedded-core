inherit base

# The namespaces can clash here hence the two step replace
def get_binconfig_mangle(d):
	import bb.data
	s = "-e ''"
	if not bb.data.inherits_class('native', d):
		optional_quote = r"\(\"\?\)"
		s += " -e 's:=%s${libdir}:=\\1OELIBDIR:;'" % optional_quote
		s += " -e 's:=%s${includedir}:=\\1OEINCDIR:;'" % optional_quote
		s += " -e 's:=%s${datadir}:=\\1OEDATADIR:'" % optional_quote
		s += " -e 's:=%s${prefix}:=\\1OEPREFIX:'" % optional_quote
		s += " -e 's:=%s${exec_prefix}:=\\1OEEXECPREFIX:'" % optional_quote
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

do_install_append() {

    #the 'if' protects native packages, since we can't easily check for bb.data.inherits_class('native', d) in shell 
    if [ -e ${D}${bindir} ] ; then
        for config in `find ${S} -name '${BINCONFIG_GLOB}'`; do
                cat $config | sed \
		-e 's:${STAGING_LIBDIR}:${libdir}:g;' \ 
		-e 's:${STAGING_INCDIR}:${includedir}:g;' \
		-e 's:${STAGING_DATADIR}:${datadir}:' \
		-e 's:${STAGING_LIBDIR}/..:${prefix}:' > ${D}${bindir}/`basename $config`
        done
    fi	

	for lafile in `find ${D} -name *.la` ; do
		sed -i \
		    -e 's:${STAGING_LIBDIR}:${libdir}:g;' \
		    -e 's:${STAGING_INCDIR}:${includedir}:g;' \
		    -e 's:${STAGING_DATADIR}:${datadir}:' \
		    -e 's:${STAGING_LIBDIR}/..:${prefix}:' \
		    $lafile
	done	    
}

do_stage_append() {
	for config in `find ${S} -name '${BINCONFIG_GLOB}'`; do
		configname=`basename $config`${@is_native(d)}
		install -d ${STAGING_BINDIR}
		cat $config | sed ${@get_binconfig_mangle(d)} > ${STAGING_BINDIR}/$configname
		chmod u+x ${STAGING_BINDIR}/$configname
	done
}
