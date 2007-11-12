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
		s += " -e 's:OEPREFIX:${STAGING_DIR_HOST}${layout_prefix}:'"
		s += " -e 's:OEEXECPREFIX:${STAGING_DIR_HOST}${layout_exec_prefix}:'"
		s += " -e 's:-I${WORKDIR}:-I${STAGING_INCDIR}:'"
		s += " -e 's:-L${WORKDIR}:-L${STAGING_LIBDIR}:'"
	return s

BINCONFIG_GLOB ?= "*-config"

do_install_append() {

    #the 'if' protects native packages, since we can't easily check for bb.data.inherits_class('native', d) in shell 
    if [ -e ${D}${bindir} ] ; then
        for config in `find ${S} -name '${BINCONFIG_GLOB}'`; do
                cat $config | sed \
		-e 's:${STAGING_LIBDIR}:${libdir}:g;' \ 
		-e 's:${STAGING_INCDIR}:${includedir}:g;' \
		-e 's:${STAGING_DATADIR}:${datadir}:' \
		-e 's:${STAGING_DIR_HOST}${layout_prefix}:${prefix}:' > ${D}${bindir}/`basename $config`
        done
    fi	

	for lafile in `find ${D} -name *.la` ; do
		sed -i \
		    -e 's:${STAGING_LIBDIR}:${libdir}:g;' \
		    -e 's:${STAGING_INCDIR}:${includedir}:g;' \
		    -e 's:${STAGING_DATADIR}:${datadir}:' \
		    -e 's:${STAGING_DIR_HOST}${layout_prefix}:${prefix}:' \
		    $lafile
	done	    
}

do_stage_append() {
	for config in `find ${S} -name '${BINCONFIG_GLOB}'`; do
		configname=`basename $config`
		install -d ${STAGING_BINDIR_CROSS}
		cat $config | sed ${@get_binconfig_mangle(d)} > ${STAGING_BINDIR_CROSS}/$configname
		chmod u+x ${STAGING_BINDIR_CROSS}/$configname
	done
}
