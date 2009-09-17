inherit nativesdk

oe_runconf () {
	if [ -x ${S}/configure ] ; then
		cfgcmd="${S}/configure \
		    --build=${BUILD_SYS} \
		    --host=${HOST_SYS} \
		    --prefix=${prefix} \
		    --exec_prefix=${exec_prefix} \
		    --bindir=${bindir} \
		    --sbindir=${sbindir} \
		    --libexecdir=${libexecdir} \
		    --datadir=${datadir} \
		    --sysconfdir=${sysconfdir} \
		    --sharedstatedir=${sharedstatedir} \
		    --localstatedir=${localstatedir} \
		    --libdir=${libdir} \
		    --includedir=${includedir} \
		    --oldincludedir=${oldincludedir} \
		    --infodir=${infodir} \
		    --mandir=${mandir} \
			${EXTRA_OECONF} \
		    $@"
		oenote "Running $cfgcmd..."
		$cfgcmd || oefatal "oe_runconf failed" 
	else
		oefatal "no configure script found"
	fi
}
